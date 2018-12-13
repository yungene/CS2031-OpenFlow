package cistiakj.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cistiakj.FlowTable.FlowTable;
import cistiakj.FlowTable.RouterFlowTableEntry;
import cistiakj.Packets.PacketTypes.OPF_TYPE;

public class GetConfigReplyPacket extends OFPacket{
	FlowTable<RouterFlowTableEntry> flowTable;
	public GetConfigReplyPacket(int version, int connectionId, int seq,FlowTable<RouterFlowTableEntry> flowTable) {
		super(OPF_TYPE.OFPT_GET_CONFIG_REPLY, version, connectionId,seq);
		this.flowTable = flowTable;
	}
	public GetConfigReplyPacket(ObjectInputStream in) throws ClassNotFoundException, IOException {
		super(in);
		type = OPF_TYPE.OFPT_GET_CONFIG_REPLY;
		this.flowTable = (FlowTable<RouterFlowTableEntry>) in.readObject();
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) throws IOException {
		out.writeObject(flowTable);
	}
}
