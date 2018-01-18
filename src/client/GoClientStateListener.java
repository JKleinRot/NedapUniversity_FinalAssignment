package client;

/**
 * Performs actions if the GoClientState changes.
 * @author janine.kleinrot
 */
public interface GoClientStateListener {
	
	/**
	 * Handles the change to the provided goClientState.
	 * @param goClientState
	 * 			The current GoClientState.
	 */
	public void stateChanged(GoClientState goClientState);

}
