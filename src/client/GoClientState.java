package client;

import java.util.List;

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
	
	private List<GoClientStateListener> goClientStateListeners;
	
	public void addGoClientStateListener(GoClientStateListener goClientStateListener) {
		goClientStateListeners.add(goClientStateListener);
	}
	
	public void removeGoClientStateListener(GoClientStateListener goClientStateListener) {
		goClientStateListeners.remove(goClientStateListener);
	}

}
