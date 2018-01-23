package game;

import game.board.Board;
import game.board.stone.StoneColor;

/**
 * Checks if a move is valid.
 * @author janine.kleinrot
 */
public interface MoveChecker {

	public boolean checkMove(int moveX, int moveY, StoneColor stoneColor, Board board, 
			Board previousBoard, Board nextBoard);
	
	public String getMoveViolations();
}
