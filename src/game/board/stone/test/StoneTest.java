package game.board.stone.test;

import org.junit.Before;
import org.junit.Test;

import game.board.stone.Stone;
import game.board.stone.StoneColor;

import static org.junit.Assert.assertEquals;

/**
 * Test program for Stone.
 * @author janine.kleinrot
 */
public class StoneTest {

	/** Test variable for a Stone object. */
	private Stone blackStone;
	
	/** Test variable for a Stone object. */
	private Stone whiteStone;
	
	/** 
	 * Create a black and a white stone. 
	 */
	@Before
	public void setUp() {
		blackStone = new Stone(StoneColor.BLACK);
		whiteStone = new Stone(StoneColor.WHITE);
	}
	
	/**
	 * Test that a stone has initial 4 liberties and that the color is correct.
	 */
	@Test
	public void testInitialState() {
		assertEquals(4, blackStone.getLiberties());
		assertEquals(4, blackStone.getInitialLiberties());
		assertEquals(StoneColor.BLACK, blackStone.getColor());
		assertEquals(StoneColor.WHITE, whiteStone.getColor());
	}
	
	/** 
	 * Test setting the (initial) liberties to a new value. 
	 */
	@Test
	public void testSetLiberties() {
		blackStone.setInitialLiberties(3);
		blackStone.setLiberties(2);
		assertEquals(3, blackStone.getInitialLiberties());
		assertEquals(2, blackStone.getLiberties());
	}
	
	/**
	 * Test that the copy method returns the same stone.
	 */
	@Test
	public void testCopy() {
		blackStone.setInitialLiberties(3);
		blackStone.setLiberties(2);
		Stone newBlackStone = blackStone.copy();
		assertEquals(3, newBlackStone.getInitialLiberties());
		assertEquals(2, newBlackStone.getLiberties());
	}

}
