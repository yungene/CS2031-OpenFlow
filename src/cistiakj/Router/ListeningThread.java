package cistiakj.Router;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.OFPacket;
import cistiakj.Packets.PacketTypes;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *	A helper thread of Router whose function is to listen for incoming packets
 *	and decide how to deal with that packet. It only support GenericPackets.
 */
public class ListeningThread implements Runnable, Constants, PacketTypes {

	Router parent;
	DatagramSocket socket;

	public ListeningThread(Router parent) {
		super();
		this.parent = parent;
		socket = parent.socket;
	}

	@Override
	public void run() {
		for (;;) {
			try {
				DatagramPacket pk = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
				socket.receive(pk);
				GenericPacket gp = GenericPacket.fromDatagramPacket(pk);
				if (gp == null) {
					continue;
				}
				int finalDest = gp.getFinalDest();
				if (finalDest == parent.srcPort) { // from the controller intended for router
					DatagramPacket dtpk = gp.getPayload();
					if (dtpk != null) {
						OFPacket ofpk = OFPacket.fromDatagramPacket(gp.getPayload());
						if (ofpk != null) {
							//TODO: look over this and test
							// a trick here to make ControllingThread to skip over listening to resolve queue and to start listening
							// controller queue because controller queue has packets arrived. Hence controller queue has higher priority
							// possibly needs testing
							//parent.resolveQueue.put(null);
							parent.controllerQueue.put(ofpk);
						}
					}
				} else {
					// not inteded for the router => need to forward
					if (parent.hasRoute(finalDest)) {
						parent.sendQueue.put(gp);
					} else {
						// need to find address
						parent.resolveQueue.put(gp);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
