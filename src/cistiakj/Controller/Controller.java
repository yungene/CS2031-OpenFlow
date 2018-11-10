package cistiakj.Controller;

import cistiakj.Packets.OFPacket;

public class Controller extends Node {

	
	public static void main(String[] args) {
		
		Controller controller = new Controller();
		new Thread(controller.listener).start();
	}
	
	
	@Override
	public void process(OFPacket pk) {
		// TODO Auto-generated method stub
		
	}

}
