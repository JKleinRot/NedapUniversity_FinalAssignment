package game;

import java.util.List;

import client.handler.GoClientHandler;
import client.handler.GoClientHandlerActorImpl;

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
	public void confirmMove(String moveMade, GoClientHandler goClientHandler);
	
	/**
	 * Return the goClientHandlers currently in the game.
	 * @return
	 * 			A list of GoClientHandlers.
	 */
	public List<GoClientHandler> getGoClientHandlers();

	/**
	 * End the game aborted by the goClientHandler.
	 * @param goClientHandler
	 * 			The goClientHandler.
	 */
	public void endAbortedGame(GoClientHandler goClientHandler);
}
