package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(3, 6); 
		assertEquals(3, b.getWidth());
		assertEquals(6, b.getHeight());
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation(); 
	
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(true, b.getGrid(-1 , 10));
		assertEquals(true, b.getGrid(10 , 1));
		assertEquals(true, b.getGrid(1 , -1));
		assertEquals(true, b.getGrid(1 , 10));
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0)); 
		assertEquals(4, b.getColumnHeight(1)); 
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(3, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
		assertEquals(4, b.getMaxHeight());
	}
	
	@Test
	public void testSample3() {
		b.commit();
		b.place(sRotated, 1, 1);
		int c = b.clearRows();
		assertEquals(1, c);
		assertEquals(0 , b.getColumnHeight(0));
		assertEquals(3 , b.getColumnHeight(1));
		assertEquals(2 , b.getColumnHeight(2));
		b.undo();
		b.place(sRotated, 1, 1);
		b.commit();
		assertEquals(4, b.getMaxHeight());
		int result = b.place(new Piece(Piece.STICK_STR), 0, 1);
		assertEquals(Board.PLACE_ROW_FILLED, result);
		assertEquals(3, b.getRowWidth(0));
		assertEquals(3, b.getRowWidth(1));
		assertEquals(3, b.getRowWidth(2));
		assertEquals(2, b.getRowWidth(3));
		assertEquals(1, b.getRowWidth(4));
		assertEquals(5, b.getMaxHeight());
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		
		c = b.clearRows();
		assertEquals(3, c); 
		assertEquals(2, b.getColumnHeight(0));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(0, b.getColumnHeight(2));
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
		assertEquals(2, b.getMaxHeight());
		b.undo();
		assertEquals(4, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(2, b.getRowWidth(1));
		assertEquals(2, b.getRowWidth(2));
		assertEquals(1, b.getRowWidth(3));
	}
	
	@Test
	public void testSample4() {		
		b.commit();
		b.place(sRotated, 1, 1);
		b.commit();
		int result = b.place(pyr2, 1, 1);
		assertEquals(Board.PLACE_BAD, result);
		b.undo();
		result = b.place(pyr1, 1, 5);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();	
		
		assertEquals(4, b.getMaxHeight());
		assertEquals(1, b.getColumnHeight(0));
		
		result = b.place(pyr2, 1, 3);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(6, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(5));
		b.undo();
	}
	
	@Test
	public void testSample5() {
		b = new Board(4, 5);
		int h = b.dropHeight(new Piece(Piece.SQUARE_STR), 0);
		
		assertEquals(0, h);
		int result = b.place(new Piece(Piece.SQUARE_STR), 0, h);
		assertEquals(Board.PLACE_OK, result);
		b.clearRows();
		b.commit();
		int c = b.clearRows();
		assertEquals(0, c);
		b.commit();
		
		result = b.place(new Piece(Piece.L1_STR), 2, 0);
		assertEquals(Board.PLACE_ROW_FILLED, result); 
		b.commit();
		
		h = b.dropHeight(new Piece(Piece.PYRAMID_STR), 0);
		assertEquals(3, h);		
		b.place(new Piece(Piece.PYRAMID_STR), 0 , h);
		b.commit();
		result = b.place(new Piece(Piece.STICK_STR), 3, 1);
		assertEquals(Board.PLACE_ROW_FILLED, result);
		b.clearRows();
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(1, b.getColumnHeight(2));
		assertEquals(0, b.getRowWidth(3));
		assertEquals(2, b.getMaxHeight());
		b.undo();
		b.undo();
		System.out.println(b.toString());
		assertEquals(5, b.getMaxHeight());
		assertEquals(4, b.getColumnHeight(0));
	}
	
	@Test
	public void testSample6() {
		b = new Board(4, 5);
		int h = b.dropHeight(new Piece(Piece.L2_STR), 1);
		b.place(new Piece(Piece.L2_STR), 1, h);
		b.commit();
		
		h = b.dropHeight(new Piece(Piece.PYRAMID_STR), 0);
		assertEquals(3, h);	
	}
	@Test
	public void testSample7() {
		b = new Board(4, 5);
		b.place(new Piece(Piece.SQUARE_STR), 1, 0);
		b.commit();
		
//		System.out.println(b.toString()); 
		int h = b.dropHeight(new Piece(Piece.S1_STR), 0);
		assertEquals(2, h);
		
		b.place(new Piece(Piece.S1_STR), 0 , 2);
//		System.out.println(b.toString());
		
	}
	
	
	
	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	
	
}
