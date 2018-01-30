package game.board.test;

import org.junit.Before;
import org.junit.Test;

import game.board.Board;
import game.board.Position;
import game.board.stone.StoneColor;
import gui.GoGUIIntegrator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
	
	/** Test variable for a Board object with a GoGUI. */
	private Board boardGoGUI;
	
	/** Test variable for a GoGUI. */
	private GoGUIIntegrator goGUI;
	
	/**
	 * Create a Go board of -1 x -1, 5 x 5, 9 x 9, 19 x 19 and 22 x 22 intersections.
	 */
	@Before
	public void setUp() {
		boardneg1xneg1 = new Board(-1, false);
		board5x5 = new Board(5, false);
		board9x9 = new Board(9, false);
		board19x19 = new Board(19, false);
		board22x22 = new Board(22, false);
		boardGoGUI = new Board(5, true);
		goGUI = boardGoGUI.startGoGUI();
	}
	
	/**
	 * Test that the boards have the size provided as an argument for 5 <= argument <= 19.
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
	 * Test setting a stone at the intersection and the corresponding initial liberties.
	 */
	@Test
	public void testSetStone() {
		board9x9.setStone(0, 0, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getIntersection(new Position(0, 0)).getStone()
				.getColor());
		assertEquals(2, board9x9.getIntersection(new Position(0, 0)).getStone().getLiberties());
		board9x9.setStone(0, 8, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getIntersection(new Position(0, 8)).getStone()
				.getColor());
		assertEquals(2, board9x9.getIntersection(new Position(0, 8)).getStone().getLiberties());
		board9x9.setStone(8, 0, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getIntersection(new Position(8, 0)).getStone()
				.getColor());
		assertEquals(2, board9x9.getIntersection(new Position(8, 0)).getStone().getLiberties());
		board9x9.setStone(8, 8, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getIntersection(new Position(8, 8)).getStone()
				.getColor());
		assertEquals(2, board9x9.getIntersection(new Position(8, 8)).getStone().getLiberties());
		board9x9.setStone(4, 5, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getIntersection(new Position(4, 5)).getStone()
				.getColor());
		assertEquals(4, board9x9.getIntersection(new Position(4, 5)).getStone().getLiberties());
		board9x9.setStone(4, 0, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getIntersection(new Position(4, 0)).getStone()
				.getColor());
		assertEquals(3, board9x9.getIntersection(new Position(4, 0)).getStone().getLiberties());
		board9x9.setStone(0, 4, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getIntersection(new Position(0, 4)).getStone()
				.getColor());
		assertEquals(3, board9x9.getIntersection(new Position(0, 4)).getStone().getLiberties());
		board9x9.setStone(4, 8, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getIntersection(new Position(4, 8)).getStone()
				.getColor());
		assertEquals(3, board9x9.getIntersection(new Position(4, 8)).getStone().getLiberties());
		board9x9.setStone(8, 4, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getIntersection(new Position(8, 4)).getStone()
				.getColor());
		assertEquals(3, board9x9.getIntersection(new Position(8, 4)).getStone().getLiberties());
		boardGoGUI.startGoGUI();
		boardGoGUI.setStone(2, 2, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, boardGoGUI.getIntersection(new Position(2, 2)).getStone()
				.getColor());
		boardGoGUI.setStone(3, 3, StoneColor.WHITE);
		assertEquals(StoneColor.WHITE, boardGoGUI.getIntersection(new Position(3, 3)).getStone()
				.getColor());
	}
	
	/**
	 * Test removing a stone from the board.
	 */
	@Test
	public void testRemoveStone() {
		board9x9.setStone(4, 5, StoneColor.BLACK);
		assertEquals(StoneColor.BLACK, board9x9.getIntersection(new Position(4, 5)).getStone()
				.getColor());
		board9x9.removeStone(new Position(4, 5));
		assertNull(board9x9.getIntersection(new Position(4, 5)).getStone());
	}
	
	/**
	 * Test that the copy of the board equals the original board.
	 */
	@Test 
	public void testCopy() {
		board9x9.setStone(0, 0, StoneColor.BLACK);
		Board newBoard9x9 = board9x9.copy();
		assertEquals(StoneColor.BLACK, newBoard9x9.getIntersection(new Position(0, 0)).getStone()
				.getColor());
	}
	
	/**
	 * Test that the copy of the board with intersection groups equals the original board.
	 */
	@Test
	public void testCopyWithIntersectionGroups() {
		board9x9.setStone(0, 0, StoneColor.BLACK);
		board9x9.setStone(0, 1, StoneColor.BLACK);
		Board newBoard9x9 = board9x9.copy();
		assertEquals(StoneColor.BLACK, newBoard9x9.getIntersection(new Position(0, 0)).getStone()
				.getColor());
		assertEquals(StoneColor.BLACK, newBoard9x9.getIntersection(new Position(0, 1)).getStone()
				.getColor());
		assertEquals(1, newBoard9x9.getIntersectionGroups().size());
	}
	
	/**
	 * Test that the copy of the board with empty intersection groups equals the original board.
	 */
	@Test
	public void testCopyWithEmptyIntersectionGroups() {
		board9x9.setStone(1, 0, StoneColor.BLACK);
		board9x9.setStone(0, 1, StoneColor.BLACK);
		board9x9.setStone(8, 8, StoneColor.WHITE);
		board9x9.calculateWinner();
		Board newBoard9x9 = board9x9.copy();
		assertEquals(StoneColor.BLACK, newBoard9x9.getIntersection(new Position(1, 0)).getStone()
				.getColor());
		assertEquals(StoneColor.BLACK, newBoard9x9.getIntersection(new Position(0, 1)).getStone()
				.getColor());
		assertEquals(2, newBoard9x9.getEmptyIntersectionGroups().size());
	}
	
	/**
	 * Test that a GoGUI is started if the second argument of the Board constructor is true.
	 */
	@Test
	public void testStartGoGUI() {
		GoGUIIntegrator noGoGUI = board9x9.startGoGUI();
		assertNotNull(goGUI instanceof GoGUIIntegrator);
		assertNull(noGoGUI);
	}
	
	/**
	 * Test setting the goGUI of a board.
	 */
	@Test
	public void testSetGoGUI() {
		board9x9.setGoGUI(goGUI);
		assertNotNull(board9x9.getGoGUI());
	}
	
	/**
	 * Test that the size of a board can be set to a different size.
	 */
	@Test
	public void testSetSize() {
		board9x9.setSize(10);
		assertEquals(10, board9x9.getSize());
	}
	
	/**
	 * Test that the stone is added to a new group if it is placed adjacent to a stone 
	 * of the same color that not in a group. 
	 * The already present stone is added to the group as well. 
	 */
	@Test
	public void testAddSetStoneToGroupNoGroupsYet() {
		board9x9.setStone(1, 1, StoneColor.BLACK);
		board9x9.setStone(1, 2, StoneColor.BLACK);
		board9x9.setStone(1, 3, StoneColor.WHITE);
		assertEquals(1, board9x9.getIntersectionGroups().size());
		assertEquals(2, board9x9.getIntersectionGroups().get(0).getIntersections().size());
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 1))));
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 2))));
		assertFalse(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 3))));
	}
	
	/**
	 * Test that a stone is added to a group if the stone is placed adjacent to a stone
	 * belonging to a group.
	 */
	@Test
	public void testAddSetStoneToGroupGroupPresent() {
		board9x9.setStone(1, 1, StoneColor.BLACK);
		board9x9.setStone(1, 2, StoneColor.BLACK);
		board9x9.setStone(1, 3, StoneColor.BLACK);
		board9x9.setStone(1, 4, StoneColor.WHITE);
		assertEquals(1, board9x9.getIntersectionGroups().size());
		assertEquals(3, board9x9.getIntersectionGroups().get(0).getIntersections().size());
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 1))));
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 2))));
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 3))));
	}
	
	/**
	 * Test that a new group is made with the adjacent and the set stone if a group of the 
	 * opposite color is present, but no group of the color of the set stone.
	 */
	@Test
	public void testAddSetStoneToGroupNoGroupOfColorYet() {
		board9x9.setStone(1, 1, StoneColor.BLACK);
		board9x9.setStone(1, 2, StoneColor.BLACK);
		board9x9.setStone(1, 3, StoneColor.WHITE);
		board9x9.setStone(1, 4, StoneColor.WHITE);
		assertEquals(2, board9x9.getIntersectionGroups().size());
		assertEquals(2, board9x9.getIntersectionGroups().get(0).getIntersections().size());
		assertEquals(2, board9x9.getIntersectionGroups().get(1).getIntersections().size());
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 1))));
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 2))));
		assertTrue(board9x9.getIntersectionGroups().get(1).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 3))));
		assertTrue(board9x9.getIntersectionGroups().get(1).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 4))));
	}
	
	/**
	 * Test that a new group is formed if two stones form a second group on the board.
	 */
	@Test
	public void testAddSetStoneToGroupNewGroupButColorAlreadyPresent() {
		board9x9.setStone(1, 1, StoneColor.BLACK);
		board9x9.setStone(1, 2, StoneColor.BLACK);
		board9x9.setStone(4, 1, StoneColor.BLACK);
		board9x9.setStone(4, 2, StoneColor.BLACK);
		assertEquals(2, board9x9.getIntersectionGroups().size());
		assertEquals(2, board9x9.getIntersectionGroups().get(0).getIntersections().size());
		assertEquals(2, board9x9.getIntersectionGroups().get(1).getIntersections().size());
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 1))));
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 2))));
		assertTrue(board9x9.getIntersectionGroups().get(1).getIntersections().contains(
				board9x9.getIntersection(new Position(4, 1))));
		assertTrue(board9x9.getIntersectionGroups().get(1).getIntersections().contains(
				board9x9.getIntersection(new Position(4, 2))));
	}
	
	/**
	 * Test that if a stone is place in between two stones of the same color that that results 
	 * in one group containing all those stones. 
	 */
	@Test
	public void testAddSetStoneToGroupMergeGroups() {
		board9x9.setStone(1, 1, StoneColor.BLACK);
		board9x9.setStone(1, 2, StoneColor.BLACK);
		board9x9.setStone(1, 4, StoneColor.BLACK);
		board9x9.setStone(1, 3, StoneColor.BLACK);
		assertEquals(1, board9x9.getIntersectionGroups().size());
		assertEquals(4, board9x9.getIntersectionGroups().get(0).getIntersections().size());
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 1))));
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 2))));
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 3))));
		assertTrue(board9x9.getIntersectionGroups().get(0).getIntersections().contains(
				board9x9.getIntersection(new Position(1, 4))));
	}
	
	/**
	 * Test that the liberties are correct after a suicide move of one stone 
	 * and that the stone is removed.
	 */
	@Test
	public void testCheckForSuicideMoveOneStone() {
		board9x9.setStone(1, 0, StoneColor.BLACK);
		board9x9.setStone(0, 1, StoneColor.BLACK);
		board9x9.setStone(0, 0, StoneColor.WHITE);
		assertNull(board9x9.getIntersection(new Position(0, 0)).getStone());
		assertEquals(3, board9x9.getIntersection(new Position(1, 0)).getStone().getLiberties());
		assertEquals(3, board9x9.getIntersection(new Position(0, 1)).getStone().getLiberties());
	}
	
	/**
	 * Test that the liberties are correct after suicide move of multiple stones 
	 * and that the group of stones is removed.
	 */
	@Test
	public void testCheckForSuicideMoveMultipleStones() {
		board9x9.setStone(0, 0, StoneColor.BLACK);
		board9x9.setStone(0, 1, StoneColor.WHITE);
		board9x9.setStone(1, 1, StoneColor.WHITE);
		board9x9.setStone(2, 0, StoneColor.WHITE);
		board9x9.setStone(1, 0, StoneColor.BLACK);
		assertNull(board9x9.getIntersection(new Position(0, 0)).getStone());
		assertEquals(3, board9x9.getIntersection(new Position(2, 0)).getStone().getLiberties());
		assertEquals(3, board9x9.getIntersection(new Position(1, 1)).getStone().getLiberties());
		assertEquals(2, board9x9.getIntersection(new Position(0, 1)).getStone().getLiberties());
	}
	
	/**
	 * Test that the liberties are correct after closed in stone and that the stone is removed.
	 */
	@Test
	public void testRemoveStonesWithZeroLibertiesOneStone() {
		board9x9.setStone(1, 1, StoneColor.WHITE);
		board9x9.setStone(1, 0, StoneColor.BLACK);
		board9x9.setStone(0, 1, StoneColor.BLACK);
		board9x9.setStone(1, 2, StoneColor.BLACK);
		board9x9.setStone(2, 1, StoneColor.BLACK);
		assertNull(board9x9.getIntersection(new Position(1, 1)).getStone());
		assertEquals(3, board9x9.getIntersection(new Position(1, 0)).getStone().getLiberties());
		assertEquals(3, board9x9.getIntersection(new Position(0, 1)).getStone().getLiberties());
		assertEquals(4, board9x9.getIntersection(new Position(1, 2)).getStone().getLiberties());
		assertEquals(4, board9x9.getIntersection(new Position(2, 1)).getStone().getLiberties());
	}
	
	/**
	 * Test that the liberties are correct after closed in stone and that the stone is removed 
	 * on the GoGUI.
	 */
	@Test
	public void testRemoveStonesWithZeroLibertiesOneStoneGoGUI() {
		boardGoGUI.setStone(1, 1, StoneColor.WHITE);
		boardGoGUI.setStone(1, 0, StoneColor.BLACK);
		boardGoGUI.setStone(0, 1, StoneColor.BLACK);
		boardGoGUI.setStone(1, 2, StoneColor.BLACK);
		boardGoGUI.setStone(2, 1, StoneColor.BLACK);
		assertNull(boardGoGUI.getIntersection(new Position(1, 1)).getStone());
		assertEquals(3, boardGoGUI.getIntersection(new Position(1, 0)).getStone().getLiberties());
		assertEquals(3, boardGoGUI.getIntersection(new Position(0, 1)).getStone().getLiberties());
		assertEquals(4, boardGoGUI.getIntersection(new Position(1, 2)).getStone().getLiberties());
		assertEquals(4, boardGoGUI.getIntersection(new Position(2, 1)).getStone().getLiberties());
	}
	
	/**
	 * Test that the liberties are correct after closed in group of stones 
	 * and that the group of stones is removed.
	 */
	@Test
	public void testRemoveStonesWithZeroLibertiesMultipleStones() {
		board9x9.setStone(1, 1, StoneColor.WHITE);
		board9x9.setStone(2, 1, StoneColor.WHITE);
		board9x9.setStone(1, 0, StoneColor.BLACK);
		board9x9.setStone(2, 0, StoneColor.BLACK);
		board9x9.setStone(3, 1, StoneColor.BLACK);
		board9x9.setStone(2, 2, StoneColor.BLACK);
		board9x9.setStone(1, 2, StoneColor.BLACK);
		board9x9.setStone(0, 1, StoneColor.BLACK);
		assertNull(board9x9.getIntersection(new Position(1, 1)).getStone());
		assertNull(board9x9.getIntersection(new Position(2, 1)).getStone());
		assertEquals(2, board9x9.getIntersection(new Position(1, 0)).getStone().getLiberties());
		assertEquals(2, board9x9.getIntersection(new Position(2, 0)).getStone().getLiberties());
		assertEquals(4, board9x9.getIntersection(new Position(3, 1)).getStone().getLiberties());
		assertEquals(3, board9x9.getIntersection(new Position(2, 2)).getStone().getLiberties());
		assertEquals(3, board9x9.getIntersection(new Position(1, 2)).getStone().getLiberties());
		assertEquals(3, board9x9.getIntersection(new Position(0, 1)).getStone().getLiberties());
	}
	
	/**
	 * Test that the liberties are correct after closed in group of stones 
	 * and that the group of stones is removed on the GoGUI.
	 */
	@Test
	public void testRemoveStonesWithZeroLibertiesMultipleStonesGoGUI() {
		boardGoGUI.setStone(1, 1, StoneColor.WHITE);
		boardGoGUI.setStone(2, 1, StoneColor.WHITE);
		boardGoGUI.setStone(1, 0, StoneColor.BLACK);
		boardGoGUI.setStone(2, 0, StoneColor.BLACK);
		boardGoGUI.setStone(3, 1, StoneColor.BLACK);
		boardGoGUI.setStone(2, 2, StoneColor.BLACK);
		boardGoGUI.setStone(1, 2, StoneColor.BLACK);
		boardGoGUI.setStone(0, 1, StoneColor.BLACK);
		assertNull(boardGoGUI.getIntersection(new Position(1, 1)).getStone());
		assertNull(boardGoGUI.getIntersection(new Position(2, 1)).getStone());
		assertEquals(2, boardGoGUI.getIntersection(new Position(1, 0)).getStone().getLiberties());
		assertEquals(2, boardGoGUI.getIntersection(new Position(2, 0)).getStone().getLiberties());
		assertEquals(4, boardGoGUI.getIntersection(new Position(3, 1)).getStone().getLiberties());
		assertEquals(3, boardGoGUI.getIntersection(new Position(2, 2)).getStone().getLiberties());
		assertEquals(3, boardGoGUI.getIntersection(new Position(1, 2)).getStone().getLiberties());
		assertEquals(3, boardGoGUI.getIntersection(new Position(0, 1)).getStone().getLiberties());
	}
	
	/**
	 * Test a draw.
	 */
	@Test
	public void testCalculateWinnerDraw() {
		board9x9.setStone(1, 1, StoneColor.BLACK);
		board9x9.setStone(4, 4, StoneColor.WHITE);
		board9x9.calculateWinner();
		assertEquals(1, board9x9.getBlackScore());
		assertEquals(1, board9x9.getWhiteScore());
	}
	
	/**
	 * Test winner with black stones.
	 */
	@Test
	public void testCalculateWinnerBlack() {
		board9x9.setStone(1, 1, StoneColor.BLACK);
		board9x9.calculateWinner();
		assertEquals(81, board9x9.getBlackScore());
		assertEquals(0, board9x9.getWhiteScore());
	}
	
	/**
	 * Test winner with white stones.
	 */
	@Test
	public void testCalculateWinnerWhite() {
		board9x9.setStone(1, 1, StoneColor.WHITE);
		board9x9.calculateWinner();
		assertEquals(0, board9x9.getBlackScore());
		assertEquals(81, board9x9.getWhiteScore());
	}
	
	/**
	 * Test winner with multiple areas.
	 */
	@Test
	public void testCalculateWinnerMultipleAreas() {
		board9x9.setStone(0, 1, StoneColor.BLACK);
		board9x9.setStone(1, 0, StoneColor.BLACK);
		board9x9.setStone(7, 7, StoneColor.WHITE);
		board9x9.setStone(8, 7, StoneColor.WHITE);
		board9x9.setStone(6, 8, StoneColor.WHITE);
		board9x9.calculateWinner();
		assertEquals(3, board9x9.getBlackScore());
		assertEquals(5, board9x9.getWhiteScore());
	}
	
	/**
	 * Test clearing the board.
	 */
	@Test
	public void testClear() {
		board9x9.setStone(1, 1, StoneColor.BLACK);
		board9x9.clear();
		assertNull(board9x9.getIntersection(new Position(1, 1)).getStone());
	}
	
	/**
	 * Test clearing the board on the GoGUI.
	 */
	@Test
	public void testClearGoGUI() {
		boardGoGUI.setStone(1, 1, StoneColor.BLACK);
		boardGoGUI.clear();
		assertNull(boardGoGUI.getIntersection(new Position(1, 1)).getStone());
	}
}
