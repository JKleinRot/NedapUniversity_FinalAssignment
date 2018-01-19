package game;

import java.util.ArrayList;
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
	
	/**
	 * Creates a GameManager.
	 */
	public GameManager() {
		goClientHandlersGameRequested = new ArrayList<GoClientHandler>();
		goClientHandlersPlayingGame = new ArrayList<GoClientHandler>();
	}
	
	@Override
	public void stateChanged(GoClientHandler goClientHandler, GoClientState goClientState) {
		if (goClientState == GoClientState.GAME_REQUESTED) {
			goClientHandlersGameRequested.add(goClientHandler);
		} else if (goClientState == GoClientState.PLAYING_GAME) {
			goClientHandlersGameRequested.remove(goClientHandler);
			goClientHandlersPlayingGame.add(goClientHandler);
		} else {
			if (!goClientHandlersGameRequested.isEmpty()) {
				if (goClientHandlersGameRequested.contains(goClientHandler)) {
					goClientHandlersGameRequested.remove(goClientHandler);
				}
			} else if (!goClientHandlersPlayingGame.isEmpty()) {
				if (goClientHandlersPlayingGame.contains(goClientHandler)) {
					goClientHandlersPlayingGame.remove(goClientHandler);
				}
			}
		}
	}

}
