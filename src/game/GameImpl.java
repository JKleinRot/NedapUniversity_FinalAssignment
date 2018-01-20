package game;

import client.handler.GoClientHandler;
import game.board.Board;

/**
 * A game of Go.
 * @author janine.kleinrot
 */
public class GameImpl implements Game {

	private GoClientHandler firstGoClientHandler;
	
	private GoClientHandler secondGoClientHandler;
	
	private Board board;
	
	public GameImpl(GoClientHandler firstGoClientHandler, GoClientHandler secondGoClientHandler) {
		this.firstGoClientHandler = firstGoClientHandler;
		this.secondGoClientHandler = secondGoClientHandler;
		board = new Board(Integer.parseInt(firstGoClientHandler.getBoardSize()));
	}

	@Override
	public void run() {
		System.out.println("GAME started");
		
	}
}
