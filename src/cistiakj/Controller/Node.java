package cistiakj.Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.BlockingQueue;

import cistiakj.Constants;
import cistiakj.Packets.OFPacket;

public abstract class Node implements Constants {
	
	public abstract void process(OFPacket pk);
	
	BlockingQueue<OFPacket> packetQueue;
	Listener listener;
	DatagramSocket socket;
	
	private class Listener implements Runnable {

		@Override
		public void run() {
			try {
				DatagramPacket pk = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
				socket.receive(pk);
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			
		}

	}
}

