package game;

import client.handler.GoClientHandler;
import game.board.Board;
import protocol.Protocol.General;
import protocol.Protocol.Server;

/**
 * A game of Go.
 * @author janine.kleinrot
 */
public class GameImpl implements Game {

	private GoClientHandler firstGoClientHandler;
	
	private GoClientHandler secondGoClientHandler;
	
	private Board board;
	
	private boolean isGameOver;
	
	private int numberOfMoves;
	
	private boolean isMoveMade;
	
	public GameImpl(GoClientHandler firstGoClientHandler, GoClientHandler secondGoClientHandler) {
		this.firstGoClientHandler = firstGoClientHandler;
		this.secondGoClientHandler = secondGoClientHandler;
		board = new Board(Integer.parseInt(firstGoClientHandler.getBoardSize()));
		isGameOver = false;
		isMoveMade = false;
		numberOfMoves = 0;
	}

	@Override
	public void run() {
		while (!isGameOver) {
			if (numberOfMoves == 0) {
				secondGoClientHandler.sendMessage("hoi\n");
				firstGoClientHandler.sendMessage("hoi1\n");
				firstGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
						firstGoClientHandler.getGoClientName() + General.DELIMITER1 + 
						Server.FIRST + General.DELIMITER1 + 
						firstGoClientHandler.getGoClientName() + General.COMMAND_END);
				numberOfMoves++;
			}
//			while (!isMoveMade) {
//				try {
//					wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				
//			}
		}
		
	}
//	
//	@Override
//	public void confirmMove(String move) {
//		//Check for validity
//		//Send move to both clients
//	}

	@Override
	public void confirmMove(String move) {
		System.out.println(move);
	}
}
