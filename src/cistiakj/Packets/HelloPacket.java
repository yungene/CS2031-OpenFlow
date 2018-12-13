package cistiakj.Packets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *
 */
public class HelloPacket extends OFPacket{
	public HelloPacket(int version, int connectionId, int seq) {
		super(OPF_TYPE.OFPT_HELLO, version, connectionId,seq);
	}
	public HelloPacket(ObjectInputStream in) {
		super(in);
		type = OPF_TYPE.OFPT_HELLO;
	}
	@Override
	public void toObjectOutputStream(ObjectOutputStream out) {
		//dumb method
	}
}
