package cistiakj.Router;

import java.net.DatagramPacket;
import java.net.InetAddress;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.PacketTypes;
/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *	A helper thread of Router whose function is to send packet.
 */
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
				//pk.setSocketAddress(gp.getFinalAddr());
				//parent.socket.send(pk);
				if(!parent.hasRoute(gp.getFinalDest())) {
					System.err.println("destination not found" + gp.getFinalDest());
				}
				assert(parent.hasRoute(gp.getFinalDest()));
				// get interface from which to send
				int outId = parent.getNextHop(gp.getFinalDest());
				//pk.setPort(outId);
				pk.setAddress(InetAddress.getLocalHost());
				parent.interfaces.get(outId).send(parent.socket, pk);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
