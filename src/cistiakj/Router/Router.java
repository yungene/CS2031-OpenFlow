package cistiakj.Router;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
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

/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *
 */
public class Router implements Constants, PacketTypes {
	
	// all entries have src as the current router. However, routing based on src can also be implemented
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
	
	
	public Router(int srcPort, ArrayList<Interface> interfaces) {
		this.srcPort = srcPort;
		this.protocolVersion = OF_VERSION;
		this.routerId = srcPort;
		for(int i = 0 ; i < interfaces.size();i++) {
			this.interfaces.put(i, interfaces.get(i));
		}	
		this.sendQueue = new LinkedBlockingQueue<GenericPacket>();
		this.resolveQueue = new LinkedBlockingQueue<GenericPacket>();
		this.controllerQueue = new LinkedBlockingQueue<OFPacket>();
		this.flowTable = new FlowTable<RouterFlowTableEntry>();
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
		addEntry(entry.getDest(),entry.getInInterfaceId(),entry.getOutInterfaceId());
	}
	
	public boolean hasRoute(int destPort) {
		return flowTable.getEntry(destPort, srcPort) != null;
	}
	
	public int getNextHop(int destPort) {
		RouterFlowTableEntry entry = flowTable.getEntry(destPort, srcPort);
		return entry.getOutInterfaceId();
	}
}
