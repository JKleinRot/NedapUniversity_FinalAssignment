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
	
	private String move;
	
	public GameImpl(GoClientHandler firstGoClientHandler, GoClientHandler secondGoClientHandler) {
		this.firstGoClientHandler = firstGoClientHandler;
		this.secondGoClientHandler = secondGoClientHandler;
		this.firstGoClientHandler.getGoClientHandlerActor().setGame(this);
		this.secondGoClientHandler.getGoClientHandlerActor().setGame(this);
		board = new Board(Integer.parseInt(firstGoClientHandler.getBoardSize()));
		isGameOver = false;
		isMoveMade = false;
		numberOfMoves = 0;
	}

	@Override
	public synchronized void run() {
		while (!isGameOver) {
			if (numberOfMoves == 0) {
				firstGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
						firstGoClientHandler.getGoClientName() + General.DELIMITER1 + 
						Server.FIRST + General.DELIMITER1 + 
						firstGoClientHandler.getGoClientName() + General.COMMAND_END);
				numberOfMoves++;
			}
			while (!isMoveMade) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (numberOfMoves % 2 == 1) {
					firstGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
							firstGoClientHandler.getGoClientName() + General.DELIMITER1 + 
							move + General.DELIMITER1 + secondGoClientHandler.getGoClientName() + 
							General.COMMAND_END);
					secondGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
							firstGoClientHandler.getGoClientName() + General.DELIMITER1 + 
							move + General.DELIMITER1 + secondGoClientHandler.getGoClientName() + 
							General.COMMAND_END);
					numberOfMoves++;
				} else if (numberOfMoves % 2 == 0) {
					firstGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
							secondGoClientHandler.getGoClientName() + General.DELIMITER1 + 
							move + General.DELIMITER1 + firstGoClientHandler.getGoClientName() + 
							General.COMMAND_END);
					secondGoClientHandler.sendMessage(Server.TURN + General.DELIMITER1 + 
							secondGoClientHandler.getGoClientName() + General.DELIMITER1 + 
							move + General.DELIMITER1 + firstGoClientHandler.getGoClientName() + 
							General.COMMAND_END);
					numberOfMoves++;
				}
			}
		}
		
	}

	@Override
	public synchronized void confirmMove(String moveMade) {
		//Needs implementation!!!
		isValidMove = true;
		if (isValidMove) {
			this.move = moveMade;
			notifyAll();
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
