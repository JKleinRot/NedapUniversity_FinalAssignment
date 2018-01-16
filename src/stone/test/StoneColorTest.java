package stone.test;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

import stone.StoneColor;

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
