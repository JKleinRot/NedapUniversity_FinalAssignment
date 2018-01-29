package game.player;

import java.util.Observable;

import game.MoveChecker;
import game.MoveCheckerImpl;
import game.board.Board;
import game.board.Position;
import game.board.stone.StoneColor;
import gui.GoGUIIntegrator;
import protocol.Protocol.Client;
import protocol.Protocol.General;
import protocol.Protocol.Server;

public abstract class AbstractPlayer extends Observable implements Player {
	
	/** The board. */
	private Board board;
	
	/** The previous board situation. */
	private Board previousBoard;
	
	/** The next board situation. */
	private Board nextBoard;
	
	/** The name of the player. */
	private String name;
	
	/** The stone color of the player. */
	private StoneColor stoneColor;
	
	/** Whether the move is valid. */
	private boolean isValidMove;
	
	/** The move checker. */
	private MoveChecker moveChecker;
	
	/** The check message. */
	private String checkMessage;
	
	/** The Go GUI. */
	private GoGUIIntegrator goGUI;
	
	/** 
	 * Create a new abstract player.
	 * @param name
	 * 			The name.
	 * @param color
	 * 			The stone color.
	 */
	public AbstractPlayer(String name, StoneColor color) {
		this.name = name;
		this.stoneColor = color;
		moveChecker = new MoveCheckerImpl();
	}
	
	public Board getBoard() {
		return board;
	}
	
	public String getName() {
		return name;
	}
	
	public StoneColor getStoneColor() {
		return stoneColor;
	}
	
	@Override
	public void setBoard(String boardSize) {
		board = new Board(Integer.parseInt(boardSize), true);
		previousBoard = board.copy();
		nextBoard = board.copy();
	}
	
	@Override
	public void adjustBoard(String boardSize, GoGUIIntegrator goGUI) {
		board = new Board(Integer.parseInt(boardSize), true);
		board.setGoGUI(goGUI);
		board.getGoGUI().setBoardSize(Integer.parseInt(boardSize));
		previousBoard = board.copy();
		nextBoard = board.copy();
	}
	
	@Override
	public void setGoGUI(String boardSize) {
		goGUI = board.startGoGUI();
	}
	
	@Override
	public void processPreviousMove(String move, String previousPlayer) {
		if (!move.equals(Server.FIRST)) {
			if (previousPlayer.equals(name.toUpperCase())) {
				if (!move.equals(Server.PASS)) {
					previousBoard = board.copy();
					String[] moveCoordinates = move.split(General.DELIMITER2); 
					board.setStone(Integer.parseInt(moveCoordinates[0]), 
							Integer.parseInt(moveCoordinates[1]), 
							stoneColor);
					nextBoard = board.copy();
					System.out.println("Liberties " + board.getIntersection(new Position(Integer.parseInt(moveCoordinates[0]), 
							Integer.parseInt(moveCoordinates[1]))).getStone().getLiberties() + "");
				}
				setChanged();
				notifyObservers("Move made");
			} else {
				if (!move.equals(Server.PASS)) {
					previousBoard = board.copy();
					String[] moveCoordinates = move.split(General.DELIMITER2); 
					board.setStone(Integer.parseInt(moveCoordinates[0]), 
							Integer.parseInt(moveCoordinates[1]), 
							stoneColor.other());
					nextBoard = board.copy();
				}
				setChanged();
				notifyObservers("Other move made");
			} 
		}
	}
	
	@Override
	public void makeMove(String move) {
		board.getGoGUI().removeHintIdicator();
		if (!move.equals(Client.PASS)) {
			String[] moveCoordinates = move.split(General.DELIMITER2);
			if (moveCoordinates.length != 2) {
				setChanged();
				notifyObservers("Invalid move input");
			} else {
				try {
					int moveX = Integer.parseInt(moveCoordinates[0]);
					int moveY = Integer.parseInt(moveCoordinates[1]);
					isValidMove = moveChecker.checkMove(moveX, moveY, stoneColor, board, 
							previousBoard, nextBoard);
					if (isValidMove) {
						setChanged();
						notifyObservers("Valid move " + move);
					} else {
						checkMessage = moveChecker.getMoveViolations();
						handleCheckMessage(checkMessage);
					}
				} catch (NumberFormatException e) {
					setChanged();
					notifyObservers("Invalid move input");
				}
			}
		} else {
			setChanged();
			notifyObservers("Valid move " + move);
		}
	}

	private void handleCheckMessage(String aCheckMessage) {
		if (checkMessage.contains("Move")) {
			setChanged();
			notifyObservers("Move not on board");
		} else if (checkMessage.contains("Occupied")) {
			setChanged();
			notifyObservers("Occupied intersection");
		} else if (checkMessage.contains("Ko")) {
			setChanged();
			notifyObservers("Ko rule");
		}
	}
	
	public abstract void determineMove();
	
	@Override 
	public void provideHint() {
		for (int x = 0; x < getBoard().getSize(); x++) {
			for (int y = 0; y < getBoard().getSize(); y++) {
				if (!getBoard().getIntersection(new Position(x, y)).isOccupied()) {
					board.getGoGUI().addHintIndicator(x, y);
					return;
				}
			}
			
		}
	}

}
