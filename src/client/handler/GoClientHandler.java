package client.handler;

import client.GoClientState;

/**
 * A client handler for the communication between the server and the clients.
 * @author janine.kleinrot
 */
public interface GoClientHandler extends Runnable {

	/**
	 * Runs continuously while the thread is running.
	 */
	public void run();
	
	/**
	 * Sends provided message over socket to GoClient.
	 * @param message
	 * 			Message send.
	 */
	public void sendMessage(String message);
	
	/**
	 * Reads message over socket from GoClient.
	 */
	public void readMessage();
	
	/**
	 * Sets the GoClientState.
	 */
	public void setGoClientState(GoClientState goClientState);
	
	/**
	 * Sets the opponent in the game.
	 * @param opponent
	 * 			The opponent.
	 */
	public void setOpponent(GoClientHandler opponent);
	
	/**
	 * Gets the name of the GoClient.
	 * @return
	 * 			The name of the GoClient.
	 */
	public String getGoClientName();
	
	/**
	 * Returns the stone color of the GoClient of the GoClientHandler.
	 * @return
	 * 			The stone color.
	 */
	public String getStoneColor();
	
	/**
	 * Returns the board size.
	 * @return
	 * 			The board size.
	 */
	public String getBoardSize();
	
	/**
	 * Sets the board size.
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
}
