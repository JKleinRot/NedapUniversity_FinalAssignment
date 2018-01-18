package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import client.tui.GoClientTUI;

/** 
 * Client able to connect to the Go server.
 * @author janine.kleinrot
 */
public class GoClient extends Observable { 
	
	/** The name of the client. */
	private String name;
	
	/** TUI for the client. */
	private GoClientTUI goClientTUI;
	
	/** The socket. */
	private Socket socket;
	
	/**
	 * Creates a new client with the provided name that can connect to the Go server.
	 * Initializes and starts the TUI.
	 * Adds the TUI as an observer.
	 * @param name
	 * 			The name of the client.
	 */
	public GoClient(String name) {
		this.name = name;
		goClientTUI = new GoClientTUI(this);
		goClientTUI.start();
	}
	
	public void connect(String ipAddress, String port) {
		try {
			socket = new Socket(InetAddress.getByName(ipAddress), Integer.parseInt(port));
			setChanged();
			notifyObservers("Connected");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	}

}
