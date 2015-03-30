package com.github.lycis.valkyrie.communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * This is the header of a message. It identifies meta data of a message
 * like the peer network it belongs to, the data length and the message
 * type.
 *  
 * @author lycis
 *
 */
public class ValkyrieMessageHeader {	
	private long id; // node internal id of a message
	private int messageType; // message type identifier
	private long dataLength; // length of the data block
	
	public ValkyrieMessageHeader() {
		
	}
	
	public ValkyrieMessageHeader(long id) {
		this.id = id;
	}
	
	public ValkyrieMessageHeader(long id, int messageType) {
		this(id);
		this.messageType = messageType;
	}
	
	public ValkyrieMessageHeader(long id, int messageType, long dataLength) {
		this(id, messageType);
		this.dataLength = dataLength;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Provides the internal ID of this message. This ID is only valid within
	 * one node of the peer network.
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the type of this message. When implementing your own custom
	 * messages keep in mind that you should only use positive numbers.
	 * Negative numbers are reserved for valkyrie system messages.
	 * 
	 * @param messageType
	 */
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	
	public int getMessageType() {
		return messageType;
	}
	
	public void setDataLength(long dataLength) {
		this.dataLength = dataLength;
	}
	
	public long getDataLength() {
		return dataLength;
	}
	
	/**
	 * Seralize the message header into an output stream. This is used
	 * when sending a message.
	 * 
	 * @return
	 */
	ByteArrayOutputStream toDataStream() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		stream.write(ByteBuffer.allocate(Long.BYTES).putLong(id).array());
		stream.write(ByteBuffer.allocate(Integer.BYTES).putInt(messageType).array());
		stream.write(ByteBuffer.allocate(Long.BYTES).putLong(dataLength).array());
		return stream;
	}
	
	/**
	 * Fills the message header based on an input stream of raw message data.
	 * This is used when reading a message.
	 * 
	 * @param data raw message data
	 * @throws IOException
	 */
	void fromDataStream(ByteArrayInputStream data) throws IOException {
		byte[] buffer;
		
		buffer = new byte[Long.BYTES];
		data.read(buffer);
		id = ByteBuffer.allocate(buffer.length).put(buffer).getLong();
		
		buffer = new byte[Integer.BYTES];
		data.read(buffer);
		messageType = ByteBuffer.allocate(buffer.length).put(buffer).getInt();
		
		buffer = new byte[Long.BYTES];
		data.read(buffer);
		dataLength = ByteBuffer.allocate(buffer.length).put(buffer).getLong();
	}
}
