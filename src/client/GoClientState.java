package client;

import java.util.ArrayList;
import java.util.List;

import game.GoClientStateListener;

/**
 * The state in which the client is. 
 * Possible values are:
 * UNCONNECTED, not connected to client 
 * CONNECTED, connected to client
 * GAME_REQUESTED, requested game at server
 * PLAYING_GAME, playing a game
 * @author janine.kleinrot
 */
public enum GoClientState {
	
	UNCONNECTED, CONNECTED, GAME_REQUESTED, PLAYING_GAME;
	
	private List<GoClientStateListener> goClientStateListeners = 
			new ArrayList<GoClientStateListener>();
	
	/**
	 * Add the provided GoClientStateListener to the list of goClientStateListeners.
	 * @param goClientStateListener
	 * 			The GoClientStateListener added to the list.
	 */
	public void addGoClientStateListener(GoClientStateListener goClientStateListener) {
		goClientStateListeners.add(goClientStateListener);
	}
	
	/**
	 * Remove the provided GoClientStateListener from the list of goClientStateListeners.
	 * @param goClientStateListener
	 * 			The GoClientStateListener removed from the list.
	 */
	public void removeGoClientStateListener(GoClientStateListener goClientStateListener) {
		goClientStateListeners.remove(goClientStateListener);
	}

}
