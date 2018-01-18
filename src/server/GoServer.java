package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import client.handler.GoClientHandler;
import client.handler.GoClientHandlerImpl;

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
	
	/** The list of client handlers. */
	private List<GoClientHandler> goClientHandlers;
	
	/**
	 * Creates a new server with the provided port number to play a game of Go. 
	 * Reads standard input using the initialized scanner.
	 * Initializes a list of client handlers.
	 * @param port
	 * 			The port of the server.
	 */
	public GoServer(int port) {
		this.port = port;
		this.in = new Scanner(System.in);
		this.goClientHandlers = new ArrayList<GoClientHandler>();
	}
	
	/**
	 * Waits for clients to connect to the Go server. 
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("GO SERVER: Initialized at port " + port);
			System.out.println("GO SERVER: Waiting for clients to connect...");
			while (true) {
				Socket socket = serverSocket.accept();
				GoClientHandler goClientHandler = new GoClientHandlerImpl(socket);
				Thread goClientHandlerThread = new Thread(goClientHandler);
				goClientHandlerThread.start();
				this.addGoClientHandler(goClientHandler);
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
	 * Adds the goClientHandler to the list of client handlers.
	 * @param goClientHandler
	 * 			The added client handler.
	 */
	public void addGoClientHandler(GoClientHandler goClientHandler) {
		goClientHandlers.add(goClientHandler);
	}
	
	/**
	 * Removes the goClientHandler of the list of client handlers.
	 * @param goClientHandler
	 * 			The removed client handler.
	 */
	public void removeGoClientHandler(GoClientHandler goClientHandler) {
		goClientHandlers.remove(goClientHandler);
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
	 * Start a new server with the provided port number to play a game of Go.
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
