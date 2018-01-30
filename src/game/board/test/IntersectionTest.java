package game.board.test;

import org.junit.Before;
import org.junit.Test;

import game.board.Intersection;
import game.board.Position;
import game.board.stone.StoneColor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test program for Intersection.
 * @author janine.kleinrot
 */
public class IntersectionTest {

	/** Test variable for an Intersection object. */
	private Intersection intersection;
	
	/** Test variable for a Position object. */
	private Position position;
	
	/** 
	 * Create an intersection.
	 */
	@Before
	public void setUp() {
		position = new Position(0, 0);
		intersection = new Intersection(position);
	}
	
	/**
	 * Test that the intersection is initially unoccupied.
	 */
	@Test
	public void testInitialState() {
		assertFalse(intersection.isOccupied());
		assertEquals(position, intersection.getPosition());
	}
	
	/**
	 * Test setting a stone at the intersection.
	 */
	@Test
	public void testSetStone() {
		intersection.setStone(StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, intersection.getStone().getColor());
		assertTrue(intersection.isOccupied());
	}
	
	/**
	 * Test removing a stone from the intersection.
	 */
	@Test
	public void testRemoveStone() {
		intersection.setStone(StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, intersection.getStone().getColor());
		intersection.removeStone();
		assertNull(intersection.getStone());
		assertFalse(intersection.isOccupied());
	}
	
	/**
	 * Test the copy of the intersection with a stone.
	 */
	@Test
	public void testCopyWithStone() {
		intersection.setStone(StoneColor.BLACK);
		Intersection newIntersection = intersection.copy();
		assertEquals(StoneColor.BLACK, newIntersection.getStone().getColor());
		assertTrue(intersection.isOccupied());
		assertEquals(position, newIntersection.getPosition());
	}
	
	/**
	 * Test the copy of the intersection without a stone.
	 */
	@Test
	public void testCopyWithoutStone() {
		Intersection newIntersection = intersection.copy();
		assertNull(newIntersection.getStone());
		assertFalse(intersection.isOccupied());
		assertEquals(position, newIntersection.getPosition());
	}
}
