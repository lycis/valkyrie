package com.github.lycis.valkyrie.communication;

import java.io.ByteArrayOutputStream;
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
	
	private long id;
	
	/**
	 * Creates an instance of the message.
	 */
	public ValkyrieMessage() {
		id = MSG_ID_SEQUENCE.getAndIncrement();
	}
	
	/**
	 * Returns the data of the message. When implementing your own message
	 * type this has to return the data that the message contains.
	 * @return data of the message
	 */
	abstract public ByteArrayOutputStream getMessageData();

	public int getMessageLength() {
		return getMessageData().size();
	}
	
	/**
	 * Provides the internal ID of this message. This ID is only valid within
	 * one node of the peer network.
	 */
	public long getId() {
		return id;
	}
}
