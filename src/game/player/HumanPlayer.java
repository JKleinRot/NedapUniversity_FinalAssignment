package game.player;

import java.util.Observable;

import game.MoveChecker;
import game.MoveCheckerImpl;
import game.board.Board;
import game.board.stone.StoneColor;
import gui.GoGUIIntegrator;
import protocol.Protocol.General;
import protocol.Protocol.Server;
/**
 * Human player for Go.
 * @author janine.kleinrot
 */
public class HumanPlayer extends Observable implements Player {
	
	private Board board;
	
	private Board previousBoard;
	
	private Board nextBoard;
	
	private GoGUIIntegrator goGUI;
	
	private String name;
	
	private StoneColor stoneColor;
	
	private boolean isWhite;
	
	private boolean isValidMove;
	
	private int numberOfMoves;
	
	private MoveChecker moveChecker;
	
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
		if (stoneColor.equals(StoneColor.WHITE)) {
			isWhite = true;
		} else {
			isWhite = false;
		}
		numberOfMoves = 0;
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
		goGUI = new GoGUIIntegrator(true, true, Integer.parseInt(boardSize));
		goGUI.startGUI();
		board = new Board(Integer.parseInt(boardSize));
		previousBoard = board;
		nextBoard = board;
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
				goGUI.addStone(Integer.parseInt(moveCoordinates[0]), 
						Integer.parseInt(moveCoordinates[1]), isWhite);
				nextBoard = board;
			} else {
				previousBoard = board;
				String[] moveCoordinates = move.split(General.DELIMITER2); 
				board.setStone(Integer.parseInt(moveCoordinates[0]), 
						Integer.parseInt(moveCoordinates[1]), 
						stoneColor.other());
				goGUI.addStone(Integer.parseInt(moveCoordinates[0]), 
						Integer.parseInt(moveCoordinates[1]), !isWhite);
				nextBoard = board;
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
				isValidMove = moveChecker.checkMove(moveX, moveY, stoneColor, board, 
						previousBoard, nextBoard);
				if (isValidMove) {
					numberOfMoves++;
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

	private void handleCheckMessage(String checkMessage) {
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
	
//	@Override
//	public void checkMove(int moveX, int moveY) {
//		if (moveX >= board.getSize() || moveY >= board.getSize()) {
//			setChanged();
//			notifyObservers("Move not on board");
//		} else {
//			if (board.getIntersection(moveX, moveY).isOccupied()) {
//				setChanged();
//				notifyObservers("Occupied intersection");
//			} else {
//				nextBoard.setStone(moveX, moveY, stoneColor);
//				for (int x = 0; x < board.getSize(); x++) {
//					for (int y = 0; y < board.getSize(); y++) {
//						if (previousBoard.getIntersection(x, y).isOccupied() != 
//								nextBoard.getIntersection(x, y).isOccupied()) {
//							isValidMove = true;
//							return;
//						} else if (previousBoard.getIntersection(x, y).isOccupied() && 
//								nextBoard.getIntersection(x, y).isOccupied()) {
//							if (!previousBoard.getStone(x, y).getColor().equals(
//									nextBoard.getStone(x, y).getColor())) {
//								isValidMove = true;
//								return;
//							}
//						} 
//					}
//				}
//				if (numberOfMoves == 0) {
//					isValidMove = true;
//					return;
//				}
//				setChanged();
//				notifyObservers("Ko rule");
//			}
//		}
//	}
	
}
