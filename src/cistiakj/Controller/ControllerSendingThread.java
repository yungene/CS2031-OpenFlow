package cistiakj.Controller;

import java.io.IOException;
import java.net.DatagramPacket;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.PacketTypes;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *	A helper thread of Controller whose function is to send packets.
 */
public class ControllerSendingThread  implements Runnable, PacketTypes, Constants {
	Controller controller;
	public ControllerSendingThread(Controller parent) {
		this.controller = parent;
	}

	@Override
	public void run() {
		GenericPacket gp = null;
		DatagramPacket pk = null;
		for (;;) {
			try {
				gp = controller.sendQueue.take();
				pk = gp.toDatagramPacket();
				pk.setAddress(gp.getFinalAddr().getAddress());
				pk.setPort(gp.getFinalAddr().getPort());
				controller.socket.send(pk);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}

		}
	}
}
