package cistiakj.Router;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cistiakj.Constants;
import cistiakj.FlowTable.RouterFlowTableEntry;
import cistiakj.Packets.AckPacket;
import cistiakj.Packets.EchoReplyPacket;
import cistiakj.Packets.FeatureReplyPacket;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.GetConfigReplyPacket;
import cistiakj.Packets.GetConfigRequestPacket;
import cistiakj.Packets.HelloPacket;
import cistiakj.Packets.OFPacket;
import cistiakj.Packets.PacketInPacket;
import cistiakj.Packets.PacketOutPacket;
import cistiakj.Packets.PacketTypes;
import cistiakj.Packets.SetConfigPacket;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *	A thread of Router that deals with processing of OpenFlow communication. i.e. communication
 *	with the Controller
 */
public class ControllingThread implements Runnable, Constants, PacketTypes {

	Router parent;
	// seq number is only needed for communication with controller
	int seq;

	// store last sent packet so that it could be resent?

	public ControllingThread(Router parent) {
		super();
		this.parent = parent;
		this.seq = 0;
	}

	@Override
	public void run() {
		GenericPacket gp = null;
		OFPacket ofpk = null;
		try {
			connect();
			for (;;) {
				//processController();
				gp = parent.resolveQueue.poll(PACKET_WAITING_TIME_IN_SEC, TimeUnit.SECONDS);
				// poll leads to high cpu usage
				//gp = parent.resolveQueue.poll();
				processController();
				if (gp == null) {
					continue;
				}
				//int dest = gp.getFinalDest();
				//DatagramPacket packet = gp.getPayload();
				// communicate with controller to get a path
				findPath(gp);
			}
		} catch (InterruptedException | TimeoutException | IOException e) {
			e.printStackTrace();
		} 
//		catch (TimeoutException e) {
//			System.err.println("Timeout has occured. Connection unsuccessful");
//			e.printStackTrace();
//		}
	}

	private void findPath(GenericPacket gp) throws InterruptedException {
		// TODO: do something :)
		// send packetOut, receive setConfig, receive packetIn
		PacketOutPacket popk = new PacketOutPacket(parent.protocolVersion, parent.routerId, seq, gp, parent.srcPort);
		sendController(popk.toDatagramPacket());
	}

	private void processController() throws InterruptedException, IOException {
		while (!parent.controllerQueue.isEmpty()) {
			processControllerPackets(parent.controllerQueue.take());
		}
	}

	private void processControllerPackets(OFPacket ofpk) throws InterruptedException, IOException {

		switch (ofpk.getType()) {
		case OFPT_ECHO_REQUEST:
			EchoReplyPacket erpk = new EchoReplyPacket(parent.protocolVersion, parent.routerId, seq,
					ECHO_OPTION_DEFAULT);
			sendController(erpk.toDatagramPacket());
			break;
		case OFPT_GET_CONFIG_REQUEST:
			GetConfigReplyPacket gcrp = new GetConfigReplyPacket(parent.protocolVersion, parent.routerId, seq,
					parent.flowTable);
			sendController(gcrp.toDatagramPacket());
			break;
		case OFPT_SET_CONFIG:
			SetConfigPacket scp = (SetConfigPacket) ofpk;
			for (RouterFlowTableEntry entry : scp.entries) {
				parent.addEntry(entry);
			}
			AckPacket ackpk = new AckPacket(parent.protocolVersion, parent.protocolVersion, seq);
			sendController(ackpk.toDatagramPacket());
			break;
		case OFPT_PACKET_IN:
			// forward packet to destination
			// TODO: error cases?? or just drop the packet and let the end use to take care
			PacketInPacket pipk = (PacketInPacket) ofpk;
			GenericPacket gp = pipk.toGenericPacket();
			if (parent.hasRoute(gp.getFinalDest())) {
				parent.sendQueue.put(gp);
			}else {
				System.err.println("no destination found " + gp.getFinalDest());
			}
			break;
		case OFPT_FEATURES_REQUEST:
			Interface[] interfaces = new Interface[parent.interfaces.size()];
			parent.interfaces.values().toArray(interfaces);
			FeatureReplyPacket frp = new FeatureReplyPacket(parent.protocolVersion, parent.routerId, seq, interfaces);
			sendController(frp.toDatagramPacket());
			break;
		default:
			break;
		}
	}

	private void connect() throws TimeoutException {
		try {
			int i;
			HelloPacket hp = new HelloPacket(parent.protocolVersion, parent.routerId, seq);
			DatagramPacket packet = hp.toDatagramPacket();
			GenericPacket gpOut = null;
			OFPacket ofpk = sendAndListenController(packet);
			if (ofpk == null) {
				throw new TimeoutException();
			}
			// TODO: look at possible infinite loop
			i = 0;
//			while (!ofpk.getType().equals(OPF_TYPE.OFPT_FEATURES_REQUEST) && i < WAIT_LIMIT) {
//				ofpk = sendAndListenController(packet);
//				i++;
//			}
			ofpk = sendAndListenController(packet);
			if(i == WAIT_LIMIT) {
				throw new TimeoutException();
			}
			// now send a features reply
			Interface[] interfaces = new Interface[parent.interfaces.size()];
			parent.interfaces.values().toArray(interfaces);
			FeatureReplyPacket frp = new FeatureReplyPacket(parent.protocolVersion, parent.routerId, seq, interfaces);
			packet = frp.toDatagramPacket();
			ofpk = sendAndListenController(packet);
			i = 0;
			//TODO: waiting fro ACK here - not necessary
//			while (!ofpk.getType().equals(OPF_TYPE.OFPT_ACK) && i < WAIT_LIMIT) {
//				ofpk = sendAndListenController(packet);
//			}
//			ofpk = sendAndListenController(packet);
//			if(i == WAIT_LIMIT) {
//				throw new TimeoutException();
//			}
			// successfully connected to controller and entered network
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private OFPacket sendAndListenController(DatagramPacket dtpk) throws TimeoutException, InterruptedException {
		OFPacket gpIn = null;
		//TODO: check this. i it neede at all
		for (int i = 0; i < 1; i++) {
			sendController(dtpk);
			try {
				gpIn = parent.controllerQueue.poll(PACKET_WAITING_TIME_IN_SEC, TimeUnit.SECONDS);
				if (gpIn == null) {
					continue;
				}else {
					return gpIn;
				}
			} catch (InterruptedException e) {
				System.err.println("timeout - trying again");
				// do nothing
			}
		}
		return gpIn;
	}

	private void sendController(DatagramPacket dtpk) throws InterruptedException {
		dtpk.setAddress(parent.controllerAddress.getAddress());
		dtpk.setPort(parent.controllerAddress.getPort());
		GenericPacket gpOut = new GenericPacket(dtpk);
		parent.sendQueue.put(gpOut);
	}

}
