package client;

import game.player.Player;

/**
 * Handles the actions required after input received from the GoClientHandler and GoClientTUI.
 * @author janine.kleinrot
 */
public interface GoClientActor {
	
	/**
	 * Attempts to connect to the Go server with the provided IP address and port number.
	 * Opens the input and output stream of the socket if connected.
	 * Sends information of itself to Go server.
	 * @param ipAddress
	 * 			IP address of the server.
	 * @param port
	 * 			Port number of the server
	 */
	public void connect(String ipAddress, String port);
	
	/**
	 * Returns the name of the Go client.
	 * @return
	 * 			The name of the Go client.
	 */
	public String getName();
	
	/**
	 * Notifies GoClientTUI that the connection is confirmed.
	 * @param words
	 * 			The input received over the socket from the Go server.
	 */
	public void showConnectionConfirmed(String[] words);
	
	/**
	 * Notifies GoClientTUI that the client is already connected.
	 */
	public void alreadyConnected();

	/**
	 * Requests to play a game of Go as the provided type.
	 */
	public void requestGame(String goPlayerType);
	
	/**
	 * Gets the game settings from the client.
	 */
	public void getGameSettings();
	
	/**
	 * Sets the provided stone color and board size.
	 * @param stoneColor
	 * 			The stone color.
	 * @param boardSize
	 * 			The board size.
	 */
	public void setGameSettings(String goStoneColor, String goBoardSize);
	
	/**
	 * Saves the received settings for the second client.
	 * @param stoneColor
	 * 			The stone color.
	 * @param boardSize
	 * 			The board size.
	 */
	public void setReceivedGameSettings(String aStoneColor, String aBoardSize, String playerName, String otherPlayerName);
	
	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer();
	
	/**
	 * Sends the move to the server.
	 * @param move
	 * 			The move.
	 */
	public void sendMove(String move);

	/**
	 * Handles the end of a game.
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
	public void handleEndOfGame(String reason, String winningPlayer, String winningScore, String losingPlayer, String losingScore);
}
