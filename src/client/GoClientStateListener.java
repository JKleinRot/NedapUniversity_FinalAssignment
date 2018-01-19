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

}
