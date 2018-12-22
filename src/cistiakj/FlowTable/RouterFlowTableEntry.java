package cistiakj.FlowTable;

import java.io.Serializable;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *
 */
public class RouterFlowTableEntry implements Serializable{

	private int dest;
	private int inInterfaceId;
	private int outInterfaceId;
	

	public RouterFlowTableEntry(int dest, int inInterfaceId, int outInterfaceId) {
		super();
		this.dest = dest;
		this.inInterfaceId = inInterfaceId;
		this.outInterfaceId = outInterfaceId;
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
