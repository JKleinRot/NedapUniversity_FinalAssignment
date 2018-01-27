package game;

import java.util.ArrayList;
import java.util.List;

import client.handler.GoClientHandler;
import game.board.Board;
import game.board.Intersection;
import game.board.Position;
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
	
	/** Wheter a move is made. */
	private boolean isMoveMade;
	
	/** Whether the move is valid. */
	private boolean isValidMove;
	
	/** The move represented as a string. */
	private String move;
	
	/** The previous move represented as a string. */
	private String previousMove;
	
	/** The MoveChecker. */
	private MoveChecker moveChecker;
	
	private int blackScore;
	
	private int whiteScore;
	
	/**
	 * Creates a new Game.
	 * @param firstGoClientHandler
	 * 			The GoClientHandler communicating with the GoClient playing with black.
	 * @param secondGoClientHandler
	 * 			The GoClientHandler communicating with the GoClient playing with white.
	 */
	public GameImpl(GoClientHandler firstGoClientHandler, GoClientHandler secondGoClientHandler) {
		this.firstGoClientHandler = firstGoClientHandler;
		this.secondGoClientHandler = secondGoClientHandler;
		this.firstGoClientHandler.getGoClientHandlerActor().setGame(this);
		this.secondGoClientHandler.getGoClientHandlerActor().setGame(this);
		board = new Board(Integer.parseInt(firstGoClientHandler.getBoardSize()), false);
		previousBoard = board;
		nextBoard = board;
		isGameOver = false;
		isMoveMade = false;
		numberOfMoves = 0;
		moveChecker = new MoveCheckerImpl();
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
	public synchronized void confirmMove(String moveMade) {
		if (!moveMade.equals(Client.PASS)) {
			String[] moveCoordinates = moveMade.split(General.DELIMITER2);
			int moveX = Integer.parseInt(moveCoordinates[0]);
			int moveY = Integer.parseInt(moveCoordinates[1]);
			if (numberOfMoves % 2 == 1 && numberOfMoves != 1) {
				isValidMove = moveChecker.checkMove(moveX, moveY, StoneColor.BLACK, board, 
						previousBoard, nextBoard); 
			} else if (numberOfMoves % 2 == 0) {
				isValidMove = moveChecker.checkMove(moveX, moveY, StoneColor.WHITE, board, 
						previousBoard, nextBoard); 
			} else if (numberOfMoves == 1) {
				isValidMove = true;
			}
			if (isValidMove) {
				this.move = moveMade;
				previousBoard = board;
				if (numberOfMoves % 2 == 1) {
					board.setStone(moveX, moveY, StoneColor.BLACK);
				} else {
					board.setStone(moveX, moveY, StoneColor.WHITE);
				}
				nextBoard = board; 
				notifyAll();
			} else {
				System.out.println("Invalid move found in Go server");
			}
		} else {
			this.move = moveMade;
			if (move.equals(previousMove)) {
				calculateWinner();
			} else {
				notifyAll();
			}
		}
		this.previousMove = moveMade;
	}
	
	/**
	 * Calculates the winner after both players passed in adjacent moves.
	 */
	private void calculateWinner() {
		board.calculateWinner();
		blackScore = board.getBlackScore();
		whiteScore = board.getWhiteScore();
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
	}

	@Override
	public List<GoClientHandler> getGoClientHandlers() {
		List<GoClientHandler> goClientHandlers = new ArrayList<GoClientHandler>();
		goClientHandlers.add(firstGoClientHandler);
		goClientHandlers.add(secondGoClientHandler);
		return goClientHandlers;
	}
}
