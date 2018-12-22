package cistiakj.EndPoint;

import java.io.IOException;
import java.net.DatagramPacket;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *
 */
public class EndPointOutputThread implements Runnable, Constants {

	EndPoint endPoint;

	public EndPointOutputThread(EndPoint endPoint) {
		super();
		this.endPoint = endPoint;
	}

	@Override
	public void run() {
		DatagramPacket dp = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);
		DatagramPacket payload = null;
		GenericPacket gp = null;
		for(;;) {
			try {
				endPoint.socket.receive(dp);
				gp = GenericPacket.fromDatagramPacket(dp);
				if(gp == null ) {
					continue;
				}
				payload = gp.getPayload();
				String message = new String(payload.getData(), 0, payload.getLength());
				endPoint.out.print(String.format("From: %s%n Message: %s%n", gp.getFinalDest(), message));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
