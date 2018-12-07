package cistiakj.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *
 */
public class PacketOutPacket extends OFPacket {
	public GenericPacket packet;

	public PacketOutPacket(int version, int connectionId, int seq, GenericPacket packet) {
		super(OPF_TYPE.OFPT_SET_CONFIG, version, connectionId, seq);
		this.packet = packet;
	}

	public PacketOutPacket(ObjectInputStream in) throws ClassNotFoundException, IOException {
		super(in);
		this.packet = (GenericPacket) in.readObject();
	}

	@Override
	public void toObjectOutputStream(ObjectOutputStream out) throws IOException {
		out.writeObject(packet);
	}
}
