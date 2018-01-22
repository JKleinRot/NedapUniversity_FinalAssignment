package game.player;

import java.util.Observable;

import game.board.Board;
import game.board.stone.StoneColor;
import gui.GoGUIIntegrator;
import protocol.Protocol.General;
import protocol.Protocol.Server;
/**
 * Human player for Go.
 * @author janine.kleinrot
 */
public class HumanPlayer extends Observable implements Player {
	
	private Board board;
	
	private GoGUIIntegrator goGUI;
	
	private String name;
	
	private StoneColor stoneColor;
	
	private boolean isWhite;
	
	private boolean isValidMove;
	
	/**
	 * Creates a human player with a given name and stone color.
	 * @param name
	 * 			The name of the human player.
	 * @param color
	 * 			The color of the stones of the human player.
	 */
	public HumanPlayer(String name, StoneColor color) {
		this.name = name;
		this.stoneColor = color;
		if (stoneColor.equals(StoneColor.WHITE)) {
			isWhite = true;
		} else {
			isWhite = false;
		}
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
	
	public void setBoard(String boardSize) {
		goGUI = new GoGUIIntegrator(true, true, Integer.parseInt(boardSize));
		goGUI.startGUI();
		board = new Board(Integer.parseInt(boardSize));
	}
	
	@Override
	public void processPreviousMove(String move) {
		if (!move.equals(Server.FIRST)) {
			String[] moveCoordinates = move.split(General.DELIMITER2); 
			board.setStone(Integer.parseInt(moveCoordinates[0]), 
					Integer.parseInt(moveCoordinates[1]), 
					stoneColor);
			goGUI.addStone(Integer.parseInt(moveCoordinates[0]), 
					Integer.parseInt(moveCoordinates[1]), isWhite);
		}
		System.out.println("ProcessPreviousMove");
	}
	
	@Override
	public void determineMove() {
		setChanged();
		notifyObservers("Move requested");
	}

	@Override
	public void makeMove(String move) {
		String[] moveCoordinates = move.split(General.DELIMITER2);
//		board.setStone(Integer.parseInt(moveCoordinates[0]), Integer.parseInt(moveCoordinates[1]), 
//				stoneColor);
		checkMove(move);
//		
	}
	
	@Override
	public void checkMove(String move) {
		//Needs implementation!
		isValidMove = true;
		if (isValidMove) {
			setChanged();
			notifyObservers("Valid move");
		} else {
			setChanged();
			notifyObservers("Invalid move");
		}
	}
	
}
