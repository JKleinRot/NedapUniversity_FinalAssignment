package player;

import stone.StoneColor;
/**
 * Human player for Go.
 * @author janine.kleinrot
 */
public class HumanPlayer extends Player {
	
	/**
	 * Creates a human player with a given name and stone color.
	 * @param name
	 * 			The name of the human player.
	 * @param color
	 * 			The color of the stones of the human player.
	 */
	public HumanPlayer(String name, StoneColor color) {
		super(name, color);
	}
	
}
