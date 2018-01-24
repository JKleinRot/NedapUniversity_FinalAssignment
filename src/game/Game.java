package game;

import java.util.List;

import client.handler.GoClientHandler;

/**
 * A game of Go.
 * @author janine.kleinrot
 */
public interface Game extends Runnable {

	public void run();
	
	/**
	 * Confirms the move made by the GoClient.
	 * @param moveMade
	 * 			The move.
	 */
	public void confirmMove(String moveMade);
	
	/**
	 * Return the goClientHandlers currently in the game.
	 * @return
	 * 			A list of GoClientHandlers.
	 */
	public List<GoClientHandler> getGoClientHandlers();
}
