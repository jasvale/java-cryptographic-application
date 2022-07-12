package com.commons.channel;

import com.commons.enums.EndPoint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Channel implements IChannel {
	protected Socket socket;
	protected int sequenceNumber = 0;
	protected EndPoint endPoint;

	public Channel(Socket socket, EndPoint endPoint) {
		this.socket = socket;
		this.endPoint = endPoint;
	}

	public Channel(ChannelContext channelContext) {
		this.socket = channelContext.socket();
		this.endPoint = channelContext.endPoint();
	}

	protected void flushOutMessageBytes(DataOutputStream out, byte[] bytes) throws IOException {
		out.writeInt(bytes.length);
		out.write(bytes);
		out.flush();
	}

	protected byte[] receiveMessageBytes(DataInputStream in) throws IOException {
		int len = in.readInt();
		if(len == 0)
			return new byte[0];
		byte[] received = new byte[len];
		for(int i = 0; i < len; i++) {
			received[i] = in.readByte();
		}
		return received;
	}

	protected DataOutputStream getDataOutputStream() throws IOException {
		DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
		this.sequenceNumber++;
		out.writeInt(this.sequenceNumber);
		return out;
	}

	protected DataInputStream getDataInputStream() throws Exception {
		DataInputStream in = new DataInputStream(this.socket.getInputStream());
		this.sequenceNumber++;
		int receivedSequenceNumber = in.readInt();
		if(receivedSequenceNumber != this.sequenceNumber) {
			throw new Exception("Sequence numbers didn't match " + receivedSequenceNumber + " <> " + sequenceNumber);
		}
		return in;
	}
	
	public String getRemoteIP() {
		return socket.getRemoteSocketAddress().toString();
	}

	public void close() throws IOException {
		this.socket.close();
	}

	@Override
	public EndPoint getRemoteEndPoint() {
		return this.endPoint;
	}
}
