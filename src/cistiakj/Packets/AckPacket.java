package cistiakj.Packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AckPacket extends OFPacket{
	
	public AckPacket(int version, int connectionId, int seq) {
		super(OPF_TYPE.OFPT_ACK, version, connectionId,seq);
	}
	public AckPacket(ObjectInputStream in) {
		super(in);
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) {
		//dumb method
	}

}
