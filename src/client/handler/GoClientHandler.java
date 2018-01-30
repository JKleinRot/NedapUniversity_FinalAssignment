package client.handler;

import client.GoClientState;
import server.GoServer;

/**
 * A client handler for the communication between the server and the clients.
 * @author janine.kleinrot
 */
public interface GoClientHandler extends Runnable {

	/**
	 * Run continuously while the thread is running.
	 */
	public void run();
	
	/**
	 * Send provided message over socket to GoClient.
	 * @param message
	 * 			Message send.
	 */
	public void sendMessage(String message);
	
	/**
	 * Read message over socket from GoClient.
	 */
	public void readMessage();
	
	/**
	 * Set the GoClientState.
	 * @param goClientState
	 * 			The goClientState.
	 */
	public void setGoClientState(GoClientState goClientState);
	
	/**
	 * Set the opponent in the game.
	 * @param opponent
	 * 			The opponent.
	 */
	public void setOpponent(GoClientHandler opponent);
	
	/**
	 * Get the name of the GoClient.
	 * @return
	 * 			The name of the GoClient.
	 */
	public String getGoClientName();
	
	/**
	 * Return the stone color of the GoClient of the GoClientHandler.
	 * @return
	 * 			The stone color.
	 */
	public String getStoneColor();
	
	/**
	 * Return the board size.
	 * @return
	 * 			The board size.
	 */
	public String getBoardSize();
	
	/**
	 * Set the board size.
	 * @return
	 * 			The board size.
	 */
	public void setBoardSize(String boardSize); 

	/**
	 * Get the GoClientHandlerActor of the GoClientHandler.
	 * @return
	 * 			The GoClientHandlerActor.
	 */
	public GoClientHandlerActor getGoClientHandlerActor();
	
	/**
	 * Get the GoServer.
	 * @return
	 * 			The GoServer.
	 */
	public GoServer getGoServer();
	
}
