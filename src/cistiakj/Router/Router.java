package cistiakj.Router;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cistiakj.Constants;
import cistiakj.FlowTable.FlowTable;
import cistiakj.FlowTable.RouterFlowTableEntry;
import cistiakj.Packets.GenericPacket;
import cistiakj.Packets.OFPacket;
import cistiakj.Packets.PacketTypes;

import tcdIO.Terminal; // for debugging purpose

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 * OpenFlow router that can route packets of type GenericPacket
 */
public class Router implements Runnable, Constants, PacketTypes {
	//TODO: chec this statement
	// all entries have src as the current router. However, routing based on src can
	// also be implemented
	FlowTable<RouterFlowTableEntry> flowTable;
	HashMap<Integer, Interface> interfaces;
	DatagramSocket socket;
	int srcPort;
	InetSocketAddress controllerAddress;
	int protocolVersion;
	int routerId;

	BlockingQueue<GenericPacket> sendQueue;
	BlockingQueue<GenericPacket> resolveQueue;
	BlockingQueue<OFPacket> controllerQueue;

	public static void main(String[] args) {
		try {
			Router r = new Router(55004, new InetSocketAddress(InetAddress.getLocalHost(), 55010),
					new ArrayList<Interface>());
			r.run();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public Router(int srcPort,InetSocketAddress controllerAddress, ArrayList<Interface> interfaces) throws SocketException {
		this.srcPort = srcPort;
		this.controllerAddress= controllerAddress;
		this.protocolVersion = OF_VERSION;
		this.sendQueue = new LinkedBlockingQueue<GenericPacket>();
		this.resolveQueue = new LinkedBlockingQueue<GenericPacket>();
		this.controllerQueue = new LinkedBlockingQueue<OFPacket>();
		this.flowTable = new FlowTable<RouterFlowTableEntry>();
		this.socket = new DatagramSocket(this.srcPort);
		//important!!!
		this.routerId = srcPort;
		this.interfaces = new HashMap<>();
		for(Interface inface: interfaces) {
			this.interfaces.put(inface.getId(), inface);
			flowTable.addEntry(inface.getPort(), srcPort,
					new RouterFlowTableEntry(inface.getPort(), 0, inface.getId()));
		}
		
		//add default entry to flow table for controller
		flowTable.addEntry(controllerAddress.getPort(), srcPort, new RouterFlowTableEntry(controllerAddress.getPort(), -1, -1));
		this.interfaces.put(-1, new Interface(-1, controllerAddress.getPort(), Integer.MAX_VALUE));
	}

	public void run() {
		// 1) connect to controller
		ControllingThread ct = new ControllingThread(this);
		new Thread(ct).start();
		// 2) listen for incoming traffic
		ListeningThread lt = new ListeningThread(this);
		new Thread(lt).start();

		SendingThread st = new SendingThread(this);
		new Thread(st).start();

	}

	void addEntry(int destPort, int inId, int outId) {
		flowTable.addEntry(destPort, srcPort, new RouterFlowTableEntry(destPort, inId, outId));
	}

	void addEntry(RouterFlowTableEntry entry) {
		addEntry(entry.getDest(), entry.getInInterfaceId(), entry.getOutInterfaceId());
	}

	public boolean hasRoute(int destPort) {
		return flowTable.getEntry(destPort, srcPort) != null;
	}

	public int getNextHop(int destPort) {
		RouterFlowTableEntry entry = flowTable.getEntry(destPort, srcPort);
		assert(entry != null);
		if(entry == null) {
			System.err.println("Error");
		}
		return entry.getOutInterfaceId();
	}
}
