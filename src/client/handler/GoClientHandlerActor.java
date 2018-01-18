package client.handler;

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

}
