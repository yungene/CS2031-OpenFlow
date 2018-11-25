package cistiakj.Router;

import java.net.DatagramPacket;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.PacketTypes;

public class SendingThread implements Runnable, Constants, PacketTypes {

	Router parent;

	public SendingThread(Router parent) {
		super();
		this.parent = parent;
	}

	@Override
	public void run() {
		for (;;) {
			try {
				GenericPacket gp = parent.sendQueue.take();
				DatagramPacket pk = gp.toDatagramPacket();
				pk.setSocketAddress(gp.getFinalAddr());
				//parent.socket.send(pk);
				assert(parent.hasRoute(gp.getFinalDest()));
				// get interface from which to send
				int outId = parent.getNextHop(gp.getFinalDest());
				parent.interfaces.get(outId).send(parent.socket, pk);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
