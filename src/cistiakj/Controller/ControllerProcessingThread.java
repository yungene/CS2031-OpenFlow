package cistiakj.Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import cistiakj.Constants;
import cistiakj.FlowTable.ControllerFlowTableEntry;
import cistiakj.FlowTable.FlowTableEntry;
import cistiakj.FlowTable.RouterFlowTableEntry;
import cistiakj.Packets.FeatureReplyPacket;
import cistiakj.Packets.FeatureRequestPacket;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.OFPacket;
import cistiakj.Packets.PacketInPacket;
import cistiakj.Packets.PacketOutPacket;
import cistiakj.Packets.PacketTypes;
import cistiakj.Packets.SetConfigPacket;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 * A thread of controller that deals with processing of queries from routers.
 */
public class ControllerProcessingThread implements Runnable, PacketTypes, Constants {
	Controller controller;
	int seq;

	public ControllerProcessingThread(Controller parent) {
		this.controller = parent;
	}

	@Override
	public void run() {
		OFPacket ofpk = null;
		for (;;) {
			try {
				ofpk = controller.processQueue.take();
				process(ofpk);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	
	void process(OFPacket ofpk) throws IOException, InterruptedException {
		
		switch(ofpk.getType()) {
		case OFPT_ACK:
			break;
		case OFPT_HELLO:
			int id = ofpk.getConnectionId();
			
			FeatureRequestPacket frpk = new FeatureRequestPacket(controller.protocolVersion, controller.controllerId, seq);
			sendRouter(frpk, ofpk.getConnectionId());
			break;
		case OFPT_FEATURES_REPLY:
			FeatureReplyPacket frp = (FeatureReplyPacket) ofpk;
			//TODO: check whether connection id would work
			controller.buildNetwork(ofpk.getConnectionId(), frp.entries);
			break;
		case OFPT_GET_CONFIG_REPLY:
			break;
		case OFPT_PACKET_OUT:
			if(controller.tableChanged) {
				System.out.println("building a table");
				controller.buildFlowTable();
			}
			//packet requires forwarding
			PacketOutPacket pop = (PacketOutPacket) ofpk;
			System.out.println("built a table correctly");
			sendPath(ofpk.getConnectionId(), pop.destPort);
			
			PacketInPacket pip = new PacketInPacket(controller.protocolVersion,controller.controllerId, seq, pop);
			sendRouter(pip, ofpk.getConnectionId());
			break;
		case OFPT_ECHO_REQUEST:
			//ignore for now
			break;
		case OFPT_ECHO_REPLY:
			break;
		case OFPT_ERROR:
			break;
		default:
			break;
		}
	}
	
	void sendPath(int src, int dest) throws IOException, InterruptedException {
		// be careful it might send packet to endpoint
		FlowTableEntry entries = controller.flowTable.getEntry(dest, src);
		for(ControllerFlowTableEntry entry: entries.getEntries()) {
			RouterFlowTableEntry routerEntry = new RouterFlowTableEntry(dest, entry.getInInterfaceId(), entry.getOutInterfaceId());
			RouterFlowTableEntry[] routerEntries = new RouterFlowTableEntry[1];
			routerEntries[0] = routerEntry;
			SetConfigPacket scpk = new SetConfigPacket(controller.protocolVersion, controller.controllerId, seq, routerEntries);
			sendRouter(scpk, entry.getRouterId());
		}
		
	}
	
	void sendRouter(OFPacket ofpk, int RouterId) throws IOException, InterruptedException {
		DatagramPacket dp = ofpk.toDatagramPacket();
		dp.setSocketAddress(new InetSocketAddress(InetAddress.getLocalHost(), RouterId));
		GenericPacket gp = new GenericPacket(dp);
		controller.sendQueue.put(gp);
	}
}
