package player;

import stone.StoneColor;
/**
 * Abstract player for Go.
 * @author janine.kleinrot
 */
public abstract class Player {
	
	/** The name of the player. */
	private String name;
	
	/** The color of the stones of the player. */
	private StoneColor color;
	
	/**
	 * Creates a player with a given name and stone color.
	 * @param name
	 * 			The name of the player.
	 * @param color
	 * 			The color of the stones of the player.
	 */
	public Player(String name, StoneColor color) {
		this.name = name;
		this.color = color;
	}
	
	/** 
	 * Get the name of the player.
	 * @return
	 * 			The name of the player.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the color of the stones of the player.
	 * @return
	 * 			The color of the stones of the player.
	 */
	public StoneColor getColor() {
		return color;
	}

}
