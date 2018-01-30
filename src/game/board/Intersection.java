package game.board;

import game.board.stone.Stone;
import game.board.stone.StoneColor;

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
	
	/** The position of the intersection. */
	private Position position;
	
	/** 
	 * Create a new intersection that is initially unoccupied at the provided position. 
	 * @param position
	 * 			The position.
	 */
	public Intersection(Position position) {
		isOccupied = false;
		this.position = position;
	}
	
	/**
	 * Create a copy of the current intersection.
	 * @return
	 * 			A copy of the intersection.
	 */
	public Intersection copy() {
		Intersection copy = new Intersection(position);
		copy.isOccupied = isOccupied;
		copy.stone = stone == null ? null : stone.copy();
		return copy;
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
	 * Return the stone at the intersection.
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
	 * Whether or not a stone is at the intersection.
	 * @return
	 * 			True if a stone occupies the intersection and false otherwise.
	 */
	public boolean isOccupied() {
		return isOccupied;
	}

	/**
	 * Return the position of the intersection.
	 * @return
	 * 			The position.
	 */
	public Position getPosition() {
		return position;
	}

}
