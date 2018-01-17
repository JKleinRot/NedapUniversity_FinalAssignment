package client;

import java.net.Socket;

import server.GoServer;

/**
 * A client handler for the communication between the server and the clients.
 * @author janine.kleinrot
 */
public class GoClientHandler extends Thread {
	
	/**
	 * Creates a new client handler.
	 * @param goServer
	 * 			The server to play a game of Go.
	 * @param socket
	 * 			The socket of the client.
	 */
	public GoClientHandler(GoServer goServer, Socket socket) {
		
		
	}

}
