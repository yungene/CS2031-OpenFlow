package cistiakj.Router;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *
 * An abstraction to represent an interface of a node =  direct link between the node and its neightbour
 */

public class Interface implements Serializable{
	private int port; //port of a neighbour
	private int metric;	// weight of an edge in a graph
	private int id;
	public Interface( int id,int port, int metric) {
		super();
		this.port = port;
		this.metric = metric;
		this.id = id;
	}

	public void send(DatagramSocket socket, DatagramPacket pk) {
		try {
			pk.setPort(port);
			socket.send(pk);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getMetric() {
		return metric;
	}
	public void setMetric(int metric) {
		this.metric = metric;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
