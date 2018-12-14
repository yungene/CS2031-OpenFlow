package cistiakj.Packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * 
 * @author Jevgenijus Cistiakovas cistiakj@tcd.ie
 *
 * A wrapper class for all the packet on the network, since the nextwork is emulated and hence MAC addresses are not
 * used. This class is needed to be able to record both the final address and the address of the next hop.
 */
public class GenericPacket implements Serializable{
	
	//port number of the final destination
	private int finalDest;
	private InetSocketAddress finalAddr;
	private DatagramPacket payload;
	private byte[] data;
	private int length;
	private int offset;
	
	
	public GenericPacket(DatagramPacket packet) {
		payload = packet;
		this.data = payload.getData();
		this.length = payload.getLength();
		this.offset = payload.getOffset();
		this.finalAddr = new InetSocketAddress(packet.getAddress(), packet.getPort());
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getFinalDest() {
		//return finalDest;
		return finalAddr.getPort();
	}

	public void setFinalDest(int finalDest) {
		this.finalDest = finalDest;
	}
	public InetSocketAddress getFinalAddr() {
		return finalAddr;
	}

	public void setFinalAddr(InetSocketAddress finalAddr) {
		this.finalAddr = finalAddr;
	}

	public static GenericPacket fromDatagramPacket(DatagramPacket packet) {
		GenericPacket gp = null;
		if(packet!=null) {
			try {
				ByteArrayInputStream bin;
				ObjectInputStream oin;
				byte[] data;
				data = packet.getData();
				bin = new ByteArrayInputStream(data);
				oin = new ObjectInputStream(bin);
				//int finalDest = oin.readInt();
				//InetSocketAddress finalAddr= (InetSocketAddress) oin.readObject();
				int port = oin.readInt();
				byte[] addr = (byte[])oin.readObject();
				InetSocketAddress finalAddr = new InetSocketAddress(InetAddress.getByAddress(addr), port);
				byte[] payload = (byte[])oin.readObject();
				int length = oin.readInt();
				int offset = oin.readInt();
				DatagramPacket pk = new DatagramPacket(payload, offset, length, finalAddr.getAddress(),finalAddr.getPort());
				gp = new GenericPacket(pk);
				//gp.setFinalDest(finalDest);
				gp.setFinalAddr(finalAddr);
			} catch(Exception e) {
				System.err.println("Failed to convert to Generic Packet");
				return null;
			}
		}
		return gp;
	}
	
	public DatagramPacket getPayload() {
		return payload;
	}

	public void setPayload(DatagramPacket payload) {
		this.payload = payload;
		this.data = payload.getData();
		this.length = payload.getLength();
		this.offset = payload.getOffset();
		this.finalAddr = new InetSocketAddress(payload.getAddress(), payload.getPort());
	}

	public DatagramPacket toDatagramPacket() {
		DatagramPacket packet = null;
		try {
			ByteArrayOutputStream bout;
			ObjectOutputStream oout;
			byte[] data;
			bout = new ByteArrayOutputStream();
			oout = new ObjectOutputStream(bout);
			//oout.writeUTF("addr");
			//oout.writeObject(finalAddr);
			oout.writeInt(finalAddr.getPort());
			oout.writeObject(finalAddr.getAddress().getAddress());
			//oout.writeUTF("data");
			oout.writeObject(this.data);
			//oout.writeUTF("len");
			oout.writeInt(length);
			//oout.writeUTF("offset");
			oout.writeInt(offset);
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
}
