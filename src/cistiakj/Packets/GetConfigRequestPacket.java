package cistiakj.Packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GetConfigRequestPacket extends OFPacket{
	public GetConfigRequestPacket(int version, int connectionId, int seq) {
		super(OPF_TYPE.OFPT_GET_CONFIG_REQUEST, version, connectionId,seq);
	}
	public GetConfigRequestPacket(ObjectInputStream in) {
		super(in);
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) {
		//dumb method
	}
}
