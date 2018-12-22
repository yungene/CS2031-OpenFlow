package cistiakj.FlowTable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *	Entry to a controller's flow table
 */
public class FlowTableEntry {
	
	ArrayList<ControllerFlowTableEntry> entries;
	//private HashMap<Integer, RouterFlowTableEntry> route;

	public FlowTableEntry() {
		super();
		this.entries = new ArrayList<>();
	}

	public void addEntry(int routerId,int dest, int inInterfaceId, int outInterfaceId) {
		entries.add(new ControllerFlowTableEntry(routerId,dest, inInterfaceId,outInterfaceId));
	}
	
	public ArrayList<ControllerFlowTableEntry> getEntries(){
		return entries;
	}

}

