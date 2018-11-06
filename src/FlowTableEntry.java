
public class FlowTableEntry {
	
	private int inInterfaceId;
	private int outInterfaceId;
	private int dest;
	private int src;
	private int routerId;

	public FlowTableEntry(int inInterfaceId, int outInterfaceId, int dest, int src, int routerId) {
		super();
		this.inInterfaceId = inInterfaceId;
		this.outInterfaceId = outInterfaceId;
		this.dest = dest;
		this.src = src;
		this.routerId = routerId;
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
	public int getDest() {
		return dest;
	}
	public void setDest(int dest) {
		this.dest = dest;
	}
	public int getSrc() {
		return src;
	}
	public void setSrc(int src) {
		this.src = src;
	}
	public int getRouterId() {
		return routerId;
	}
	public void setRouterId(int routerId) {
		this.routerId = routerId;
	}

}
