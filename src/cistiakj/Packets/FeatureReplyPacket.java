package cistiakj.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cistiakj.Packets.PacketTypes.OPF_TYPE;
import cistiakj.Router.Interface;

/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *	A reply to FeatureRequest. It is sent by router to controller. It includes all the 
 *	information about router's neighbours.
 */
public class FeatureReplyPacket extends OFPacket{
	public Interface[] entries;
	public FeatureReplyPacket(int version, int connectionId, int seq,Interface[] entries) {
		super(OPF_TYPE.OFPT_FEATURES_REPLY, version, connectionId,seq);
		this.entries = entries;
	}
	public FeatureReplyPacket(ObjectInputStream in) throws ClassNotFoundException, IOException {
		super(in);
		type = OPF_TYPE.OFPT_FEATURES_REPLY;
		this.entries = (Interface[])in.readObject();
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) throws IOException {
		out.writeObject(entries);
	}
}
