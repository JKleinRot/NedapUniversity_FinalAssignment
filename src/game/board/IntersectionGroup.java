package game.board;

import java.util.ArrayList;
import java.util.HashSet;
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
	 * Create a new intersection group. 
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
	 * Return the list of intersections in the intersection group.
	 * @return
	 * 			The intersections in the intersection group.
	 */
	public List<Intersection> getIntersections() {
		return new ArrayList<Intersection>(intersections);
	}

	/**
	 * Create a copy of the current intersection group.
	 * @return
	 * 			The copy of the intersection group.
	 */
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
