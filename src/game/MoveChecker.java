package game;

import game.board.Board;
import game.board.stone.StoneColor;

/**
 * Check if a move is valid.
 * @author janine.kleinrot
 */
public interface MoveChecker {

	/**
	 * Check if a move is valid.
	 * @param moveX
	 * 			The x coordinate of the move.
	 * @param moveY
	 * 			The y coordinate of the move.
	 * @param stoneColor
	 * 			The stone color of the move.
	 * @param board
	 * 			The current board situation.
	 * @param previousBoard
	 * 			The previous board situation.
	 * @param nextBoard
	 * 			The next board situation.
	 * @return
	 * 			True if the move was valid.
	 */
	public boolean checkMove(int moveX, int moveY, StoneColor stoneColor, Board board, 
			Board previousBoard, Board nextBoard);
	
	/**
	 * Return the string containing the concatenated messages from the different move checks.
	 * @return
	 * 			The check messages.
	 */
	public String getMoveViolations();
}
