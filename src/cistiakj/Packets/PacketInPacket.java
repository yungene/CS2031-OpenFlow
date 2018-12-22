package cistiakj.Packets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import cistiakj.Packets.PacketTypes.OPF_TYPE;

/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *	A reply to PacketOutPacket. It must be sent after all the routers along the path were setup.
 */
public class PacketInPacket extends OFPacket{
	//public GenericPacket packet;
	public int srcPort;
	public int destPort;
	public byte[] data;	// data of datagram apcker
	public int length;
	public int offset;
	
	public PacketInPacket(int version, int connectionId, int seq, GenericPacket packet, int srcPort) {
		super(OPF_TYPE.OFPT_PACKET_IN, version, connectionId, seq);
		this.srcPort = srcPort;
		this.destPort = packet.getFinalDest();
		this.data = packet.getData();
		this.length = packet.getLength();
		this.offset = packet.getOffset();
	}
	
	public PacketInPacket(int version, int connectionId, int seq,PacketOutPacket pop) {
		super(OPF_TYPE.OFPT_PACKET_IN, version, connectionId, seq);
		this.srcPort = pop.srcPort;
		this.destPort = pop.destPort;
		this.data = pop.data;
		this.length = pop.length;
		this.offset = pop.offset;
	}


	public PacketInPacket(ObjectInputStream in) throws ClassNotFoundException, IOException {
		super(in);
		type = OPF_TYPE.OFPT_PACKET_IN;
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
	
	public GenericPacket toGenericPacket() throws IOException {
		DatagramPacket dp = new DatagramPacket(data, length, InetAddress.getLocalHost(), destPort);
		GenericPacket gp = new GenericPacket(dp);
		return gp;
	}
}
