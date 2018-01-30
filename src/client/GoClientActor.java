package client;

import game.player.Player;

/**
 * Handle the actions required after input received from the GoClientHandler and GoClientTUI.
 * @author janine.kleinrot
 */
public interface GoClientActor {
	
	/**
	 * Attempt to connect to the Go server with the provided IP address and port number.
	 * Open the input and output stream of the socket if connected.
	 * Send information of itself to Go server.
	 * @param ipAddress
	 * 			IP address of the server.
	 * @param port
	 * 			Port number of the server
	 */
	public void connect(String ipAddress, String port);
	
	/**
	 * Return the name of the Go client.
	 * @return
	 * 			The name of the Go client.
	 */
	public String getName();
	
	/**
	 * Notify GoClientTUI that the connection is confirmed.
	 * @param words
	 * 			The input received over the socket from the Go server.
	 */
	public void showConnectionConfirmed(String[] words);
	
	/**
	 * Ask the GoClient for a new name.
	 */
	public void handleNameError();
	
	
	/**
	 * Change the name to the provided name.
	 * @param name
	 * 			The new name.
	 */
	public void changeName(String name);
	
	/**
	 * Notify GoClientTUI that the client is already connected.
	 */
	public void alreadyConnected();

	/**
	 * Request to play a game of Go as the provided type.
	 * @param goPlayerType
	 * 			The player type.
	 */
	public void requestGame(String goPlayerType);
	
	/**
	 * Get the game settings from the client.
	 */
	public void getGameSettings();
	
	/**
	 * Set the provided stone color and board size.
	 * @param stoneColor
	 * 			The stone color.
	 * @param boardSize
	 * 			The board size.
	 */
	public void setGameSettings(String goStoneColor, String goBoardSize);
	
	/**
	 * Save the received settings for the second client.
	 * @param aStoneColor
	 * 			The stone color.
	 * @param aBoardSize
	 * 			The board size.
	 * @param playerName
	 * 			The name of the player.
	 * @param otherPlayerName
	 * 			The name of the opponent.
	 */
	public void setReceivedGameSettings(String aStoneColor, String aBoardSize, 
			String playerName, String otherPlayerName);
	
	/**
	 * Get the player.
	 * @return 
	 * 			The player.
	 */
	public Player getPlayer();
	
	/**
	 * Send the move to the server.
	 * @param move
	 * 			The move.
	 */
	public void sendMove(String move);

	/** 
	 * Handle an invalid move checked by the Game. 
	 */
	public void handleInvalidMove();
	
	/**
	 * Handle unknown command given to Go server.
	 */
	public void handleUnknownCommand();
	
	/**
	 * Handle the end of a game.
	 * @param reason
	 * 			The reason for the game to end.
	 * @param winningPlayer
	 * 			The name of the winning player.
	 * @param winningScore
	 * 			The score of the winning player.
	 * @param losingPlayer
	 * 			The name of the losing player.
	 * @param losingScore
	 * 			The score of the losing player.
	 */
	public void handleEndOfGame(String reason, String winningPlayer, String winningScore, 
			String losingPlayer, String losingScore);

	/** 
	 * Exit the Go server.
	 */
	public void exit();
	
	/**
	 * Quit a currently running game.
	 */
	public void quitGame();
	
	/**
	 * Return the GoClient.
	 * @return goClient
	 * 			The goClient.
	 */
	public GoClient getGoClient();
	
	/**
	 * Handle end of connection.
	 */
	public void handleEndOfConnection();
	
}
