package game;

import game.board.Board;
import game.board.stone.StoneColor;

/**
 * Checks if a move is valid.
 * @author janine.kleinrot
 */
public class MoveCheckerImpl implements MoveChecker {
	
	/** The current board situation. */
	private Board board;
	
	/** The previous board situation. */
	private Board previousBoard;
	
	/** The next board situation. */
	private Board nextBoard;
	
	/** The stone color of the move. */
	private StoneColor stoneColor;
	
	/** The check message. */
	private String checkMessage;
	
	/**
	 * Creates a new MoveCheckerImpl.
	 */
	public MoveCheckerImpl() {

	}
	
	@Override
	public boolean checkMove(int moveX, int moveY, StoneColor aStoneColor, Board aBoard, 
			Board aPreviousBoard, Board aNextBoard) {
		this.board = aBoard;
		this.previousBoard = aPreviousBoard;
		this.nextBoard = aNextBoard;
		this.stoneColor = aStoneColor;
		checkMessage = checkOutOfRange(moveX, moveY);
		if (checkMessage.contains("Move")) {
			return false;
		}
		checkMessage = checkMessage.concat(checkIsOccupied(moveX, moveY));
		if (checkMessage.contains("Occupied")) {
			return false;
		}
		checkMessage = checkMessage.concat(checkKoRule(moveX, moveY));
		if (checkMessage.contains("Ko")) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Checks if the move is out of range of the board.
	 * @param moveX
	 * 			The x coordinate of the move.
	 * @param moveY
	 * 			The y coordinate of the move.
	 * @return
	 * 			A message describing the possible error.
	 */
	private String checkOutOfRange(int moveX, int moveY) {
		if (moveX >= board.getSize() || moveY >= board.getSize()) {
			return "Move not on board ";
		} else {
			return "";
		}
	}
	
	/**
	 * Checks if the intersection of the move is already occupied.
	 * @param moveX
	 * 			The x coordinate of the move.
	 * @param moveY
	 * 			The y coordinate of the move.
	 * @return
	 * 			A message describing the possible error.
	 */
	private String checkIsOccupied(int moveX, int moveY) {
		if (board.getIntersection(moveX, moveY).isOccupied()) {
			return "Occupied intersection ";
		} else {
			return "";
		}
	}
	
	/**
	 * Checks if the move violates the Ko rule.
	 * @param moveX
	 * 			The x coordinate of the move.
	 * @param moveY
	 * 			The y coordinate of the move.
	 * @return
	 * 			A message describing the possible error.
	 */
	private String checkKoRule(int moveX, int moveY) {
		nextBoard.setStone(moveX, moveY, stoneColor);
		int equalsCount = 0;
		for (int x = 0; x < board.getSize(); x++) {
			for (int y = 0; y < board.getSize(); y++) {
				if (previousBoard.getIntersection(x, y).isOccupied() == 
						nextBoard.getIntersection(x, y).isOccupied() && 
						previousBoard.getIntersection(x, y).isOccupied()) {
					if (previousBoard.getStone(x, y).getColor().equals(
							nextBoard.getStone(x, y).getColor())) {
						equalsCount++;
					} else if (!previousBoard.getIntersection(x, y).isOccupied()) {
						equalsCount++;
					}
				}
			}
		}
		if (equalsCount == board.getSize() * board.getSize()) {
			return "Ko rule";
		} else {
			return "";
		}
	}
	
	@Override
	public String getMoveViolations() {
		return checkMessage;
	}
}
