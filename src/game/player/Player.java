package game.player;

import game.board.Board;

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
	 * Adjust the current board to the provided size.
	 * @param boardSize
	 * 			The board size.
	 */
	public void adjustBoard(String boardSize);
	
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
	

}
