package game.board;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A group of intersections of which the stones are from the same color 
 * and lie adjacent to each other.
 * @author janine.kleinrot
 */
public class IntersectionGroup {

	/** A list of the intersections in the group. */
	private List<Intersection> intersections;
	
	/** The liberties of the group. */
	private int liberties;
	
	/**
	 * Creates a new intersection group. 
	 */
	public IntersectionGroup() {
		intersections = new ArrayList<Intersection>();
	}
	
	/**
	 * Add the provided intersection to the list of intersections.
	 * @param intersection
	 * 			The intersection.
	 */
	public void addIntersection(Intersection intersection) {
		intersections.add(intersection);
	}
	
	/**
	 * Return the amount of liberties of the group. 
	 * @return
	 * 			The amount of liberties.
	 */
	public int getLiberties() {
		Iterator<Intersection> intersectionsIterator = intersections.iterator();
		while (intersectionsIterator.hasNext()) {
			Intersection intersection = intersectionsIterator.next();
			liberties = liberties + intersection.getStone().getLiberties();
		}
		return liberties;
	}
}
