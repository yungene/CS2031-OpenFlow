package cistiakj.Controller;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cistiakj.Constants;
import cistiakj.Graph;
import cistiakj.FlowTable.FlowTable;
import cistiakj.FlowTable.FlowTableEntry;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.OFPacket;
import cistiakj.Router.Interface;

import tcdIO.Terminal; // for debugging purpose


/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 * OpenFlow controller
 */
public class Controller implements Runnable, Constants {

	// (routrId) -> address of router
	HashMap<Integer, InetSocketAddress> routers;

	BlockingQueue<GenericPacket> sendQueue;
	BlockingQueue<OFPacket> processQueue;
	// graph of all the routers managed by this controllers
	Graph<Integer> network;
	FlowTable<FlowTableEntry> flowTable;
	// (source router address) -> ((next router address) -> (source router out
	// interface))
	HashMap<Integer, HashMap<Integer, Integer>> routerInterfaceIdMap;
	DatagramSocket socket;
	int srcPort;
	int protocolVersion;
	int controllerId;
	boolean tableChanged;

	public static void main(String[] args) {

		Controller controller;
		try {
			controller = new Controller(CONTROLLER_DEFAULT_PORT);
			controller.run();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public Controller(int srcPort) throws SocketException {
		this.sendQueue = new LinkedBlockingQueue<>();
		this.processQueue = new LinkedBlockingQueue<>();
		this.network = new Graph<Integer>();
		this.flowTable = new FlowTable<FlowTableEntry>();
		this.routerInterfaceIdMap = new HashMap<>();
		this.srcPort = srcPort;
		this.socket = new DatagramSocket(srcPort);
		this.protocolVersion = PROTOCOL_VERSION;
		this.controllerId = (int) (50000 * Math.random());
		this.routers = new HashMap<>();
	}

	public void run() {
		ControllerListeningThread clt = new ControllerListeningThread(this);
		new Thread(clt).start();
		ControllerProcessingThread cpt = new ControllerProcessingThread(this);
		new Thread(cpt).start();
		ControllerSendingThread cst = new ControllerSendingThread(this);
		new Thread(cst).start();
	}

	// TODO: possibility of updating info
	void buildNetwork(int nodeId, Interface[] adj) {
		tableChanged = true;
		if (!routerInterfaceIdMap.containsKey(nodeId)) {
			routerInterfaceIdMap.put(nodeId, new HashMap<>());
		}
		HashMap<Integer, Integer> map = routerInterfaceIdMap.get(nodeId);
		// create a loopback
		map.put(nodeId, nodeId);

		for (Interface i : adj) {
			if (i.getPort() != this.srcPort) {
				map.put(i.getPort(), i.getId());
				this.network.addEdge(nodeId, i.getPort(), i.getMetric());
				//this.network.addUndirectedEdge(nodeId, i.getPort(), i.getMetric());
			}
		}
		// TODO: check for efficiency
		// buildFlowTable();
	}

	void buildFlowTable() {
		// run dijkstra for each node? similar to Johnson's algo
		// dijkstra will return a hashmap with all the parents
		Set<Integer> routers = network.V();
		for (Integer start : routers) {
			if(start.compareTo(50010) > 0) {
				//it is an endpoint
				continue;
			}
			HashMap<Integer, Integer> parents = network.dijkstraParents(start);
			for (Integer fin : parents.keySet()) {
				if(fin.equals(start)) {
					continue;
				}
				FlowTableEntry path = new FlowTableEntry();
				//flowTable.addEntry(fin, start, path);
				Integer next = fin;
				Integer curr = parents.get(next);
				Integer prev = parents.get(curr);
				// Integer v = parents.get(p);
				// reconstruct the path and put it into flow table
				while (!next.equals(start)) {
					// TODO: create a loop back interface from router to itself, so that flow table
					// works
					path.addEntry(curr, next, routerInterfaceIdMap.get(curr).get(prev),
							routerInterfaceIdMap.get(curr).get(next));
					next = curr;
					curr = prev;
					prev = parents.get(prev);
				}
//				if (curr == prev && prev != null && next != null) {
//					//System.out.printf("cu");
//					path.addEntry(curr, next, routerInterfaceIdMap.get(curr).get(prev),
//							routerInterfaceIdMap.get(curr).get(next));
//				}
				//just to be sure hashing is done correctly
				flowTable.addEntry(fin, start, path);

			}
		}
		tableChanged = false;

	}

}
