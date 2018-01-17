package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Server to play a game of Go.
 * @author janine.kleinrot
 */
public class GoServer {
	
	/** The port of the server. */
	private int port;
	
	/** The server socket. */
	private ServerSocket serverSocket;
	
	/**	A scanner to read input. */
	private Scanner in;
	
	/**
	 * Create a new server to play a game of Go. 
	 * Standard input is read using the initialized scanner.
	 * @param port
	 * 			The port of the server.
	 */
	public GoServer(int port) {
		this.port = port;
		this.in = new Scanner(System.in);
	}
	
	/**
	 * Waits for clients to connect to the Go server. 
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Go server initialized at port " + port);
			while (true) {
				Socket socket = serverSocket.accept();
			}
		} catch (BindException e) {
			System.out.println("ERROR: Port " + port + " already in use.");
			port = Integer.parseInt(readString("New port number: "));
			this.run();
		} catch (IOException e) {
			System.out.println("ERROR: Could not set up a Go server.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads standard input after sending prompt to standard output.
	 * @param prompt
	 * 			The message displayed at standard output.
	 * @return
	 * 			The line read from standard input.
	 */
	private String readString(String prompt) {
		String result = null;
		System.out.println(prompt);
		if (in.hasNextLine()) {
			result = in.nextLine();
		}
		return result;
	}
	
	/**
	 * Start a new server to play a game of Go.
	 * @param args
	 * 			The port number of the Go server.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("ERROR: A port number should be provided");
			System.exit(0);
		}
		GoServer goServer = new GoServer(Integer.parseInt(args[0]));
		goServer.run();
	}

}
