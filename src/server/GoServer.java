package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import client.GoClientStateListener;
import client.handler.GoClientHandler;
import client.handler.GoClientHandlerImpl;
import game.GameManager;

/**
 * Server to play a game of Go.
 * @author janine.kleinrot
 */
public class GoServer {
	
	/** The port of the server. */
	private String port;
	
	/** The server socket. */
	private ServerSocket serverSocket;
	
	/**	A scanner to read input. */
	private Scanner in;
	
	/** The list of client handlers. */
	private List<GoClientHandler> goClientHandlers;
	
	/** The GameManager. */
	private GoClientStateListener gameManager;
	
	/**
	 * Create a new server with the provided port number to play a game of Go. 
	 * Read standard input using the initialized scanner.
	 * Initialize a list of client handlers and a GameManager.
	 * @param port
	 * 			The port of the server.
	 */
	public GoServer(String port) {
		this.port = port;
		this.in = new Scanner(System.in);
		this.goClientHandlers = new ArrayList<GoClientHandler>();
		this.gameManager = new GameManager();
	}
	
	/**
	 * Wait for clients to connect to the Go server. 
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket(Integer.parseInt(port));
			System.out.println("GO SERVER: Initialized at port " + port);
			System.out.println("GO SERVER: Waiting for clients to connect...");
			while (true) {
				Socket socket = serverSocket.accept();
				GoClientHandler goClientHandler = 
						new GoClientHandlerImpl(socket, gameManager, this);
				Thread goClientHandlerThread = new Thread(goClientHandler);
				goClientHandlerThread.start();
			}
		} catch (NumberFormatException e) {
			System.out.println("ERROR: not a valid port number");
			port = readString("New port number: ");
			this.run();
		} catch (BindException e) {
			System.out.println("ERROR: Port " + port + " already in use.");
			port = readString("New port number: ");
			this.run();
		} catch (IOException e) {
			System.out.println("ERROR: Could not set up a Go server.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Add the goClientHandler to the list of client handlers.
	 * @param goClientHandler
	 * 			The added client handler.
	 * @return 
	 * 			True if no GoClient tried to connect with this name and false otherwise.
	 */
	public boolean addGoClientHandler(GoClientHandler goClientHandler) {
		for (GoClientHandler connectedGoClientHandler : goClientHandlers) {
			if (connectedGoClientHandler.getGoClientName().toUpperCase()
					.equals(goClientHandler.getGoClientName().toUpperCase())) {
				return false;
			}
		}
		goClientHandlers.add(goClientHandler);
		return true;
	}
	
	/**
	 * Remove the goClientHandler of the list of client handlers.
	 * @param goClientHandler
	 * 			The removed client handler.
	 */
	public void removeGoClientHandler(GoClientHandler goClientHandler) {
		goClientHandlers.remove(goClientHandler);
		System.out.println("GO SERVER: Client " + goClientHandler.getGoClientName() + 
				" disconnected");
		System.out.println("GO SERVER: Waiting for clients to connect...");
	}
	
	/**
	 * Read standard input after sending prompt to standard output.
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
		GoServer goServer = new GoServer(args[0]);
		goServer.run();
	}

}
