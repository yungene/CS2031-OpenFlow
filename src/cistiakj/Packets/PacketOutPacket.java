package cistiakj.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cistiakj.Packets.PacketTypes.OPF_TYPE;

/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *	Packet is sent by router to controller to request help with routing
 */
public class PacketOutPacket extends OFPacket {
	//carrying packet is not feasible
	// carry only destination
	//public GenericPacket packet;
	public int srcPort;
	public int destPort;
	public byte[] data;	// data of datagram apcker
	public int length;
	public int offset;

	public PacketOutPacket(int version, int connectionId, int seq, GenericPacket packet, int srcPort) {
		super(OPF_TYPE.OFPT_PACKET_OUT, version, connectionId, seq);
		this.srcPort = srcPort;
		this.destPort = packet.getFinalDest();
		this.data = packet.getData();
		this.length = packet.getLength();
		this.offset = packet.getOffset();
	}
	
	public PacketOutPacket(int version, int connectionId, int seq,PacketInPacket pip) {
		super(OPF_TYPE.OFPT_PACKET_OUT, version, connectionId, seq);
		this.srcPort = pip.srcPort;
		this.destPort = pip.destPort;
		this.data = pip.data;
		this.length = pip.length;
		this.offset = pip.offset;
	}

	public PacketOutPacket(ObjectInputStream in) throws ClassNotFoundException, IOException {
		super(in);
		type = OPF_TYPE.OFPT_PACKET_OUT;
		//this.packet = (GenericPacket) in.readObject();
		this.srcPort = in.readInt();
		this.destPort = in.readInt();
		this.data = (byte[]) in.readObject();
		this.length = in.readInt();
		this.offset = in.readInt();
	}

	@Override
	public void toObjectOutputStream(ObjectOutputStream out) throws IOException {
		//out.writeObject(packet);
		out.writeInt(srcPort);
		out.writeInt(destPort);
		out.writeObject(data);
		out.writeInt(length);
		out.writeInt(offset);
	}
}
