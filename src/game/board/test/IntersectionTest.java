package game.board.test;

import org.junit.Before;
import org.junit.Test;

import game.board.Intersection;
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

	/**Test variable for an Intersection object. */
	private Intersection intersection;
	
	/** 
	 * Creates an intersection.
	 */
	@Before
	public void setUp() {
		intersection = new Intersection();
	}
	
	/**
	 * Tests that the intersection is initially unoccupied.
	 */
	@Test
	public void testInitialState() {
		assertFalse(intersection.isOccupied());
	}
	
	/**
	 * Tests setting a stone at the intersection.
	 */
	@Test
	public void testSetStone() {
		intersection.setStone(StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, intersection.getStone().getColor());
		assertTrue(intersection.isOccupied());
	}
	
	/**
	 * Tests removing a stone from the intersection.
	 */
	@Test
	public void testRemoveStone() {
		intersection.setStone(StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, intersection.getStone().getColor());
		intersection.removeStone();
		assertNull(intersection.getStone());
		assertFalse(intersection.isOccupied());
	}
	
}
