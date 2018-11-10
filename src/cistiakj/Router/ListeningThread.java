package cistiakj.Router;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.PacketTypes;

public class ListeningThread implements Runnable, Constants, PacketTypes{
	
	Router parent;
	DatagramSocket socket;
	public ListeningThread(Router parent) {
		super();
		this.parent = parent;
		socket = parent.socket;
	}

	@Override
	public void run() {
		for(;;) {
			try {
				DatagramPacket pk = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
				socket.receive(pk);
				GenericPacket gp = GenericPacket.fromDatagramPacket(pk);
				if(gp == null) {
					continue;
				}
				int finalDest = gp.getFinalDest();
				if(finalDest == parent.srcPort) {	// from the controller
					parent.controllerQueue.put(gp);
				}else {
					// not inteded for the router => need to forward
					if(parent.hasRoute(finalDest)) {
						parent.sendQueue.put(gp);
					}else {
						//need to find address
						parent.resolveQueue.put(gp);
					}
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
