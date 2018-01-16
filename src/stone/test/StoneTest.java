package stone.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import stone.Stone;
import stone.StoneColor;

/**
 * Test program for Stone.
 * @author janine.kleinrot
 */
public class StoneTest {

	/** Test variable for a Stone object. */
	private Stone blackStone;
	
	/** Test variable for a Stone object. */
	private Stone whiteStone;
	
	/** Creates a black and a white stone. */
	@Before
	public void setUp() {
		blackStone = new Stone(StoneColor.BLACK);
		whiteStone = new Stone(StoneColor.WHITE);
	}
	
	/**
	 * Tests that a stone has initial 4 liberties.
	 */
	@Test
	public void testGetLiberties() {
		assertEquals(4, blackStone.getLiberties());
	}
	
	/** Tests setting the liberties to a new value. */
	@Test
	public void testSetLiberties() {
		blackStone.setLiberties(2);
		assertEquals(2, blackStone.getLiberties());
	}
	
	/** Tests that a stone has the correct color. */
	@Test
	public void testGetColor() {
		assertEquals(StoneColor.BLACK, blackStone.getColor());
		assertEquals(StoneColor.WHITE, whiteStone.getColor());
	}

}
