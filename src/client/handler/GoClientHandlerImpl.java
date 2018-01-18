package client.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import protocol.Protocol.General;
import protocol.Protocol.Server;

/**
 * A client handler for the communication between the server and the clients.
 * @author janine.kleinrot
 */
public class GoClientHandlerImpl implements GoClientHandler {
	
	/** Reader to read from input stream. */
	private BufferedReader in;
	
	/** Writer to write to output stream. */
	private BufferedWriter out;
	
	/** The name of the server. */
	private String name;
	
	private GoClientHandlerActor goClientHandlerActor;
	
	/**
	 * Creates a new client handler.
	 * @param socket
	 * 			The socket of the client.
	 */
	public GoClientHandlerImpl(Socket socket) {
		this.name = "Go Server";
		goClientHandlerActor = new GoClientHandlerActorImpl(this);
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			System.out.println("ERROR: Could not create GoClientHandler");
		}
	}
	
	@Override
	public void sendMessage(String message) {
		try {
			out.write(message);
			out.flush();
		} catch (IOException e) {
			System.out.println("ERROR: Connection lost with Go server");
		}
	}
	
	@Override
	public void readMessage() {
		String message = "";
		try {
			while ((message = in.readLine()) != null) {
				String[] words = message.split("\\" + General.DELIMITER1);
				if (words.length == 12) {
					System.out.println("GO SERVER: Client " + words[1] + " connected");
					System.out.println(message);
					goClientHandlerActor.confirmConnection(words, name);
					System.out.println("GO SERVER: Waiting for clients to connect...");
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR: Connection lost with Go server");
		}
	}
	
	@Override
	public void run() {
		readMessage();
		
	}

}
