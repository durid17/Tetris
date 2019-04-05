package tetris;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import com.sun.xml.internal.rngom.digested.DElementPattern;

public class JBrainTetris extends JTetris{
	protected JCheckBox brainMode;
	protected JCheckBox animateFall;
	protected JSlider adversary;
	protected JLabel adversaryMessage;
	protected static Brain brain;
	protected int localCount;
	protected Brain.Move next;
	
	JBrainTetris(int pixels) {
		super(pixels);
	}
	
	@Override
	public Piece pickNextPiece() {
		int ran = random.nextInt(99) + 1;
		if(ran >= adversary.getValue()) {
			adversaryMessage.setText("ok");
			return super.pickNextPiece();
		}else {
			adversaryMessage.setText("*ok*");
			Brain.Move worst = null;
			for(int i = 0 ; i < pieces.length; i++) {
				Brain.Move curr = brain.bestMove(board, pieces[i], board.getHeight() - 4, null);
				if( curr != null && (worst == null || curr.score > worst.score)) worst = curr;
			}
			if(worst == null) return null;
			return worst.piece;
		}
	}
	
	@Override
	public JComponent createControlPanel() {
		JPanel panel = (JPanel)super.createControlPanel();
		panel.add(new JLabel("Brain:"));
		brainMode = new JCheckBox("Brain active");
		panel.add(brainMode);
		JPanel little = new JPanel();
		little.add(new JLabel("Adversary:"));
		adversary = new JSlider(0, 100, 0); // min, max, current
		adversary.setPreferredSize(new Dimension(100,15));
		little.add(adversary);
		panel.add(little);
		adversaryMessage = new JLabel("ok");
		panel.add(adversaryMessage);
		return panel;
	}
	
	@Override
	public void tick(int verb) {
		if(!gameOn) return;
		if(brainMode.isSelected()) {
			if (currentPiece != null) {
				board.undo();
			}
			if(localCount != super.count) {
				next = brain.bestMove(board, currentPiece, board.getHeight() - 4, next);
				localCount = super.count;
			}
			makeBestMove();
		}
		super.tick(verb);
	}
	

	private void makeBestMove() {
		if(next == null) return;
		if( !next.piece.equals(currentPiece)) {
			super.tick(ROTATE);
		}
		if(next.x > currentX) {
			super.tick(RIGHT);
		}else if(next.x < currentX) {
			super.tick(LEFT);
		}
	}

	public static void main(String[] args) {
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception ignored) { }
		brain = new DefaultBrain();
		JTetris brainTetris = new JBrainTetris(16);
		JFrame frame = JTetris.createFrame(brainTetris);
		frame.setVisible(true);
	}
	
}
