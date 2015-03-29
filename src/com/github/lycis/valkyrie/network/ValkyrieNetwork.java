package com.github.lycis.valkyrie.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * The peer network that is established by valkyrie is accessed by this class.
 * Use this class as an interface to access the network by joining or using it
 * to rely messages to your target.
 * 
 * Each network hast its own identfier that is used to identify peers when there
 * are multiple peer networks or listeners to the multicast group.
 * 
 * By default valkyrie uses multicast address 239.199.28.1 and port 6781 for
 * communication.
 * 
 * @author lycis
 *
 */
public class ValkyrieNetwork {
	private String identifier = "";
	private int port = 6781;
	private InetAddress address = null;
	private MulticastSocket mcSocket = null;
	private Thread mcHandlingThread;

	private static String DEFAULT_ADDRESS = "239.199.28.1";
	private static int DEFAULT_PORT = 6781;
	private static String DEFAULT_IDENTIFIER = "valkyrie";

	/**
	 * Provides access to a peer network with the given identifier. It uses the
	 * standard valkyrie multicast group and port for communication. Ususally
	 * you want something more elaborate.
	 * 
	 * @param identifier
	 */
	public ValkyrieNetwork(String identifier) {
		this.identifier = identifier;
		port = DEFAULT_PORT;
		try {
			address = InetAddress.getByName(DEFAULT_ADDRESS);
		} catch (UnknownHostException e) {
			address = null;
		}
		checkSettings();
	}

	/**
	 * Provides access to a peer network with the given identifier and address.
	 * The default port will be used for communication.
	 * 
	 * @param identifier
	 * @param address
	 */
	public ValkyrieNetwork(String identifier, InetAddress address) {
		this.identifier = identifier;
		this.address = address;
		port = DEFAULT_PORT;
		checkSettings();
	}

	/**
	 * Provides access to a peer network with the given identifier by using the
	 * given port for communication. All multicasts will be sent on the default
	 * address.
	 * 
	 * @param identifier
	 * @param port
	 */
	public ValkyrieNetwork(String identifier, int port) {
		this.identifier = identifier;
		this.port = port;
		try {
			address = InetAddress.getByName(DEFAULT_ADDRESS);
		} catch (UnknownHostException e) {
			address = null;
		}
		checkSettings();
	}

	private void checkSettings() throws IllegalArgumentException {
		// check identifier
		if (getIdentifier() == null) {
			throw new IllegalArgumentException("identifier must not be null");
		}

		if (getIdentifier().isEmpty()) {
			throw new IllegalArgumentException("identifier must not be empty");
		}

		// check port
		if (port == 0) {
			throw new IllegalArgumentException("invalid port number");
		}

		// check multicast address
		if (getAddress() == null) {
			throw new IllegalArgumentException("invalid address given");
		}
	}

	/**
	 * Gives the identifier that is used for communication.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Gives the address that is used for multicast communication.
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * Gives the port used for multicast communication.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Join the valkyrie peer network and start receiving events that peers
	 * publish. This is the first thing you have to do before you can do
	 * anything else with the peer network.
	 */
	public void join() throws IOException {
		if (mcSocket != null) {
			return; // already joined
		}

		mcSocket = new MulticastSocket(port);
		mcSocket.joinGroup(address);
		
		// start event handling thread
		mcHandlingThread = new Thread(new MulticastHandler(mcSocket));
		mcHandlingThread.start();
		
		// TODO send joined event to everybody
	}
}
