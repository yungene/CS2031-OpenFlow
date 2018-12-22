package cistiakj.Packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cistiakj.Packets.PacketTypes.OPF_TYPE;

public class GetConfigRequestPacket extends OFPacket{
	public GetConfigRequestPacket(int version, int connectionId, int seq) {
		super(OPF_TYPE.OFPT_GET_CONFIG_REQUEST, version, connectionId,seq);
	}
	public GetConfigRequestPacket(ObjectInputStream in) {
		super(in);
		type = OPF_TYPE.OFPT_GET_CONFIG_REQUEST;
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) {
		//dumb method
	}
}
