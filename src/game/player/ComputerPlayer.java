package game.player;

import game.board.Board;
import game.board.stone.StoneColor;
import protocol.Protocol.General;
import protocol.Protocol.Server;

/**
 * Computer player for Go.
 * @author janine.kleinrot
 */
public class ComputerPlayer implements Player {
	
	/** The board. */
	private Board board;
	
	/** The name of the player. */
	private String name;
	
	/** The stone color of the player. */
	private StoneColor stoneColor;
	
	/** Whether the stone color is white. */
	private boolean isWhite;
	
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
		if (stoneColor.equals(StoneColor.WHITE)) {
			isWhite = true;
		} else {
			isWhite = false;
		}
	}

	/** 
	 * Return the name of the player.
	 * @return
	 * 			The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 *  Return the stone color of the player.
	 * @return
	 * 			The stone color.
	 */
	public StoneColor getStoneColor() {
		return stoneColor;
	}
	
	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public void setBoard(String boardSize) {
		board = new Board(Integer.parseInt(boardSize), true);	
	}

	@Override
	public void makeMove(String move) {
		//Notify of move
		//Check move and forward it to clienthandler
	}

	@Override
	public void processPreviousMove(String move, String previousPlayer) {
		if (!move.equals(Server.FIRST)) {
			if (previousPlayer.equals(name.toUpperCase())) {
				String[] moveCoordinates = move.split(General.DELIMITER2); 
				board.setStone(Integer.parseInt(moveCoordinates[0]), 
						Integer.parseInt(moveCoordinates[1]), 
						stoneColor);
			} else {
				String[] moveCoordinates = move.split(General.DELIMITER2); 
				board.setStone(Integer.parseInt(moveCoordinates[0]), 
						Integer.parseInt(moveCoordinates[1]), 
						stoneColor.other());
			}
		}
	}

	@Override
	public void determineMove() {
		//Make the move depending on the used strategy
	}
	
}
