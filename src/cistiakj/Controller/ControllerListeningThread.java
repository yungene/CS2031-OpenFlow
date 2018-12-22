package cistiakj.Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.OFPacket;
import cistiakj.Packets.PacketTypes;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *	A helper thread of Controller whose function is to listen for incoming packets and deal with them.
 */
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
					System.err.println("Controller got packet not for router");
					continue;
				}
				OFPacket ofpk = OFPacket.fromDatagramPacket(gp.getPayload());
				if (ofpk == null) {
					System.err.println("Controller got packet that is not OpenFlow");
					continue;
				}
				// insert an antry about each router into memory
				if (!controller.routers.containsKey(ofpk.getConnectionId())) {
					if (ofpk.getType() == OPF_TYPE.OFPT_HELLO) {
						//System.out.println(ofpk.getConnectionId());
						controller.routers.put(ofpk.getConnectionId(),
												new InetSocketAddress(pk.getAddress(), pk.getPort()));
					} else {
						// drop the packet
						System.err.println("New router and not HELLO message. Go away!");
						System.err.println("Message type is " + ofpk.getType().name());
						continue;
					}
				}
				controller.processQueue.add(ofpk);
				// maybe spawn a new thread instead
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
