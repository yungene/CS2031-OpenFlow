package cistiakj.FlowTable;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *
 */
public class ControllerFlowTableEntry{
	int routerId;

	int dest;
	int inInterfaceId;
	int outInterfaceId;
	
	public ControllerFlowTableEntry(int routerId,int dest, int inInterfaceId, int outInterfaceId) {
		super();
		this.routerId = routerId;
		this.dest = dest;
		this.inInterfaceId = inInterfaceId;
		this.outInterfaceId = outInterfaceId;
	}
	
	
	public int getRouterId() {
		return routerId;
	}

	public void setRouterId(int routerId) {
		this.routerId = routerId;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public int getInInterfaceId() {
		return inInterfaceId;
	}

	public void setInInterfaceId(int inInterfaceId) {
		this.inInterfaceId = inInterfaceId;
	}

	public int getOutInterfaceId() {
		return outInterfaceId;
	}

	public void setOutInterfaceId(int outInterfaceId) {
		this.outInterfaceId = outInterfaceId;
	}
}