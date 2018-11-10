package cistiakj.Router;

import java.net.InetSocketAddress;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.PacketTypes;

public class ControllingThread implements Runnable, Constants, PacketTypes {

	
	Router parent;
	
	public ControllingThread(Router parent) {
		super();
		this.parent = parent;
	}

	@Override
	public void run() {
		connect();
		for(;;) {
			try {
				GenericPacket gp = parent.resolveQueue.take();
				
				for(;;) {
					// communicate with controller to get a path
					findPath(gp.getFinalAddr());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void findPath(InetSocketAddress finalDest) {
		// do something :)
	}
	
	
	private void connect() {
		for(;;) {
			//TODO: fill in details
		}
	}

}
