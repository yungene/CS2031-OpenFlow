package cistiakj.Controller;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cistiakj.Graph;
import cistiakj.FlowTable.FlowTable;
import cistiakj.FlowTable.FlowTableEntry;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.OFPacket;
import cistiakj.Router.Interface;

public class Controller{

	// (routrId) -> address of router
	HashMap<Integer, InetSocketAddress> routers;
	BlockingQueue<GenericPacket> sendQueue;
	BlockingQueue<OFPacket> processQueue;
	// graph of all the routers managed by this controllers
	Graph<Integer> network;
	FlowTable<FlowTableEntry> flowTable;
	DatagramSocket socket;
	int srcPort;
	int protocolVersion;
	int controllerId;
	
	public static void main(String[] args) {
		
		Controller controller = new Controller();
	}
	
	public Controller() {
		this.sendQueue = new LinkedBlockingQueue<>();
		this.processQueue = new LinkedBlockingQueue<>();
		this.network = new Graph<Integer>();
		this.flowTable = new FlowTable<FlowTableEntry>();
	}
	
	public void run() {
		ControllerListeningThread clt = new ControllerListeningThread(this);
		new Thread(clt).start();
	}
	
	void buildNetwork(int nodeId, Interface[] adj) {
		for(Interface i: adj) {
			this.network.addEdge(nodeId, i.getPort(), i.getMetric());
		}
	}
	
	void buildFlowTable() {
		//run djikstra for each node? similar to Johnson's algo
	}

}
