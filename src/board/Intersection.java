package board;

import stone.Stone;
import stone.StoneColor;

/**
 * Intersection at the Go board.
 * An intersection can be unoccupied or occupied by either a black stone or a white stone.
 * @author janine.kleinrot
 *
 */
public class Intersection {
	
	/** Whether the intersection is occupied. */
	private boolean isOccupied;
	
	/** Stone at the intersection. */
	private Stone stone;
	
	/** 
	 * Creates a new intersection that is initially unoccupied. 
	 */
	public Intersection() {
		isOccupied = false;
	}
	
	/** 
	 * Put a stone at the intersection with the provided color. 
	 * @param color
	 * 			The color of the stone.
	 */
	public void setStone(StoneColor color) {
		isOccupied = true;
		stone = new Stone(color);
	}
	
	/**
	 * Returns the stone at the intersection.
	 * @return
	 * 			The stone at the intersection.
	 */
	public Stone getStone() {
		return stone;
	}
	
	/**
	 * Remove stone at the intersection.
	 */
	public void removeStone() {
		isOccupied = false;
		stone = null;
	}
	
	/**
	 * Returns if a stone is at the intersection.
	 * @return
	 * 			True if a stone occupies the intersection and false otherwise.
	 */
	public boolean isOccupied() {
		return isOccupied;
	}

}
