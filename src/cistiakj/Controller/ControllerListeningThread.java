package cistiakj.Controller;

import java.io.IOException;
import java.net.DatagramPacket;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.OFPacket;
import cistiakj.Packets.PacketTypes;

public class ControllerListeningThread implements Runnable, PacketTypes, Constants {

	Controller controller;

	public ControllerListeningThread(Controller parent) {
		this.controller = parent;
	}

	@Override
	public void run() {
		for (;;) {
			try {
				DatagramPacket pk = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
				controller.socket.receive(pk);
				GenericPacket gp = GenericPacket.fromDatagramPacket(pk);
				if (gp == null || gp.getFinalDest() != controller.srcPort) {
					continue;
				}
				OFPacket ofpk = OFPacket.fromDatagramPacket(gp.getPayload());
				if(ofpk == null) {
					continue;
				}
				controller.processQueue.add(ofpk);
				//maybe spawn a new thread instead
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
