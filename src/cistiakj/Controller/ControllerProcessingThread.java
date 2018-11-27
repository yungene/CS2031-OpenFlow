package cistiakj.Controller;

import cistiakj.Constants;
import cistiakj.Packets.AckPacket;
import cistiakj.Packets.EchoReplyPacket;
import cistiakj.Packets.EchoRequestPacket;
import cistiakj.Packets.ErrorPacket;
import cistiakj.Packets.FeatureReplyPacket;
import cistiakj.Packets.FeatureRequestPacket;
import cistiakj.Packets.GetConfigReplyPacket;
import cistiakj.Packets.GetConfigRequestPacket;
import cistiakj.Packets.HelloPacket;
import cistiakj.Packets.OFPacket;
import cistiakj.Packets.PacketInPacket;
import cistiakj.Packets.PacketOutPacket;
import cistiakj.Packets.PacketTypes;
import cistiakj.Packets.SetConfigPacket;

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
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	
	void process(OFPacket ofpk) {
		
		switch(ofpk.getType()) {
		case OFPT_ACK:
			break;
		case OFPT_HELLO:
			FeatureRequestPacket frpk = new FeatureRequestPacket(controller.protocolVersion, controller.controllerId, seq);
			sendRouter(frpk, ofpk.getConnectionId());
			break;
		case OFPT_FEATURES_REPLY:
			break;
		case OFPT_GET_CONFIG_REPLY:
			break;
		case OFPT_PACKET_OUT:
			break;
		case OFPT_ECHO_REQUEST:
			break;
		case OFPT_ECHO_REPLY:
			break;
		case OFPT_ERROR:
			break;
		default:
			break;
		}
	}
	
	void sendRouter(OFPacket ofpk, int RouterId) {
		
	}
}
