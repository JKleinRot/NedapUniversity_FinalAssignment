package client.handler;

import java.net.Socket;

import server.GoServer;

/**
 * A client handler for the communication between the server and the clients.
 * @author janine.kleinrot
 */
public class GoClientHandlerImpl implements GoClientHandler {
	
	/**
	 * Creates a new client handler.
	 * @param goServer
	 * 			The server to play a game of Go.
	 * @param socket
	 * 			The socket of the client.
	 */
	public GoClientHandlerImpl(GoServer goServer, Socket socket) {
		
		
	}
	
	public void run() {
		
	}

}
