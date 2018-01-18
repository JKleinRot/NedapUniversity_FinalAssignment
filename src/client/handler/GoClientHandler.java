package client.handler;

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
