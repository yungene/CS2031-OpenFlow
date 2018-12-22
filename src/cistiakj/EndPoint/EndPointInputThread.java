package cistiakj.EndPoint;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import cistiakj.Constants;
import cistiakj.Packets.GenericPacket;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *
 */
public class EndPointInputThread implements Runnable, Constants {

	EndPoint endPoint;

	public EndPointInputThread(EndPoint endPoint) {
		super();
		this.endPoint = endPoint;
	}

	@Override
	public void run() {
		String message = "";
		int dest = 0;
		GenericPacket gp = null;
		byte[] buffer;
		endPoint.in.println("Delivery of the messages is not guarateed.");
		for (;;) {
			try {
				endPoint.in.println("Enter destination address as integer(i.e. port number): ");
				dest = endPoint.in.readInt();
				endPoint.in.println("Enter message to be sent: ");
				message = endPoint.in.readString();

				// pack a message and send
				buffer = message.getBytes();
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				dp.setSocketAddress(new InetSocketAddress(InetAddress.getLocalHost(), dest));
				gp = new GenericPacket(dp);
				dp = gp.toDatagramPacket();
				dp.setSocketAddress(endPoint.defaultGatewayAddress);
				endPoint.socket.send(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
