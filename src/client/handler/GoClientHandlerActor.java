package client.handler;

import client.GoClientState;

/**
 * Handles the actions required after input received from the GoClient.
 * @author janine.kleinrot
 */
public interface GoClientHandlerActor {

	/**
	 * Sends information of itself to the client.
	 * @param words
	 * 			Decomposed received message.
	 */
	public void confirmConnection(String[] words, String name);

	/**
	 * Set the GoClientState to the provided GoClientState.
	 * @param goClientState
	 * 			The new GoClientState.
	 */
	public void setGoClientState(GoClientState goClientState);
	
	/**
	 * Handles the game request of the client of the GoClientHandler.
	 * Adds the GoClientHandler to the game requested list in the GameManager
	 */
	public void handleGameRequest();
}
