package cistiakj.Packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

import cistiakj.Constants;
import cistiakj.Packets.PacketTypes.OPF_TYPE;

public abstract class OFPacket implements Constants, PacketTypes{
	public static final int SUPPORTED_VERSION = 1;
	OPF_TYPE type;
	int version;
	int connectionId;
	int seq;
	public OFPacket(OPF_TYPE type, int version, int connectionId, int seq){
		this.type = type;
		this.version = version;
		this.connectionId = connectionId;
		this.seq = seq;
	}
	
	public OFPacket(ObjectInputStream in){
		version = SUPPORTED_VERSION;
		try {
			connectionId = in.readInt();
			seq = in.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int compareTo(OFPacket that) {
		return Integer.compare(this.seq, that.seq);
	}
	
	public OFPacket() {
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public void setOPF_TYPE(OPF_TYPE type) {
		this.type = type;
	}

	public static OFPacket fromDatagramPacket(DatagramPacket packet) {
		OFPacket content = null;
		if (packet != null) {
			try {
				ByteArrayInputStream bin;
				ObjectInputStream oin;
				byte[] data;
				OPF_TYPE type;
				data = packet.getData();
				bin = new ByteArrayInputStream(data);
				oin = new ObjectInputStream(bin);
				String name = oin.readUTF();
				if(!name.equals("SPB")) {
					return null;
				}
				type = OPF_TYPE.values()[oin.readInt()]; // read in the type of the
													// packet
				int version = oin.readInt();
				if (version == SUPPORTED_VERSION) {
					switch (type) {
					default:
						break;
					}
				}
				oin.close();
				bin.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return content;
	}

	public DatagramPacket toDatagramPacket() {
		DatagramPacket packet = null;
		try {
			ByteArrayOutputStream bout;
			ObjectOutputStream oout;
			byte[] data;
			bout = new ByteArrayOutputStream();
			oout = new ObjectOutputStream(bout);
			oout.writeUTF("SPB");
			oout.writeInt(type.ordinal());
			oout.writeInt(version);
			oout.writeInt(connectionId);
			oout.writeInt(seq);
			toObjectOutputStream(oout);
			oout.writeUTF(type.toString());
			oout.flush();
			data = bout.toByteArray();
			packet = new DatagramPacket(data, data.length);

			oout.close();
			bout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return packet;
	}

	public abstract void toObjectOutputStream(ObjectOutputStream out);

	// public toString();
	public String toString() {
		return String.format("SPB : OPF_TYPE %s, version: %d, connectionId: %d, seqNo: %d %n",
				type.toString(),version,connectionId,seq);
	}

	public OPF_TYPE getType() {
		return type;
	}

}
