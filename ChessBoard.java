package chess;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ChessBoard {

	// Chess board vars
	private int squareDimension = 60;

	private Color white = Color.white;
	private Color black = Color.black;
	private Color current;
	private Square highlightSquare = new Square(-1, -1, false);
//	private Square flashRedSquare = new Square(-1, -1, false);

	private static String[] files = {"a", "b", "c", "d", "e", "f", "g", "h"};
	private static String[] rows = {"1", "2", "3", "4", "5", "6", "7", "8"};

	private static Integer[] squareColors = {0, 1, 0, 1, 0, 1, 0, 1,
			1, 0, 1, 0, 1, 0, 1, 0,
			0, 1, 0, 1, 0, 1, 0, 1,
			1, 0, 1, 0, 1, 0, 1, 0,
			0, 1, 0, 1, 0, 1, 0, 1,
			1, 0, 1, 0, 1, 0, 1, 0,
			0, 1, 0, 1, 0, 1, 0, 1,
			1, 0, 1, 0, 1, 0, 1, 0,};

	public void paintBoard(Graphics g) {
		// draws board rectangles
		g.setColor(white);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// TODO: re-factor highlighting for efficiency; this is very inefficient
				if (j == highlightSquare.getXSquare() && i == highlightSquare.getYSquare()) {
					highlightSquare(g);
//				} else if (j == flashRedSquare.getxSquare() && i == flashRedSquare.getySquare()) {
//					flashRedSquare(g);
				} else
					g.fillRect(20 + 60 * j, 20 + 60 * i, squareDimension, squareDimension);
				if (j != 7)
					g.setColor(flipColor());
			}
		}
		g.setColor(black);

		// draws file/row characters
		g.drawString(files[0], 45, 510);
		g.drawString(files[1], 105, 510);
		g.drawString(files[2], 165, 510);
		g.drawString(files[3], 225, 510);
		g.drawString(files[4], 285, 510);
		g.drawString(files[5], 345, 510);
		g.drawString(files[6], 405, 510);
		g.drawString(files[7], 465, 510);

		g.drawString(rows[7], 10, 52);
		g.drawString(rows[6], 10, 112);
		g.drawString(rows[5], 10, 172);
		g.drawString(rows[4], 10, 232);
		g.drawString(rows[3], 10, 292);
		g.drawString(rows[2], 10, 352);
		g.drawString(rows[1], 10, 412);
		g.drawString(rows[0], 10, 472);
	}

	private Color flipColor() {
		if (current == black)
			current = white;
		else
			current = black;
		return current;
	}

	// TODO: wrong colors are used in highlighting
	public void highlight(Square sq) {
		highlightSquare = sq;
	}

	public void stopHighlight(Square sq) {
		highlightSquare = new Square(-1, -1, false);
	}

	private boolean isInitialColorWhite(Square sq) {
		return (squareColors[sq.getXSquare() + sq.getYSquare() * 8] == 0) ? true : false;
	}

	private void highlightSquare(Graphics g) {
		Color previous = current;
		Color highlighter = isInitialColorWhite(highlightSquare) ? Color.LIGHT_GRAY : Color.DARK_GRAY;
		g.setColor(highlighter);
		g.fillRect(highlightSquare.getXSquare() * squareDimension + 20, highlightSquare.getYSquare() * squareDimension + 20, squareDimension, squareDimension);
		current = previous;
		g.setColor(current);
	}

//	public void flashRed(Square sq) {
//		flashRedSquare = sq;
//	}

//	private void flashRedSquare(Graphics g) {
//		Color previous = current;
//		g.setColor(Color.RED);
//		g.fillRect(flashRedSquare.getxSquare() * squareDimension + 20, flashRedSquare.getySquare() * squareDimension + 20, squareDimension, squareDimension);
//		current = previous;
//		g.setColor(current);
//		//TODO: this goes forever
//		TimerTask task = new TimerTask() {
//	        public void run() {
//	            flashRedSquare = new Square(-1, -1, false);
//	        }
//	    };
//	    Timer timer = new Timer("Timer");
//	    
//	    long delay = 0L;
//	    timer.schedule(task, delay);
//	}

	public int getSquareDimension() {
		return squareDimension;
	}
}