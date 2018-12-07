package cistiakj.Controller;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.PacketTypes;

public class ControllerSendingThread  implements Runnable, PacketTypes, Constants {
	Controller controller;
	public ControllerSendingThread(Controller parent) {
		this.controller = parent;
	}

	@Override
	public void run() {
		GenericPacket gp = null;
		for (;;) {
			try {
				gp = controller.sendQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
