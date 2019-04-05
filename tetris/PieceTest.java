package tetris;

import static org.junit.Assert.*;
import java.util.*;

import javax.print.attribute.standard.NumberOfDocuments;

import org.junit.*;

//import com.sun.corba.se.impl.naming.cosnaming.NamingUtils;

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
	private Piece[] pyr , s1 , s2 , stick , l1 , l2 , square , pieces;	
	private final int ROTATION_NUMBER = 4;

	@Before
	public void setUp() throws Exception {
		stick = new Piece[ROTATION_NUMBER];
		computeAllRotation(Piece.STICK_STR , stick);
		s1 = new Piece[ROTATION_NUMBER];
		computeAllRotation(Piece.S1_STR , s1);
		s2 = new Piece[ROTATION_NUMBER];
		computeAllRotation(Piece.S2_STR , s2);
		l1 = new Piece[ROTATION_NUMBER];
		computeAllRotation(Piece.L1_STR , l1);
		l2 = new Piece[ROTATION_NUMBER];
		computeAllRotation(Piece.L2_STR , l2);
		square = new Piece[ROTATION_NUMBER];
		computeAllRotation(Piece.SQUARE_STR , square);
		pyr = new Piece[ROTATION_NUMBER];
		computeAllRotation(Piece.PYRAMID_STR , pyr);
		pieces = Piece.getPieces();
		
	}
	
	private void computeAllRotation(String start, Piece[] rot) {
		rot[0] = new Piece(start);
		for(int i = 1 ; i < ROTATION_NUMBER; i++) {
			rot[i] = rot[i-1].computeNextRotation();			
		}
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
	
	@Test
	public void testSize1() {  
		// Check size of pyr piece
		assertEquals(3, s1[0].getWidth());  
		assertEquals(2, s1[0].getHeight()); 
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, s1[3].getWidth());
		assertEquals(3, s1[3].getHeight());
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
	
	@Test
	public void testSSkirt2() {
		assertTrue(Arrays.equals(new int[] {0}, stick[0].getSkirt()));
		assertTrue(Arrays.equals(new int[] {0 , 0 , 0 , 0}, stick[1].getSkirt()));
		assertTrue(Arrays.equals(stick[0].getSkirt(), stick[2].getSkirt()));
		assertTrue(Arrays.equals(stick[1].getSkirt(), stick[3].getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0}, l1[0].getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0 , 0}, l1[1].getSkirt()));
		assertTrue(Arrays.equals(new int[] {2, 0}, l1[2].getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1 , 1}, l1[3].getSkirt()));
	}
	
	@Test
	public void testEquals1() { 
		assertTrue(stick[0].equals(stick[2]));
		assertTrue(stick[1].equals(stick[3]));
		
		assertTrue(square[0].equals(square[1]));
		assertTrue(square[0].equals(square[2]));
		assertTrue(square[0].equals(square[3]));
	}
	
	@Test
	public void testEquals2() {
		TPoint[] p1 = new TPoint[4];
		p1[0] = new TPoint(0 , 0);
		p1[1] = new TPoint(1 , 0);
		p1[2] = new TPoint(1 , 1);
		p1[3] = new TPoint(2 , 1);
		Piece piece1 = new Piece(p1);
		
		TPoint[] p2 = new TPoint[4];
		p2[0] = new TPoint(2 , 1);
		p2[1] = new TPoint(1 , 1);
		p2[2] = new TPoint(0 , 0);
		p2[3] = new TPoint(1 , 0);
		Piece piece2 = new Piece(p2);  
		
		assertTrue(piece1.equals(piece2));
		
		assertTrue(piece1.equals(piece1));
		boolean res = !piece1.equals(null);
		assertTrue(res);
		
		p1 = new TPoint[5];
		p1[0] = new TPoint(2 , 1);
		p1[1] = new TPoint(1 , 1);
		p1[2] = new TPoint(0 , 0);
		p1[3] = new TPoint(1 , 0);
		p1[4] = new TPoint(4 , 4);
		piece1 = new Piece(p1);
		res = !piece2.equals(piece1); 
		assertTrue(res);
		
		
	}
	
	
	@Test
	public void testRotation() { 
		
		String str = "1 1 0 0 1 0 0 1";
		assertTrue(square[0].equals(new Piece(str)));
		assertTrue(square[1].equals(new Piece(str)));
		assertTrue(square[0].computeNextRotation().equals(new Piece(str)));
		
		str = "0 0 0 2 0 1 1 0";
		assertTrue(l1[0].equals(new Piece(str)));
		
	}
	
	@Test
	public void testFastRotation() {		
		String str; 
		str = "2 0 3 0 1 0 0 0";
		assertTrue(pieces[Piece.STICK].fastRotation().equals(new Piece(str)));
		
		// strick
		assertTrue(pieces[Piece.STICK].fastRotation().fastRotation().equals(pieces[Piece.STICK]));
		//s1
		assertTrue(pieces[Piece.S1].fastRotation().fastRotation().equals(pieces[Piece.S1]));
		//s2
		assertTrue(pieces[Piece.S2].fastRotation().fastRotation().equals(pieces[Piece.S2]));
		
		//square
		assertTrue(pieces[Piece.SQUARE].fastRotation().fastRotation().equals(pieces[Piece.SQUARE]));
		
		//l1
		Piece L1 = pieces[Piece.L1];
		Piece L2 = pieces[Piece.L2];
		Piece pyramid = pieces[Piece.PYRAMID];
		for(int i = 0 ; i < ROTATION_NUMBER ; i++) {
			L1 = L1.fastRotation();
			L2 = L2.fastRotation();
			pyramid = pyramid.fastRotation();
		}
		assertTrue(L1.equals(pieces[Piece.L1]));
		assertTrue(L2.equals(pieces[Piece.L2]));
		assertTrue(pyramid.equals(pieces[Piece.PYRAMID]));
		
		pyramid = pyramid.fastRotation();
		assertTrue(pyr[1].equals(pyramid));
		
		
	}
	
}
