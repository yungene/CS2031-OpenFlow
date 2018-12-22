package cistiakj.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cistiakj.FlowTable.RouterFlowTableEntry;
import cistiakj.Packets.PacketTypes.OPF_TYPE;

public class SetConfigPacket extends OFPacket{
	public RouterFlowTableEntry[] entries;
	public SetConfigPacket(int version, int connectionId, int seq,RouterFlowTableEntry[] entries) {
		super(OPF_TYPE.OFPT_SET_CONFIG, version, connectionId,seq);
		this.entries = entries;
	}
	public SetConfigPacket(ObjectInputStream in) throws ClassNotFoundException, IOException {
		super(in);
		type = OPF_TYPE.OFPT_SET_CONFIG;
		this.entries = (RouterFlowTableEntry[])in.readObject();
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) throws IOException {
		out.writeObject(entries);
	}
}
