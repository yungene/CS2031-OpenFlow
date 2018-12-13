package cistiakj.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cistiakj.Packets.PacketTypes.OPF_TYPE;
/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *
 */
public class EchoReplyPacket extends OFPacket{
	int option;
	public EchoReplyPacket(int version, int connectionId, int seq, int option) {
		super(OPF_TYPE.OFPT_ECHO_REPLY, version, connectionId,seq);
		this.option = option;
	}
	public EchoReplyPacket(ObjectInputStream in) {
		super(in);
		type = OPF_TYPE.OFPT_ECHO_REPLY;
		try {
			this.option = in.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) {
		try {
			out.writeInt(option);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
