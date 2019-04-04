package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// every possible rotation of each piece
	private Piece[] pyr , s1 , s2 , stick , l1 , l2 , square;	

	@Before
	public void setUp() throws Exception {
//		stick = new Piece[4];
		computeAllRotation(Piece.STICK_STR , stick);
//		s1 = new Piece[4];
		computeAllRotation(Piece.S1_STR , s1);
		computeAllRotation(Piece.S2_STR , s2);
		computeAllRotation(Piece.L1_STR , l1);
		computeAllRotation(Piece.L2_STR , l2);
		computeAllRotation(Piece.SQUARE_STR , square);
//		computeAllRotation(Piece.PYRAMID_STR , pyr);
		 
		pyr = new Piece[4];
		pyr[0] = new Piece(Piece.PYRAMID_STR);
		pyr[1] = pyr[0].computeNextRotation();
		pyr[2] = pyr[1].computeNextRotation();
		pyr[3] = pyr[2].computeNextRotation();
	}
	
	private void computeAllRotation(String start, Piece[] rot) {
//		rot = new Piece[4];
		rot[0] = new Piece(start);
		rot[1] = rot[0].computeNextRotation();
		rot[2] = rot[1].computeNextRotation();
		rot[3] = rot[2].computeNextRotation();
	} 
	
	// Here are some sample tests to get you started
	
	@Test
	public void testSampleSize() {  
		// Check size of pyr piece
		assertEquals(3, pyr[0].getWidth());  
		assertEquals(2, pyr[0].getHeight()); 
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr[1].getWidth());
		assertEquals(3, pyr[1].getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	} 
	
	
	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr[0].getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr[2].getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s1[0].getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, s1[1].getSkirt()));
	}
	
//	
//	@Test
//	public void testSSkirt2() {
//		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
//		// right for arrays.
//		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr[0].getSkirt()));
//		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr[2].getSkirt()));
//		
//		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s1[0].getSkirt()));
//		assertTrue(Arrays.equals(new int[] {1, 0}, s1[1].getSkirt()));
//	}
	
}
