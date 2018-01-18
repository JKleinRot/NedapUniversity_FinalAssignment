package game.board.stone.test;

import org.junit.Test;

import game.board.stone.StoneColor;

import static org.junit.Assert.assertNotNull;

/**
 * Test program for StoneColor.
 * @author janine.kleinrot
 */
public class StoneColorTest {
	
	/** 
	 * Tests that StoneColor.WHITE and StoneColor.BLACK are present in StoneColor. 
	 */
	@Test
	public void testPossibleValues() {
		assertNotNull(StoneColor.valueOf("WHITE"));
		assertNotNull(StoneColor.valueOf("BLACK"));
	}
	
}
