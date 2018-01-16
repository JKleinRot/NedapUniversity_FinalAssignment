package stone.test;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import stone.StoneColor;

/**
 * Test program for StoneColor.
 * @author janine.kleinrot
 */
public class StoneColorTest {
	
	/** Tests that StoneColor.WHITE and StoneColor.BLACK are present in StoneColor. */
	@Test
	public void testPossibleValues() {
		assertTrue(StoneColor.valueOf("WHITE") != null);
		assertTrue(StoneColor.valueOf("BLACK") != null);
	}
	
}
