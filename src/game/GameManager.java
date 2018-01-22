package game;

import java.util.ArrayList;
import java.util.List;

import client.handler.GoClientHandler;
import protocol.Protocol.General;
import protocol.Protocol.Server;
import client.GoClientState;
import client.GoClientStateListener;

/**
 * Manages the initiation of a game for GoClients that requested a game.
 * @author janine.kleinrot
 *
 */
public class GameManager implements GoClientStateListener {

	/**	The GoClientHandlers corresponding to the GoClients that requested a game. */
	private List<GoClientHandler> goClientHandlersGameRequested;
	
	/**	The GoClientHandlers corresponding to the GoClients that play a game. */
	private List<GoClientHandler> goClientHandlersPlayingGame;
	
	private List<Game> goGames;
	
	/** A game of Go. */
	private Game game;
	
	/**
	 * Creates a GameManager.
	 */
	public GameManager() {
		goClientHandlersGameRequested = new ArrayList<GoClientHandler>();
		goClientHandlersPlayingGame = new ArrayList<GoClientHandler>();
		goGames = new ArrayList<Game>();
	}
	
	@Override
	public void goClientStateChanged(GoClientHandler goClientHandler, GoClientState goClientState) {
		if (goClientState == GoClientState.GAME_REQUESTED) {
			goClientHandlersGameRequested.add(goClientHandler);
			if (goClientHandlersGameRequested.size() % 2 == 0 && 
					!goClientHandlersGameRequested.isEmpty()) {
				getGameSettings(goClientHandlersGameRequested.get(0), 
						goClientHandlersGameRequested.get(1));
//				game = new GameImpl(goClientHandlersGameRequested.get(0), 
//				goClientHandlersGameRequested.get(1));
//				Thread gameThread = new Thread(game);
//				gameThread.start();
			}
		} else if (goClientState == GoClientState.PLAYING_GAME) {
			goClientHandlersGameRequested.remove(goClientHandler);
			goClientHandlersPlayingGame.add(goClientHandler);
		} else {
			if (!goClientHandlersGameRequested.isEmpty()) {
				if (goClientHandlersGameRequested.contains(goClientHandler)) {
					goClientHandlersGameRequested.remove(goClientHandler);
				}
			} else if (!goClientHandlersPlayingGame.isEmpty()) {
				if (goClientHandlersPlayingGame.contains(goClientHandler)) {
					goClientHandlersPlayingGame.remove(goClientHandler);
				}
			}
		}
	}

	@Override
	public void getGameSettings(GoClientHandler firstGoClientHandler, 
			GoClientHandler secondGoClientHandler) {
		firstGoClientHandler.setGoClientState(GoClientState.PLAYING_GAME);
		goClientStateChanged(firstGoClientHandler, GoClientState.PLAYING_GAME);
		secondGoClientHandler.setGoClientState(GoClientState.PLAYING_GAME);
		goClientStateChanged(secondGoClientHandler, GoClientState.PLAYING_GAME);
		firstGoClientHandler.setOpponent(secondGoClientHandler);
		secondGoClientHandler.setOpponent(firstGoClientHandler);
		System.out.println("GO SERVER: Waiting for " + 
				firstGoClientHandler.getGoClientName() + 
				" to set game settings...");
		System.out.println("GO SERVER: Waiting for clients to connect...");
		firstGoClientHandler.sendMessage(Server.START + General.DELIMITER1 + 2 + 
				General.COMMAND_END);
	}
	
	@Override 
	public void startGame(GoClientHandler firstGoClientHandler, 
			GoClientHandler secondGoClientHandler) {
		game = new GameImpl(firstGoClientHandler, secondGoClientHandler);
		System.out.println("GO SERVER: Game started between " + 
				firstGoClientHandler.getGoClientName() + " and " + 
				secondGoClientHandler.getGoClientName());
		Thread gameThread = new Thread(game);
		gameThread.start();
		
	}
	
	@Override
	public Game getGame(GoClientHandler goClientHandler) {
		for (int i = 0; i < goGames.size(); i++) {
			if (goGames.get(i).getGoClientHandlers().contains(goClientHandler)) {
				return game;
			}
		}
		return null;
	}

}
