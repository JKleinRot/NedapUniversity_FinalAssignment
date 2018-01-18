package client;

/**
 * Handles the actions required after input received from the GoClientHandler and GoClientTUI.
 * @author janine.kleinrot
 */
public interface GoClientActor {
	
	/**
	 * Attempts to connect to the Go server with the provided IP address and port number.
	 * Opens the input and output stream of the socket if connected.
	 * Sends information of itself to Go server.
	 * @param ipAddress
	 * 			IP address of the server.
	 * @param port
	 * 			Port number of the server
	 */
	public void connect (String ipAddress, String port);

}
