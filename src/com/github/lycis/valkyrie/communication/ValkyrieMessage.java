package com.github.lycis.valkyrie.communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This is the base class for all messages that will be sent using the valkyrie
 * peer network. If you wish to implement your own message you have to derive
 * from this class.
 * 
 * 
 * @author lycis
 *
 */
public abstract class ValkyrieMessage {
	private static AtomicLong MSG_ID_SEQUENCE = new AtomicLong();
	
	private ValkyrieMessageHeader header = new ValkyrieMessageHeader();
	
	/**
	 * Creates an instance of the message.
	 */
	public ValkyrieMessage() {
		header.setId(MSG_ID_SEQUENCE.getAndIncrement());
	}
	
	/**
	 * Returns the data of the message. When implementing your own message
	 * type this has to return the data that the message contains.
	 * @return data of the message
	 */
	abstract public ByteArrayOutputStream getMessageData();
	
	/**
	 * This method is called when the message is read from the incoming channel.
	 * When implementing your own message type this method should be used to 
	 * deserialise the message data.
	 * 
	 * @param data raw message data
	 */
	abstract public void setMessageData(ByteArrayInputStream data);
	
	/**
	 * Converts the complete message (incl. header) to a single data stream.
	 * This is used when sending the message.
	 *  
	 * @return data stream of the message
	 */
	public ByteArrayOutputStream toDataStream() throws IOException {
		ByteArrayOutputStream data = getMessageData();
		header.setDataLength(data.size());
		
		ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
		header.toDataStream().writeTo(data);
		data.writeTo(dataStream);
		return data;
	}
	
	/**
	 * Converts a data stream into a message. This is used when reading a
	 * message from a channel.
	 * 
	 * @param data raw data of the message
	 * @throws IOException
	 */
	public void fromDataStream(ByteArrayInputStream data) throws IOException {
		header.fromDataStream(data);
		setMessageData(data);
	}
	
	/**
	 * Provides the header of this message.
	 */
	public ValkyrieMessageHeader getHeader() {
		return header;
	}
}
