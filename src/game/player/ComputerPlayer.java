package game.player;

import game.board.Board;
import game.board.stone.StoneColor;
import gui.GoGUIIntegrator;
import protocol.Protocol.General;

/**
 * Computer player for Go.
 * @author janine.kleinrot
 */
public class ComputerPlayer implements Player {
	
	private Board board;
	
	private GoGUIIntegrator goGUI;
	
	private String name;
	
	private StoneColor stoneColor;
	
	/**
	 * Creates a computer player with a given name and stone color.
	 * @param name
	 * 			The name of the computer player.
	 * @param color
	 * 			The color of the stones of the computer player.
	 */
	public ComputerPlayer(String name, StoneColor color) {
		this.name = name;
		this.stoneColor = color;
	}

	public String getName() {
		return name;
	}
	
	public StoneColor getStoneColor() {
		return stoneColor;
	}
	
	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public void setBoard(String boardSize) {
		goGUI = new GoGUIIntegrator(true, true, Integer.parseInt(boardSize));
		goGUI.startGUI();
		board = new Board(Integer.parseInt(boardSize));	
	}

	@Override
	public void makeMove(String move) {
		processPreviousMove(move);
	}

	@Override
	public void processPreviousMove(String move) {
		String[] moveCoordinates = move.split(General.DELIMITER2); 
		board.setStone(Integer.parseInt(moveCoordinates[1]), Integer.parseInt(moveCoordinates[2]), 
				getStoneColor());
	}
	
}
