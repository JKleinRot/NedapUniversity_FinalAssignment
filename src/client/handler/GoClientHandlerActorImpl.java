package client.handler;

import protocol.Protocol.General;
import protocol.Protocol.Server;

/**
 * Handles the actions required after input received from the GoClient.
 * @author janine.kleinrot
 */
public class GoClientHandlerActorImpl implements GoClientHandlerActor {
	
	/** The GoClientHandler. */
	private GoClientHandler goClientHandler;
	
	/**
	 * Creates a new GoClientHandlerActor for the provided goClientHandler.
	 * @param goClientHandler
	 * 			The goClientHandler.
	 */
	public GoClientHandlerActorImpl(GoClientHandler goClientHandler) {
		this.goClientHandler = goClientHandler;
	}

	@Override
	public void confirmConnection(String[] words, String name) {
		goClientHandler.sendMessage(Server.NAME + General.DELIMITER1 + name + General.DELIMITER1 + 
				Server.VERSION + General.DELIMITER1 +  Server.VERSIONNO + 
				General.DELIMITER1 + Server.EXTENSIONS + General.DELIMITER1 + 0 + 
				General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 
				0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
				General.DELIMITER1 + 0 + General.COMMAND_END);
	}

}
