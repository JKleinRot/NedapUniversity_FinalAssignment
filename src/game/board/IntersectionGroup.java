package game.board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A group of intersections of which the stones are from the same color 
 * and lie adjacent to each other.
 * @author janine.kleinrot
 */
public class IntersectionGroup {

	/** A list of the intersections in the group. */
	private Set<Intersection> intersections;
	
	/** The liberties of the group. */
	private int liberties;
	
	/**
	 * Creates a new intersection group. 
	 */
	public IntersectionGroup() {
		intersections = new HashSet<Intersection>();
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
	 * Set the amount of liberties of the group. 
	 */
	public void setLiberties() {
		liberties = 0;
		Iterator<Intersection> intersectionsIterator = intersections.iterator();
		while (intersectionsIterator.hasNext()) {
			Intersection intersection = intersectionsIterator.next();
			liberties = liberties + intersection.getStone().getLiberties();
		}
	}
	
	/**
	 * Return the amount of liberties.
	 * @return
	 * 			The amount of liberties.
	 */
	public int getLiberties() {
		setLiberties();
		return liberties;
	}
	
	public List<Intersection> getIntersections() {
		return new ArrayList<Intersection>(intersections);
	}

	public IntersectionGroup copy() {
		IntersectionGroup copy = new IntersectionGroup();
		copy.liberties = liberties;
		copy.intersections = new HashSet<Intersection>();
		for (Intersection intersection : intersections) {
			copy.intersections.add(intersection.copy());
		}
		return copy;
	}
}
