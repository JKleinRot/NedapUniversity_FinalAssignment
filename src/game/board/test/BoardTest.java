package game.board.test;

import org.junit.Before;
import org.junit.Test;

import game.board.Board;
import game.board.stone.StoneColor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test program for Board.
 * @author janine.kleinrot
 */
public class BoardTest {

	/** Test variable for a Board object. */
	private Board boardneg1xneg1;
	
	/** Test variable for a Board object. */
	private Board board5x5;
	
	/** Test variable for a Board object. */
	private Board board9x9;
	
	/** Test variable for a Board object. */
	private Board board19x19;
	
	/** Test variable for a Board object. */
	private Board board22x22;
	
	/**
	 * Creates a Go board of -1 x -1, 5 x 5, 9 x 9, 19 x 19 and 22 x 22 intersections.
	 */
	@Before
	public void setUp() {
		boardneg1xneg1 = new Board(-1, false);
		board5x5 = new Board(5, false);
		board9x9 = new Board(9, false);
		board19x19 = new Board(19, false);
		board22x22 = new Board(22, false);
	}
	
	/**
	 * Tests that the boards have the size provided as an argument for 5 <= argument <= 19.
	 * The -1 x -1 board results in a board of 5 x 5 intersections.
	 * The 22 x 22 board results in a board of 19 x 19 intersections.
	 */
	@Test
	public void testInitialState() {
		assertEquals(5, boardneg1xneg1.getSize());
		assertEquals(5, board5x5.getSize());
		assertEquals(9, board9x9.getSize());
		assertEquals(19, board19x19.getSize());
		assertEquals(19, board22x22.getSize());
	}
	
	/**
	 * Tests setting a stone at the intersection at the provided x and y coordinate.
	 */
	@Test
	public void testSetStone() {
		board9x9.setStone(0, 0, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getStone(0, 0).getColor());
		board9x9.setStone(0, 8, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getStone(0, 8).getColor());
		board9x9.setStone(8, 0, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getStone(8, 0).getColor());
		board9x9.setStone(8, 8, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getStone(8, 8).getColor());
		board9x9.setStone(4, 5, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getStone(4, 5).getColor());
	}
	
	@Test
	public void testRemoveStone() {
		board9x9.setStone(4, 5, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getStone(4, 5).getColor());
		board9x9.removeStone(4, 5);
		assertNull(board9x9.getStone(4, 5));
	}
}
