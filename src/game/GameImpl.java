package game;

import java.util.ArrayList;
import java.util.List;

import client.GoClientState;
import client.handler.GoClientHandler;
import game.board.Board;
import game.board.stone.StoneColor;
import protocol.Protocol.Client;
import protocol.Protocol.General;
import protocol.Protocol.Server;

/**
 * A game of Go.
 * @author janine.kleinrot
 */
public class GameImpl implements Game {

	/** The GoClientHandler communicating with the GoClient playing with black. */
	private GoClientHandler firstGoClientHandler;
	
	/** The GoClientHandler communicating with the GoClient playing with white. */
	private GoClientHandler secondGoClientHandler;
	
	/** The game manager. */
	private GoClientStateListener gameManager;
	
	/** The board. */
	private Board board;
	
	/** The previous board situation. */ 
	private Board previousBoard;
	
	/** The next board situation. */
	private Board nextBoard;
	
	/** Whether the game is over. */
	private boolean isGameOver;
	
	/** The number of moves made in the game. */
	private int numberOfMoves;
	
	/** Whether a move is made. */
	private boolean isMoveMade;
	
	/** Whether the move is valid. */
	private boolean isValidMove;
	
	/** The move represented as a string. */
	private String move;
	
	/** The previous move represented as a string. */
	private String previousMove;
	
	/** The MoveChecker. */
	private MoveChecker moveChecker;
	
	/** The score of the player with black stones. */
	private int blackScore;
	
	/** The score of the player with white stones. */
	private int whiteScore;
	
	/**	The maximum amount of moves with black stones. */
	private int maxBlackStones;
	
	/**	The maximum amount of moves with white stones. */
	private int maxWhiteStones;
	
	/**
	 * Create a new Game.
	 * @param firstGoClientHandler
	 * 			The GoClientHandler communicating with the GoClient playing with black.
	 * @param secondGoClientHandler
	 * 			The GoClientHandler communicating with the GoClient playing with white.
	 * @param gameManager
	 * 			The gameManager.
	 */
	public GameImpl(GoClientHandler firstGoClientHandler, GoClientHandler secondGoClientHandler, 
			GoClientStateListener gameManager) {
		this.firstGoClientHandler = firstGoClientHandler;
		this.secondGoClientHandler = secondGoClientHandler;
		this.gameManager = gameManager;
		this.firstGoClientHandler.getGoClientHandlerActor().setGame(this);
		this.secondGoClientHandler.getGoClientHandlerActor().setGame(this);
		board = new Board(Integer.parseInt(firstGoClientHandler.getBoardSize()), false);
		previousBoard = board.copy();
		nextBoard = board.copy();
		isGameOver = false;
		isMoveMade = false;
		numberOfMoves = 0;
		moveChecker = new MoveCheckerImpl();
		maxBlackStones = 0;
		maxWhiteStones = 0;
	}

	@Override
	public synchronized void run() {
		while (!isGameOver) {
			if (numberOfMoves == 0) {
				firstGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
						firstGoClientHandler.getGoClientName() + General.DELIMITER1 + 
						Server.FIRST + General.DELIMITER1 + 
						firstGoClientHandler.getGoClientName() + General.COMMAND_END);
				numberOfMoves++;
			}
			while (!isMoveMade) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (numberOfMoves % 2 == 1) {
					firstGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
							firstGoClientHandler.getGoClientName() + General.DELIMITER1 + 
							move + General.DELIMITER1 + secondGoClientHandler.getGoClientName() + 
							General.COMMAND_END);
					secondGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
							firstGoClientHandler.getGoClientName() + General.DELIMITER1 + 
							move + General.DELIMITER1 + secondGoClientHandler.getGoClientName() + 
							General.COMMAND_END);
					numberOfMoves++;
				} else if (numberOfMoves % 2 == 0) {
					firstGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
							secondGoClientHandler.getGoClientName() + General.DELIMITER1 + 
							move + General.DELIMITER1 + firstGoClientHandler.getGoClientName() + 
							General.COMMAND_END);
					secondGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
							secondGoClientHandler.getGoClientName() + General.DELIMITER1 + 
							move + General.DELIMITER1 + firstGoClientHandler.getGoClientName() + 
							General.COMMAND_END);
					numberOfMoves++;
				}
			}
		}
		
	}

	@Override
	public synchronized void confirmMove(String moveMade, GoClientHandler goClientHandler) {
		if (!isGameOver && maxBlackStones != (board.getSize() * board.getSize() / 2) && 
				maxWhiteStones != (board.getSize() * board.getSize() / 2)) {
			if ((numberOfMoves % 2 == 1 && goClientHandler.equals(firstGoClientHandler)) || 
					(numberOfMoves % 2 == 0 && goClientHandler.equals(secondGoClientHandler))) {
				if (!moveMade.equals(Client.PASS)) {
					if (goClientHandler.equals(firstGoClientHandler)) {
						maxBlackStones++;
					} else if (goClientHandler.equals(secondGoClientHandler)) {
						maxWhiteStones++;
					}
					String[] moveCoordinates = moveMade.split(General.DELIMITER2);
					try {
						int moveX = Integer.parseInt(moveCoordinates[0]);
						int moveY = Integer.parseInt(moveCoordinates[1]);
						if (numberOfMoves % 2 == 1) {
							isValidMove = moveChecker.checkMove(moveX, moveY, StoneColor.BLACK, 
									board, previousBoard, nextBoard); 
						} else if (numberOfMoves % 2 == 0) {
							isValidMove = moveChecker.checkMove(moveX, moveY, StoneColor.WHITE, 
									board, previousBoard, nextBoard); 
						}
						if (isValidMove) {
							this.move = moveMade;
							previousBoard = board.copy();
							if (numberOfMoves % 2 == 1) {
								board.setStone(moveX, moveY, StoneColor.BLACK);
							} else {
								board.setStone(moveX, moveY, StoneColor.WHITE);
							}
							nextBoard = board.copy(); 
							notifyAll();
						} else {
							goClientHandler.sendMessage(Server.ERROR + General.DELIMITER1 + 
									Server.INVALID + General.DELIMITER1 + "The move " + moveMade + 
									" was invalid" + General.COMMAND_END);
						}
					} catch (NumberFormatException e) {
						goClientHandler.sendMessage(Server.ERROR + General.DELIMITER1 + 
								Server.INVALID + General.DELIMITER1 + "The move " + moveMade + 
								" was invalid" + General.COMMAND_END);
					}
				} else {
					this.move = moveMade;
					if (move.equals(previousMove)) {
						calculateWinner();
						System.out.println("GO SERVER: Game ended between " + 
								firstGoClientHandler.getGoClientName().toUpperCase() + " and " + 
								secondGoClientHandler.getGoClientName().toUpperCase());
					} else {
						notifyAll();
					}
				}
				this.previousMove = moveMade;
			}
		} else {
			calculateWinner();
			System.out.println("GO SERVER: Game ended between " + 
					firstGoClientHandler.getGoClientName().toUpperCase() + " and " + 
					secondGoClientHandler.getGoClientName().toUpperCase());
		}
	}

	/**
	 * Calculate the winner after both players passed in adjacent moves.
	 */
	private void calculateWinner() {
		board.calculateWinner();
		if (numberOfMoves == 2) {
			blackScore = 0;
			whiteScore = 0;
		} else {
			blackScore = board.getBlackScore();
			whiteScore = board.getWhiteScore();
		}
		if (blackScore >= whiteScore) {
			firstGoClientHandler.sendMessage(Server.ENDGAME + General.DELIMITER1 + 
					Server.FINISHED + General.DELIMITER1 + 
					firstGoClientHandler.getGoClientName() + General.DELIMITER1 + blackScore + 
					General.DELIMITER1 + secondGoClientHandler.getGoClientName() + 
					General.DELIMITER1 + whiteScore + General.COMMAND_END);
			secondGoClientHandler.sendMessage(Server.ENDGAME + General.DELIMITER1 + 
					Server.FINISHED + General.DELIMITER1 + 
					firstGoClientHandler.getGoClientName() + General.DELIMITER1 + blackScore + 
					General.DELIMITER1 + secondGoClientHandler.getGoClientName() + 
					General.DELIMITER1 + whiteScore + General.COMMAND_END);
		} else if (blackScore < whiteScore) {
			firstGoClientHandler.sendMessage(Server.ENDGAME + General.DELIMITER1 + 
					Server.FINISHED + General.DELIMITER1 + 
					secondGoClientHandler.getGoClientName() + General.DELIMITER1 + whiteScore + 
					General.DELIMITER1 + firstGoClientHandler.getGoClientName() + 
					General.DELIMITER1 + blackScore + General.COMMAND_END);
			secondGoClientHandler.sendMessage(Server.ENDGAME + General.DELIMITER1 + 
					Server.FINISHED + General.DELIMITER1 + 
					secondGoClientHandler.getGoClientName() + General.DELIMITER1 + whiteScore + 
					General.DELIMITER1 + firstGoClientHandler.getGoClientName() + 
					General.DELIMITER1 + blackScore + General.COMMAND_END);
		}
		firstGoClientHandler.setGoClientState(GoClientState.CONNECTED);
		gameManager.goClientStateChanged(firstGoClientHandler, GoClientState.CONNECTED);
		secondGoClientHandler.setGoClientState(GoClientState.CONNECTED);
		gameManager.goClientStateChanged(secondGoClientHandler, GoClientState.CONNECTED);
		isGameOver = true;
	}

	@Override
	public List<GoClientHandler> getGoClientHandlers() {
		List<GoClientHandler> goClientHandlers = new ArrayList<GoClientHandler>();
		goClientHandlers.add(firstGoClientHandler);
		goClientHandlers.add(secondGoClientHandler);
		return goClientHandlers;
	}

	@Override
	public void endAbortedGame(GoClientHandler goClientHandler) {
		calculateWinnerAbortedGame(goClientHandler);
		System.out.println("GO SERVER: Game ended between " + 
				firstGoClientHandler.getGoClientName().toUpperCase() + " and " + 
				secondGoClientHandler.getGoClientName().toUpperCase());
		isGameOver = true;
	}

	/**
	 * Calculate the winner if one of the players aborted the game.
	 * @param goClientHandler
	 * 			The GoClientHandler of the GoClient ending the game.
	 */
	private void calculateWinnerAbortedGame(GoClientHandler goClientHandler) {
		board.calculateWinner();
		blackScore = board.getBlackScore();
		whiteScore = board.getWhiteScore();
		if (goClientHandler.equals(secondGoClientHandler)) {
			whiteScore = 0;
			firstGoClientHandler.sendMessage(Server.ENDGAME + General.DELIMITER1 + 
					Server.ABORTED + General.DELIMITER1 + 
					firstGoClientHandler.getGoClientName() + General.DELIMITER1 + blackScore + 
					General.DELIMITER1 + secondGoClientHandler.getGoClientName() + 
					General.DELIMITER1 + whiteScore + General.COMMAND_END);
			secondGoClientHandler.sendMessage(Server.ENDGAME + General.DELIMITER1 + 
					Server.ABORTED + General.DELIMITER1 + 
					firstGoClientHandler.getGoClientName() + General.DELIMITER1 + blackScore + 
					General.DELIMITER1 + secondGoClientHandler.getGoClientName() + 
					General.DELIMITER1 + whiteScore + General.COMMAND_END);
		} else if (goClientHandler.equals(firstGoClientHandler)) {
			blackScore = 0;
			firstGoClientHandler.sendMessage(Server.ENDGAME + General.DELIMITER1 + 
					Server.ABORTED + General.DELIMITER1 + 
					secondGoClientHandler.getGoClientName() + General.DELIMITER1 + whiteScore + 
					General.DELIMITER1 + firstGoClientHandler.getGoClientName() + 
					General.DELIMITER1 + blackScore + General.COMMAND_END);
			secondGoClientHandler.sendMessage(Server.ENDGAME + General.DELIMITER1 + 
					Server.ABORTED + General.DELIMITER1 + 
					secondGoClientHandler.getGoClientName() + General.DELIMITER1 + whiteScore + 
					General.DELIMITER1 + firstGoClientHandler.getGoClientName() + 
					General.DELIMITER1 + blackScore + General.COMMAND_END);
		}
		firstGoClientHandler.setGoClientState(GoClientState.CONNECTED);
		gameManager.goClientStateChanged(firstGoClientHandler, GoClientState.CONNECTED);
		secondGoClientHandler.setGoClientState(GoClientState.CONNECTED);
		gameManager.goClientStateChanged(secondGoClientHandler, GoClientState.CONNECTED);
	}

	@Override
	public void endGameExit(GoClientHandler goClientHandler) {
		calculateWinnerExitGame(goClientHandler);
		System.out.println("GO SERVER: Game ended between " + 
				firstGoClientHandler.getGoClientName().toUpperCase() + " and " + 
				secondGoClientHandler.getGoClientName().toUpperCase());
		isGameOver = true;
	}

	/**
	 * Calculate the winner if one of the players left the GoServer.
	 * @param goClientHandler
	 * 			The GoClientHandler of the GoClient ending the game.
	 */
	private void calculateWinnerExitGame(GoClientHandler goClientHandler) {
		board.calculateWinner();
		blackScore = board.getBlackScore();
		whiteScore = board.getWhiteScore();
		if (goClientHandler.equals(firstGoClientHandler)) {
			blackScore = 0;
			secondGoClientHandler.sendMessage(Server.ENDGAME + General.DELIMITER1 + 
					Server.ABORTED + General.DELIMITER1 + 
					secondGoClientHandler.getGoClientName() + General.DELIMITER1 + whiteScore + 
					General.DELIMITER1 + firstGoClientHandler.getGoClientName() + 
					General.DELIMITER1 + blackScore + General.COMMAND_END);
			secondGoClientHandler.setGoClientState(GoClientState.CONNECTED);
			gameManager.goClientStateChanged(secondGoClientHandler, GoClientState.CONNECTED);
		} else if (goClientHandler.equals(secondGoClientHandler)) {
			whiteScore = 0;
			firstGoClientHandler.sendMessage(Server.ENDGAME + General.DELIMITER1 + 
					Server.ABORTED + General.DELIMITER1 + 
					firstGoClientHandler.getGoClientName() + General.DELIMITER1 + blackScore + 
					General.DELIMITER1 + secondGoClientHandler.getGoClientName() + 
					General.DELIMITER1 + whiteScore + General.COMMAND_END);
			firstGoClientHandler.setGoClientState(GoClientState.CONNECTED);
			gameManager.goClientStateChanged(firstGoClientHandler, GoClientState.CONNECTED);
		}
	}
}
