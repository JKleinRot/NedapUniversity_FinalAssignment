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
	
	/** The game manager. */
	private GoClientStateListener gameManager;
	
	/** The name of the client. */
	private String goClientName;
	
	/** The game. */
	private Game game;
	
	/** Whether the name of the GoClient is valid. */
	private boolean isNameValid;
	
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
		isNameValid = goClientHandler.getGoServer().addGoClientHandler(goClientHandler);
		if (isNameValid) {
			System.out.println("GO SERVER: Client " + goClientName + " connected");
			goClientHandler.sendMessage(Server.NAME + General.DELIMITER1 + name + General.DELIMITER1 + 
					Server.VERSION + General.DELIMITER1 +  Server.VERSIONNO + 
					General.DELIMITER1 + Server.EXTENSIONS + General.DELIMITER1 + 0 + 
					General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 
					0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
					General.DELIMITER1 + 0 + General.COMMAND_END);
			setGoClientState(GoClientState.CONNECTED);
			System.out.println("GO SERVER: Waiting for clients to connect...");
		} else {
			goClientHandler.sendMessage(Server.ERROR + General.DELIMITER1 + Server.NAMETAKEN + General.DELIMITER1 + "The name " + goClientName.toUpperCase() + " is already taken" + General.COMMAND_END);
		}
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
					General.BLACK + General.DELIMITER1 + boardSize + General.DELIMITER1 + 
					goClientName + General.DELIMITER1 + opponent.getGoClientName() + 
					General.COMMAND_END);
			goClientHandler.sendMessage(Server.START + General.DELIMITER1 + 2 + General.DELIMITER1 + 
					General.WHITE + General.DELIMITER1 + boardSize + General.DELIMITER1 + 
					goClientName + General.DELIMITER1 + opponent.getGoClientName() + 
					General.COMMAND_END);
			opponent.setBoardSize(boardSize);
			gameManager.startGame(opponent, goClientHandler);
		} else if (stoneColor.equals(General.BLACK)) {
			System.out.println("GO SERVER: " + goClientName  + " plays with BLACK stones and " + 
					opponent.getGoClientName() + " plays with WHITE stones");
			opponent.sendMessage(Server.START + General.DELIMITER1 + 2 + General.DELIMITER1 + 
					General.WHITE + General.DELIMITER1 + boardSize + General.DELIMITER1 + 
					goClientName + General.DELIMITER1 + opponent.getGoClientName() + 
					General.COMMAND_END);
			goClientHandler.sendMessage(Server.START + General.DELIMITER1 + 2 + General.DELIMITER1 + 
					General.BLACK + General.DELIMITER1 + boardSize + General.DELIMITER1 + 
					goClientName + General.DELIMITER1 + opponent.getGoClientName() + 
					General.COMMAND_END);
			opponent.setBoardSize(boardSize);
			gameManager.startGame(goClientHandler, opponent);
		}
	}
	
	@Override
	public void setBoardSize(String boardSize) {
		goClientHandler.setBoardSize(boardSize);
	}
	
	@Override
	public void confirmMove(String move, GoClientHandler goClientHandler) {
		game.confirmMove(move, goClientHandler);
		
	}
	
	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public void endAbortedGame() {
		if (game != null) {
			game.endAbortedGame(goClientHandler);
		}
	}
	
	@Override
	public void endConnection() {
		if (game != null) {
			game.endGameExit(goClientHandler);
		}
		goClientHandler.setGoClientState(GoClientState.UNCONNECTED);
		goClientHandler.getGoServer().removeGoClientHandler(goClientHandler);
		Thread.interrupted();
	}

}
