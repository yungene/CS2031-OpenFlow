package cistiakj.EndPoint;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import cistiakj.Constants;
import tcdIO.Terminal;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *	A class to represent an End Node. EndPoint can send messages addressed to another endPoint
 *	and Receive message.
 */
public class EndPoint implements Runnable, Constants{

	Terminal in;
	Terminal out;
	public DatagramSocket socket;
	InetSocketAddress defaultGatewayAddress;
	
	public EndPoint(int srcPort, int routerPort) throws UnknownHostException, SocketException {
		
		this.in = new Terminal(String.format("%d - Input terminal",srcPort));
		this.out = new Terminal(String.format("%d - Input terminal",srcPort));
		this.defaultGatewayAddress = new InetSocketAddress(InetAddress.getLocalHost(), routerPort);
		this.socket = new DatagramSocket(srcPort);
	}
	
	public static void main(String[] args) {
		try {
			EndPoint ep = new EndPoint(50001, 50000);
			ep.run();
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		EndPointInputThread inThread = new EndPointInputThread(this);
		EndPointOutputThread outThread = new EndPointOutputThread(this);
		
		new Thread(inThread).start();
		new Thread(outThread).start();
	}
	
	void send(int port, String message) {
		
	}
	
}
