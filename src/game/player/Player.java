package game.player;

import game.board.Board;

public interface Player {
	
	public Board getBoard();
	
	public void setBoard(String boardSize);
	
	public void makeMove(String move);
	
	public void processPreviousMove(String move, String previousPlayer);
	
	public void determineMove();
	
	public void checkMove(int moveX, int moveY);

}
