package game;

import java.util.List;

import client.handler.GoClientHandler;
import client.GoClientState;
import client.GoClientStateListener;

/**
 * Manages the initiation of a game for GoClients that requested a game.
 * @author janine.kleinrot
 *
 */
public class GameManager implements GoClientStateListener {

	/**	The GoClientHandlers corresponding to the GoClients that requested a game. */
	private List<GoClientHandler> goClientHandlersGameRequested;
	
	/**	The GoClientHandlers corresponding to the GoClients that play a game. */
	private List<GoClientHandler> goClientHandlersPlayingGame;
	
	@Override
	public void stateChanged(GoClientState goClientState) {
		if (goClientState == GoClientState.GAME_REQUESTED) {
			;
		}
	}

}
