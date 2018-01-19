package client.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import client.GoClientState;
import client.GoClientStateListener;
import protocol.Protocol.Client;
import protocol.Protocol.General;

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
	
	/** The actor of the client. */
	private GoClientHandlerActor goClientHandlerActor;
	
	/** The GoClientState. */
	private GoClientState goClientState;
	
	/** The opponent. */
	private GoClientHandler opponent;
	
	/** The name of the GoClient. */
	private String goClientName;
	
	/** The stone color of the GoClient. */
	private String stoneColor;
	
	/** The board size. */
	private String boardSize;
	
	/**
	 * Creates a new client handler.
	 * Initializes the actor.
	 * Initializes the GoClientState to UNCONNECTED and add GoClientStateListener.
	 * @param socket
	 * 			The socket of the client.
	 */
	public GoClientHandlerImpl(Socket socket, GoClientStateListener gameManager) {
		this.name = "Go Server";
		goClientHandlerActor = new GoClientHandlerActorImpl(this, gameManager);
		goClientState = GoClientState.UNCONNECTED;
		goClientState.addGoClientStateListener(gameManager);
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
				if (words.length == 12 && words[0].equals(Client.NAME)) {
					goClientName = words[1].toUpperCase();
					goClientHandlerActor.confirmConnection(words, name);
				} else if (words.length == 3 && words[0].equals(Client.REQUESTGAME)) {
					goClientHandlerActor.handleGameRequest();
				} else if (words.length == 3 && words[0].equals(Client.SETTINGS)) {
					stoneColor = words[1];
					boardSize = words[2];
					goClientHandlerActor.notifyOtherClientOfGameSettings(opponent, words[1], 
							words[2]);
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR: Connection lost with Go server");
		}
	}
	
	@Override
	public void setGoClientState(GoClientState goClientState) {
		this.goClientState = goClientState;
	}
	
	@Override
	public void run() {
		readMessage();
		
	}

	@Override
	public void setOpponent(GoClientHandler opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public String getGoClientName() {
		return goClientName;
	}

	@Override
	public String getStoneColor() {
		return stoneColor;
	}

	@Override
	public String getBoardSize() {
		return boardSize;
	}
}
