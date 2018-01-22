package client.handler;

import client.GoClientState;
import client.GoClientStateListener;
import game.Game;
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
	
	private String goClientName;
	
	private Game game;
	
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
		goClientName = words[1].toUpperCase();
		System.out.println("GO SERVER: Client " + goClientName + " connected");
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
		gameManager.goClientStateChanged(goClientHandler, goClientState);
	}
	
	@Override
	public void handleGameRequest() {
		System.out.println("GO SERVER: " + goClientName + " requested a game");
		System.out.println("GO SERVER: Waiting for clients to connect...");
		setGoClientState(GoClientState.GAME_REQUESTED);
	}

	@Override
	public void notifyOtherClientOfGameSettings(GoClientHandler opponent, String stoneColor, 
			String boardSize) {
		if (stoneColor.equals(General.WHITE)) {
			System.out.println("GO SERVER: " + opponent.getGoClientName() + 
					" plays with BLACK stones and " + goClientName + " plays with WHITE stones");
			opponent.sendMessage(Server.START + General.DELIMITER1 + 2 + General.DELIMITER1 + 
					General.BLACK + General.DELIMITER1 + boardSize + General.COMMAND_END);
			opponent.setBoardSize(boardSize);
			gameManager.startGame(opponent, goClientHandler);
			game = gameManager.getGame(goClientHandler);
		} else if (stoneColor.equals(General.BLACK)) {
			System.out.println("GO SERVER: " + goClientName  + " plays with BLACK stones and " + 
					opponent.getGoClientName() + " plays with WHITE stones");
			opponent.sendMessage(Server.START + General.DELIMITER1 + 2 + General.DELIMITER1 + 
					General.WHITE + General.DELIMITER1 + boardSize + General.COMMAND_END);
			gameManager.startGame(goClientHandler, opponent);
			game = gameManager.getGame(goClientHandler);
		}
	}
	
	@Override
	public void setBoardSize(String boardSize) {
		goClientHandler.setBoardSize(boardSize);
	}
	
	@Override
	public void confirmMove(String move) {
		//Send to game, let game check for validity
		game.confirmMove(move);
		
	}

}
