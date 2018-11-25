package cistiakj.Router;

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
				processController();
				gp = parent.resolveQueue.poll(PACKET_WAITING_TIME_IN_SEC, TimeUnit.SECONDS);
				processController();
				if (gp == null) {
					continue;
				}
				//int dest = gp.getFinalDest();
				//DatagramPacket packet = gp.getPayload();
				// communicate with controller to get a path
				findPath(gp);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			System.err.println("Timeout has occured. Connection unsuccessful");
			e.printStackTrace();
		}
	}

	private void findPath(GenericPacket gp) throws InterruptedException {
		// TODO: do something :)
		// send packetOut, receive setConfig, receive packetIn
		PacketOutPacket popk = new PacketOutPacket(parent.protocolVersion, parent.protocolVersion, seq, gp);
		sendController(popk.toDatagramPacket());
	}

	private void processController() throws InterruptedException {
		while (!parent.controllerQueue.isEmpty()) {
			processControllerPackets(parent.controllerQueue.take());
		}
	}

	private void processControllerPackets(OFPacket ofpk) throws InterruptedException {

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
			GenericPacket gp = pipk.packet;
			if (parent.hasRoute(gp.getFinalDest())) {
				parent.sendQueue.put(gp);
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
			HelloPacket hp = new HelloPacket(parent.protocolVersion, parent.routerId, seq);
			DatagramPacket packet = hp.toDatagramPacket();
			GenericPacket gpOut = null;
			OFPacket ofpk = sendAndListenController(packet);
			if (ofpk == null) {
				throw new TimeoutException();
			}
			// TODO: look at possible infinite loop
			while (!ofpk.getType().equals(OPF_TYPE.OFPT_FEATURES_REQUEST)) {
				ofpk = sendAndListenController(packet);
			}
			// now send a features reply
			Interface[] interfaces = new Interface[parent.interfaces.size()];
			parent.interfaces.values().toArray(interfaces);
			FeatureReplyPacket frp = new FeatureReplyPacket(parent.protocolVersion, parent.routerId, seq, interfaces);
			packet = frp.toDatagramPacket();
			ofpk = sendAndListenController(packet);
			while (!ofpk.getType().equals(OPF_TYPE.OFPT_ACK)) {
				ofpk = sendAndListenController(packet);
			}
			// successfully connected to controller and entered network
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private OFPacket sendAndListenController(DatagramPacket dtpk) throws TimeoutException, InterruptedException {
		OFPacket gpIn = null;
		for (int i = 0; i < 3; i++) {
			sendController(dtpk);
			try {
				gpIn = parent.controllerQueue.poll(PACKET_WAITING_TIME_IN_SEC, TimeUnit.SECONDS);
				if (gpIn == null) {
					continue;
				}
			} catch (InterruptedException e) {
				// do nothing
			}
		}
		return gpIn;
	}

	private void sendController(DatagramPacket dtpk) throws InterruptedException {
		dtpk.setAddress(parent.controllerAddress.getAddress());
		GenericPacket gpOut = new GenericPacket(dtpk);
		parent.sendQueue.put(gpOut);
	}

}
