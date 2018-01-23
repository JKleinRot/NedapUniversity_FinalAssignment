package game;

import game.board.Board;

/**
 * Checks if a move is valid.
 * @author janine.kleinrot
 */
public interface MoveChecker {

	public boolean checkMove(int moveX, int moveY, Board board, Board previousBoard, 
			Board nextBoard);
	
	public String getMoveViolations();
}
