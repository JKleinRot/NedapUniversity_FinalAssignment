package game.player;

import game.board.Board;
import game.board.Position;
import game.board.stone.StoneColor;
import protocol.Protocol.Client;
import protocol.Protocol.General;
import protocol.Protocol.Server;

/**
 * Computer player for Go.
 * @author janine.kleinrot
 */
public class ComputerPlayer extends AbstractPlayer {
	
	private int moveCount;
	
	/**
	 * Creates a computer player with a given name and stone color.
	 * @param name
	 * 			The name of the computer player.
	 * @param color
	 * 			The color of the stones of the computer player.
	 */
	public ComputerPlayer(String name, StoneColor color) {
		super(name, color);
		moveCount = 0;
	}

	public void determineMove() {
		if (moveCount <= 2) {
			for (int x = 0; x < getBoard().getSize(); x++) {
				for (int y = 0; y < getBoard().getSize(); y++) {
					if (!getBoard().getIntersection(new Position(x, y)).isOccupied()) {
						makeMove(x + General.DELIMITER2 + y);
						moveCount++;
						return;
					}
				}
				
			}
		} else {
			makeMove(Client.PASS);
		}
	}
	
}
