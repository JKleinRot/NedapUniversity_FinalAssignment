package game.board;

import java.util.List;

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
	
	/** Whether the intersection is in an intersection group. */
	private boolean isInIntersectionGroup;
	
	/** The intersection group. */
	private IntersectionGroup intersectionGroup;
	
	/** The position of the intersection. */
	private Position position;
	/** 
	 * Creates a new intersection that is initially unoccupied. 
	 */
	public Intersection(Position position) {
		isOccupied = false;
		isInIntersectionGroup = false;
		this.position = position;
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
	
	/**
	 * Return if an intersection is in an intersection group.
	 * @return
	 * 			True if the intersection is in an intersection group and false otherwise.
	 */
	public boolean isInIntersectionGroup() {
		return isInIntersectionGroup;
	}
	
	/** 
	 * Return the intersection group the intersection is in.
	 * @return
	 * 			The intersection group.
	 */
	public IntersectionGroup getIntersectionGroup() {
		return intersectionGroup;
	}
	
	/**
	 * Set an intersection group and add this intersection.
	 */
	public void setNewIntersectionGroup() {
		intersectionGroup = new IntersectionGroup();
		intersectionGroup.addIntersection(this);
	}
	
	/**
	 * Set an intersection group.
	 * @param intersectionGroup
	 * 			The intersection group of this intersection.
	 */
	public void setIntersectionGroup(IntersectionGroup intersectionGroup) {
		this.intersectionGroup = intersectionGroup;
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
