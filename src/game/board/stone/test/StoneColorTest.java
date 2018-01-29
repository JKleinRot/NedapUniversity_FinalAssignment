package game.board.stone.test;

import org.junit.Test;

import game.board.stone.StoneColor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test program for StoneColor.
 * @author janine.kleinrot
 */
public class StoneColorTest {
	
	/** 
	 * Test that StoneColor.WHITE and StoneColor.BLACK are present in StoneColor. 
	 */
	@Test
	public void testPossibleValues() {
		assertNotNull(StoneColor.valueOf("BLACK"));
		assertNotNull(StoneColor.valueOf("WHITE"));
	}
	
	/**
	 * Test that StoneColor.other() returns the opposite stonecolor.
	 */
	@Test
	public void testOther() {
		assertEquals(StoneColor.BLACK, StoneColor.WHITE.other());
		assertEquals(StoneColor.WHITE, StoneColor.BLACK.other());
	}
	
}
