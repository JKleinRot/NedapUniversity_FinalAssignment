package game.player;

import game.board.Board;
import game.board.gui.GoGUIIntegrator;
import game.board.stone.StoneColor;

/**
 * A player for Go.
 * @author janine.kleinrot
 */
public interface Player {
	
	/**
	 * Return the board.
	 * @return
	 * 			The board.
	 */
	public Board getBoard();
	
	/**
	 * Set the board to the provided size.
	 * @param boardSize
	 * 			The board size.
	 */
	public void setBoard(String boardSize);
	
	/**
	 * Set the GoGUI.
	 * @param boardSize
	 * 			The board size.
	 */
	public void setGoGUI(String boardSize);
	
	/**
	 * Adjust the current board to the provided size.
	 * @param boardSize
	 * 			The board size.
	 */
	public void adjustBoard(String boardSize, GoGUIIntegrator goGUI);
	
	/**
	 * Make a move if the move is valid.
	 * @param move
	 * 			The move.
	 */
	public void makeMove(String move);
	
	/**
	 * Process the previous valid move by updating the board and the GoGUI.
	 * @param move
	 * 			The move.
	 * @param previousPlayer
	 * 			The player that made the move.
	 */
	public void processPreviousMove(String move, String previousPlayer);
	
	/**
	 * Determine the next move.
	 */
	public void determineMove();
	
	/** 
	 * Show the player a hint.
	 */
	public void provideHint();

	/**
	 * Set the move time of the computer player.
	 * @param moveTime
	 * 			The move time.
	 */
	public void setMoveTime(String moveTime);

	/**
	 * Return the name of the player.
	 * @return
	 * 			The name.
	 */
	public String getName();
	
	/**
	 * Return the stone color.
	 * @return
	 * 			The stone color.
	 */
	public StoneColor getStoneColor();
	
	/**
	 * Return the move time of the computer player.
	 * @return
	 * 			The move time in seconds.
	 */
	public int getMoveTime();
}
