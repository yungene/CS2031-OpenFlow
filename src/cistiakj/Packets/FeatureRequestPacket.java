package cistiakj.Packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cistiakj.Packets.PacketTypes.OPF_TYPE;
/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *
 */
public class FeatureRequestPacket extends OFPacket{

	public FeatureRequestPacket(int version, int connectionId, int seq) {
		super(OPF_TYPE.OFPT_FEATURES_REQUEST, version, connectionId,seq);
	}
	public FeatureRequestPacket(ObjectInputStream in) {
		super(in);
		type = OPF_TYPE.OFPT_FEATURES_REQUEST;
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) {
		//dumb method
	}
}
