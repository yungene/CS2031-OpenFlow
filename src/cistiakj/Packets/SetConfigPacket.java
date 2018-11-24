package cistiakj.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cistiakj.FlowTable.RouterFlowTableEntry;

public class SetConfigPacket extends OFPacket{
	RouterFlowTableEntry[] entries;
	public SetConfigPacket(int version, int connectionId, int seq,RouterFlowTableEntry[] entries) {
		super(OPF_TYPE.OFPT_SET_CONFIG, version, connectionId,seq);
		this.entries = entries;
	}
	public SetConfigPacket(ObjectInputStream in) throws ClassNotFoundException, IOException {
		super(in);
		this.entries = (RouterFlowTableEntry[])in.readObject();
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) throws IOException {
		out.writeObject(entries);
	}
}
