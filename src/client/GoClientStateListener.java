package client;

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
	 * Initialize a new game of Go.
	 * Asks the first GoClientHandler for the settings of the game.
	 * Asks both GoClientHandlers for their player type.
	 * @param firstGoClientHandler
	 * 			The first client entered in the game.
	 * @param secondGoClientHandler
	 * 			The second client entered in the game.
	 */
	public void startGame(GoClientHandler firstGoClientHandler, 
			GoClientHandler secondGoClientHandler);
}
