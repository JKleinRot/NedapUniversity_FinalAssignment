package client.handler;

import client.GoClientState;
import client.GoClientStateListener;
import protocol.Protocol.General;
import protocol.Protocol.Server;

/**
 * Handles the actions required after input received from the GoClient.
 * @author janine.kleinrot
 */
public class GoClientHandlerActorImpl implements GoClientHandlerActor {
	
	/** The GoClientHandler. */
	private GoClientHandler goClientHandler;
	
	private GoClientStateListener gameManager;
	
	/**
	 * Creates a new GoClientHandlerActor for the provided goClientHandler.
	 * @param goClientHandler
	 * 			The goClientHandler.
	 */
	public GoClientHandlerActorImpl(GoClientHandler goClientHandler, 
			GoClientStateListener gameManager) {
		this.goClientHandler = goClientHandler;
		this.gameManager = gameManager;
	}

	@Override
	public void confirmConnection(String[] words, String name) {
		System.out.println("GO SERVER: Client " + words[1] + " connected");
		goClientHandler.sendMessage(Server.NAME + General.DELIMITER1 + name + General.DELIMITER1 + 
				Server.VERSION + General.DELIMITER1 +  Server.VERSIONNO + 
				General.DELIMITER1 + Server.EXTENSIONS + General.DELIMITER1 + 0 + 
				General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 
				0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
				General.DELIMITER1 + 0 + General.COMMAND_END);
		setGoClientState(GoClientState.CONNECTED);
		System.out.println("GO SERVER: Waiting for clients to connect...");
	}
	
	@Override
	public void setGoClientState(GoClientState goClientState) {
		goClientHandler.setGoClientState(goClientState);
		gameManager.stateChanged(goClientHandler, goClientState);
	}
	
	@Override
	public void handleGameRequest() {
		setGoClientState(GoClientState.GAME_REQUESTED);
	}

}
