package game;

import client.GoClientState;
import client.handler.GoClientHandler;

/**
 * Performs actions if the GoClientState changes.
 * @author janine.kleinrot
 */
public interface GoClientStateListener {
	
	/**
	 * Handles the change to the provided goClientState of the provided goClientHandler.
	 * @param goClientState
	 * 			The current GoClientState.
	 */
	public void goClientStateChanged(GoClientHandler goClientHandler, GoClientState goClientState);

	/**
	 * Asks the first GoClientHandler for the settings of the game.
	 * @param firstGoClientHandler
	 * 			The first client entered in the game.
	 * @param secondGoClientHandler
	 * 			The second client entered in the game.
	 */
	public void getGameSettings(GoClientHandler firstGoClientHandler, 
			GoClientHandler secondGoClientHandler);
	
	/**
	 * Start a new game of Go with the firstGoClientHandler playing with black.
	 * @param firstGoClientHandler
	 * 			The GoClient playing with black.
	 * @param secondGoClientHandler
	 * 			The GoClient playing with white.
	 */
	public void startGame(GoClientHandler firstGoClientHandler, 
			GoClientHandler secondGoClientHandler);

}
