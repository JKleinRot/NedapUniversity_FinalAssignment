package game;

import java.util.ArrayList;
import java.util.List;

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
	
	private boolean isValidMove;
	
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

	@Override
	public void confirmMove(String move) {
		//Needs implementation!!!
		System.out.println("ConfirmMove");
		isValidMove = true;
		if (isValidMove) {
			firstGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
					firstGoClientHandler.getGoClientName() + General.DELIMITER1 + 
					move + General.DELIMITER1 + secondGoClientHandler.getGoClientName() + 
					General.COMMAND_END);
		}
	}
	
	@Override
	public List<GoClientHandler> getGoClientHandlers() {
		List<GoClientHandler> goClientHandlers = new ArrayList<GoClientHandler>();
		goClientHandlers.add(firstGoClientHandler);
		goClientHandlers.add(secondGoClientHandler);
		return goClientHandlers;
	}
}
