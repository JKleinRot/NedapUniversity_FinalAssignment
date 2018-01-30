package game.player;

import game.board.stone.StoneColor;

/**
 * Human player for Go.
 * @author janine.kleinrot
 */
public class HumanPlayer extends AbstractPlayer {
	
	/**
	 * Create a human player with a given name and stone color.
	 * @param name
	 * 			The name of the human player.
	 * @param color
	 * 			The color of the stones of the human player.
	 */
	public HumanPlayer(String name, StoneColor color) {
		super(name, color);
	}
	
	/**
	 * Determine the move.
	 */
	public void determineMove() {
		setChanged();
		notifyObservers("Move requested");
	}
	
}
