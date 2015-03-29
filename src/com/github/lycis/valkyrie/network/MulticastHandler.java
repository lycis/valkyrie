package com.github.lycis.valkyrie.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Handles incoming messages on the multicast channel (unreliable events, joins,
 * ...)
 * 
 * @author lycis
 *
 */
class MulticastHandler implements Runnable {
	private MulticastSocket mcSocket = null;

	private static int PACKAGE_MAX_SIZE = 8192; // 8 Kb max package size

	public MulticastHandler(MulticastSocket mcSocket) {
		this.mcSocket = mcSocket;
	}

	@Override
	public void run() {
		
		// handle incoming multicast messages
		while (!mcSocket.isClosed()) {
			
			// read message
			byte[] receiveBuffer = new byte[PACKAGE_MAX_SIZE];
			DatagramPacket pkg = new DatagramPacket(receiveBuffer,
					PACKAGE_MAX_SIZE);
			try {
				mcSocket.receive(pkg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// TODO deserialize message object			
		}
	}

}
