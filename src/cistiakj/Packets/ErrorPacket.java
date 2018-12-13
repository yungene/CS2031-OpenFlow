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
public class ErrorPacket extends OFPacket{
	int errorType;
	public ErrorPacket(int version, int connectionId, int seq, int errorType) {
		super(OPF_TYPE.OFPT_ERROR, version, connectionId,seq);
		this.errorType = errorType;
	}
	public ErrorPacket(ObjectInputStream in) {
		super(in);
		type = OPF_TYPE.OFPT_ERROR;
		try {
			this.errorType = in.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) {
		try {
			out.writeInt(errorType);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
