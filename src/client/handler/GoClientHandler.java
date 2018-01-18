package client.handler;

/**
 * A client handler for the communication between the server and the clients.
 * @author janine.kleinrot
 */
public interface GoClientHandler extends Runnable {

	/**
	 * Runs continuously while the thread is running.
	 */
	public void run();
	
	/**
	 * Sends provided message over socket to GoClient.
	 * @param message
	 * 			Message send.
	 */
	public void sendMessage(String message);
	
	/**
	 * Reads message over socket from GoClient.
	 */
	public void readMessage();
	
}
