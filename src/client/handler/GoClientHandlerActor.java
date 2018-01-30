package client.handler;

import client.GoClientState;
import game.Game;

/**
 * Handle the actions required after input received from the GoClient.
 * @author janine.kleinrot
 */
public interface GoClientHandlerActor {

	/**
	 * Send information of itself to the client.
	 * @param words
	 * 			Decomposed received message.
	 * @param name
	 * 			The name of the GoServer.
	 */
	public void confirmConnection(String[] words, String name);

	/**
	 * Set the GoClientState to the provided GoClientState.
	 * @param goClientState
	 * 			The new GoClientState.
	 */
	public void setGoClientState(GoClientState goClientState);
	
	/**
	 * Handle the game request of the client of the GoClientHandler.
	 * Add the GoClientHandler to the game requested list in the GameManager
	 */
	public void handleGameRequest();
	
	/**
	 * Notify the other client of the choice made by the client on game settings.
	 * @param opponent
	 * 			The opponent GoClientHandler. 
	 * @param stoneColor
	 * 			The chosen stone color.
	 * @param boardSize
	 * 			The chosen board size.
	 */
	public void notifyOtherClientOfGameSettings(GoClientHandler opponent, 
			String stoneColor, String boardSize);
	
	/**
	 * Set the boardSize.
	 * @param boardSize
	 * 			The board size.
	 */
	public void setBoardSize(String boardSize);
	
	/**
	 * Confirm the move made by the player.
	 * @param move
	 * 			The move made.
	 * @param goClientHandler
	 * 			The GoClientHandler.
	 */
	public void confirmMove(String move, GoClientHandler goClientHandler);
	
	/**
	 * Set the game of the GoClientHandlerActor to the provided Game.
	 * @param game
	 * 			The game.
	 */
	public void setGame(Game game);
	
	/** 
	 * End the game if one of the GoClients aborted the game.
	 */
	public void endAbortedGame();
	
	/**
	 * End the connection with the Go server.
	 */
	public void endConnection();
	
}

