package player;

import stone.StoneColor;

/**
 * Computer player for Go.
 * @author janine.kleinrot
 */
public class ComputerPlayer extends Player {
	
	/**
	 * Creates a computer player with a given name and stone color.
	 * @param name
	 * 			The name of the computer player.
	 * @param color
	 * 			The color of the stones of the computer player.
	 */
	public ComputerPlayer(String name, StoneColor color) {
		super(name, color);
	}
	
}
