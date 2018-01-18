package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import client.tui.GoClientTUI;
import protocol.Protocol.Client;
import protocol.Protocol.General;
import protocol.Protocol.Server;

/** 
 * Client able to connect to the Go server.
 * @author janine.kleinrot
 */
public class GoClient extends Observable implements Runnable { 
	
	/** The name of the client. */
	private String name;
	
	/** TUI for the client. */
	private GoClientTUI goClientTUI;
	
	/** The socket. */
	private Socket socket;
	
	/** Reader to read from input stream. */
	private BufferedReader in;
	
	/** Writer to write to output stream. */
	private BufferedWriter out;
	
	/**	If the Go client is connected to the Go server. */
	private boolean isConnected;
	
	/**
	 * Creates a new client with the provided name that can connect to the Go server.
	 * Initializes and starts the TUI.
	 * Adds the TUI as an observer.
	 * Initializes isConnected to false;
	 * @param name
	 * 			The name of the client.
	 */
	public GoClient(String name) {
		this.name = name;
		goClientTUI = new GoClientTUI(this);
		Thread goClientTUIThread = new Thread(goClientTUI);
		goClientTUIThread.start();
		isConnected = false;
	}
	
	/**
	 * Attempts to connect to the Go server with the provided IP address and port number.
	 * Opens the input and output stream of the socket if connected.
	 * @param ipAddress
	 * 			IP address of the server.
	 * @param port
	 * 			Port number of the server
	 */
	public synchronized void connect(String ipAddress, String port) {
		try {
			socket = new Socket(InetAddress.getByName(ipAddress), Integer.parseInt(port));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			sendMessage(Client.NAME + General.DELIMITER1 + name + General.DELIMITER1 + 
					Client.VERSION + General.DELIMITER1 +  Client.VERSIONNO + General.DELIMITER1 + 
					Client.EXTENSIONS + General.DELIMITER1 + 0 + General.DELIMITER1 + 
					0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 
					0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.COMMAND_END);
			isConnected = true;
			notifyAll();
			setChanged();
			notifyObservers("Connected");
		} catch (NumberFormatException e) {
			System.out.println("ERROR: Not a valid port number");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("ERROR: Not a valid host");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR: Could not connect to Go server");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends provided message over socket to GoClientHandler.
	 * @param message
	 * 			Message send.
	 */
	public void sendMessage(String message) {
		try {
			out.write(message);
			out.flush();
		} catch (IOException e) {
			System.out.println("ERROR: Connection lost with Go server");
		}
	}
	
	/**
	 * Reads message over socket from GoClientHandler.
	 */
	public synchronized void readMessage() {
		while (!isConnected) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String message = "";
		try {
			while ((message = in.readLine()) != null) {
				String[] words = message.split("\\" + General.DELIMITER1);
				if (words.length == 12) {
					System.out.println(message); 
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR: Connection lost with Go server");
		} catch (NullPointerException e) {
			readMessage();
		}
	}
	
	/**
	 * Runs continuously while the thread is running.
	 */
	public void run() {
		readMessage();
	}
	
	/** 
	 * Starts a new client with the provided name to connect to a Go server.
	 * @param args
	 * 			The name of the client.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("ERROR: a name should be provided");
			System.exit(0);
		}
		GoClient goClient = new GoClient(args[0]);
		Thread goClientThread = new Thread(goClient);
		goClientThread.start();
	}

}
