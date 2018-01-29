package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

import client.tui.GoClientTUI;
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
	
	/** The GoClientActor. */
	private GoClientActor goClientActor;
	
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
		goClientActor = new GoClientActorImpl(this);
		goClientTUI = new GoClientTUI(goClientActor);
		Thread goClientTUIThread = new Thread(goClientTUI);
		goClientTUIThread.start();
		isConnected = false;
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
				if (words.length == 12 && words[0].equals(Server.NAME)) {
					goClientActor.showConnectionConfirmed(words);
				} else if (words.length == 3 && words[0].equals(Server.ERROR) && words[1].equals(Server.NAMETAKEN)) {
					goClientActor.handleNameError();
					socket = null;
					isConnected = false;
					readMessage();
				} else if (words.length == 2 && words[0].equals(Server.START)) {
					goClientActor.getGameSettings();
				} else if (words.length == 6 && words[0].equals(Server.START)) {
					goClientActor.setReceivedGameSettings(words[2], words[3], words[4], words[5]);
				} else if (words.length == 4 && words[0].equals(Server.TURN) 
						&& words[3].equals(name.toUpperCase())) {
					goClientActor.getPlayer().processPreviousMove(words[2], words[1]);
					goClientActor.getPlayer().determineMove();
				} else if (words.length == 4 && words[0].equals(Server.TURN) 
						&& !words[3].equals(name.toUpperCase())) {
					goClientActor.getPlayer().processPreviousMove(words[2], words[1]);
				} else if (words.length == 3 && words[0].equals(Server.ERROR) && words[1].equals(Server.INVALID)) {
					goClientActor.handleInvalidMove();
				} else if (words.length == 6 && words[0].equals(Server.ENDGAME)) {
					goClientActor.handleEndOfGame(words[1], words[2], words[3], words[4], words[5]);
				} else if (words.length == 3 && words[0].equals(Server.ERROR) && words[1].equals(Server.UNKNOWN)) {
					goClientActor.handleUnknownCommand();
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
	 * Return the socket of the GoClient GoServer connection.
	 * @return
	 * 			the socket.
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * Set the socket of the GoClient to the provided socket.
	 * @param socket
	 * 			The socket.
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * Set the reader of the GoClient to the provided reader.
	 * @param inActor
	 * 			The reader.
	 */
	public void setReader(BufferedReader inActor) {
		in = inActor;
	}
	
	/**
	 * Set the writer of the GoClient to the provided writer.
	 * @param outActor
	 * 			The writer.
	 */
	public void setWriter(BufferedWriter outActor) {
		out = outActor;
	}
	
	/**
	 * Return the name of the GoClient.
	 * @return
	 * 			The name.
	 */
	public String getName() {
		return name;

	}
	
	/**
	 * Notifies the GoClient that is is connected and can start the reading and writing 
	 * with the GoServer.
	 */
	public synchronized void setIsConnected() {
		isConnected = true;
		notifyAll();
	}
	
	/**
	 * Change the name of the GoClient.
	 * @param name
	 * 			The new name.
	 */
	public void changeName(String name) {
		this.name = name;
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
