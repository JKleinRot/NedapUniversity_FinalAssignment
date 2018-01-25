package game.player;

import java.util.Observable;

import game.MoveChecker;
import game.MoveCheckerImpl;
import game.board.Board;
import game.board.stone.StoneColor;
import protocol.Protocol.General;
import protocol.Protocol.Server;
/**
 * Human player for Go.
 * @author janine.kleinrot
 */
public class HumanPlayer extends Observable implements Player {
	
	/** The board. */
	private Board board;
	
	/** The board for checking with the current situation. */
	private Board currentBoard;
	
	/** The previous board situation. */
	private Board previousBoard;
	
	/** The next board situation. */
	private Board nextBoard;
	
	/** The name of the player. */
	private String name;
	
	/** The stone color of the player. */
	private StoneColor stoneColor;
	
	/** Whether the move is valid. */
	private boolean isValidMove;
	
	/** The move checker. */
	private MoveChecker moveChecker;
	
	/** The check message. */
	private String checkMessage;
	
	/**
	 * Creates a human player with a given name and stone color.
	 * @param name
	 * 			The name of the human player.
	 * @param color
	 * 			The color of the stones of the human player.
	 */
	public HumanPlayer(String name, StoneColor color) {
		this.name = name;
		this.stoneColor = color;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public String getName() {
		return name;
	}
	
	public StoneColor getStoneColor() {
		return stoneColor;
	}
	
	public void setBoard(String boardSize) {
		board = new Board(Integer.parseInt(boardSize), true);
		previousBoard = board;
		nextBoard = board;
		currentBoard = board;
		moveChecker = new MoveCheckerImpl();
	}
	
	@Override
	public void processPreviousMove(String move, String previousPlayer) {
		if (!move.equals(Server.FIRST)) {
			if (previousPlayer.equals(name.toUpperCase())) {
				previousBoard = board;
				String[] moveCoordinates = move.split(General.DELIMITER2); 
				board.setStone(Integer.parseInt(moveCoordinates[0]), 
						Integer.parseInt(moveCoordinates[1]), 
						stoneColor);
				nextBoard = board;
				currentBoard = board;
				System.out.println("Liberties " + board.getStone(Integer.parseInt(moveCoordinates[0]), 
						Integer.parseInt(moveCoordinates[1])).getLiberties() + "");
			} else {
				previousBoard = board;
				String[] moveCoordinates = move.split(General.DELIMITER2); 
				board.setStone(Integer.parseInt(moveCoordinates[0]), 
						Integer.parseInt(moveCoordinates[1]), 
						stoneColor.other());
				nextBoard = board;
				currentBoard = board;
			}
		}
		
	}
	
	@Override
	public void determineMove() {
		setChanged();
		notifyObservers("Move requested");
	}

	@Override
	public void makeMove(String move) {
		String[] moveCoordinates = move.split(General.DELIMITER2);
		if (moveCoordinates.length != 2) {
			setChanged();
			notifyObservers("Invalid move input");
		} else {
			try {
				int moveX = Integer.parseInt(moveCoordinates[0]);
				int moveY = Integer.parseInt(moveCoordinates[1]);
				isValidMove = moveChecker.checkMove(moveX, moveY, stoneColor, currentBoard, 
						previousBoard, nextBoard);
				if (isValidMove) {
					setChanged();
					notifyObservers("Valid move");
				} else {
					checkMessage = moveChecker.getMoveViolations();
					handleCheckMessage(checkMessage);
				}
			} catch (NumberFormatException e) {
				setChanged();
				notifyObservers("Invalid move input");
			}
		}
	}

	private void handleCheckMessage(String aCheckMessage) {
		if (checkMessage.contains("Move")) {
			setChanged();
			notifyObservers("Move not on board");
		} else if (checkMessage.contains("Occupied")) {
			setChanged();
			notifyObservers("Occupied intersection");
		} else if (checkMessage.contains("Ko")) {
			setChanged();
			notifyObservers("Ko rule");
		}
	}
	
}
