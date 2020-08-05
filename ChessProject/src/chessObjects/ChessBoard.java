package chessObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Graphics for chess board including squares, square highlighting, and surrounding file/row
 * annotations (a-h and 1-8)
 * @author Lorenzo Battigelli
 *
 */
public class ChessBoard {

	/** Chess board square dimension */
	private int squareDimension = 60;
	/** Current board square color, used when painting squares */
	private Color current;
	/** Designated square to highlight (when clicked on), used when painting squares */
	private Square highlightSquare;
	//	private Square flashRedSquare = new Square(-1, -1, false);
	/** ArrayList of file characters, a-h or h-a depending on board orientation */
	private static List<Character> files = new ArrayList<Character>();
	/** ArrayList of row characters, 1-8 or 8-1 depending on board orientation */
	private static List<Character> rows = new ArrayList<Character>();
	/** ArrayList of 0s/1s representing board square colors, contents depend on board orientation */
	private static List<Integer> squareColors = new ArrayList<Integer>();
	private Color orientation;
	private Square promotionSquare;
	private BufferedImage promotionSpriteWhite = null;
	private BufferedImage promotionSpriteBlack = null;
	private Color promotionColor;
	/**
	 * ChessBoard constructor populating all lists from white's perspective
	 */
	public ChessBoard() {
		highlightSquare = new Square(-1, -1, false);
		promotionSquare = new Square(-1, -1, false);
		orientation = Color.white;
		// chess squares from white's perspective
		for (int i = 0; i < 64; i++) {
			if (i % 2 == 0) squareColors.add(0);
			else squareColors.add(1);
		}
		char fileChar = 'a';
		for (int i = 0; i < 8; i++) {
			files.add(fileChar++);
		}
		char rowChar = '1';
		for (int i = 0; i < 8; i++) {
			rows.add(rowChar++);
		}
		loadPromotionSprite();
	}

	public void paintBoard(Graphics g) {
		// set initial color depending on board orientation
		current = Color.white;
		g.setColor(current);
		// draws board rectangles
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// check if square needs to be highlighted
				if (j == highlightSquare.getXSquare() && i == highlightSquare.getYSquare()) {
					highlightSquare(g);
				// [unfinished] check if square should flash, aka king is in check and illegal move attempted
//				} else if (j == flashRedSquare.getxSquare() && i == flashRedSquare.getySquare()) {
//					flashRedSquare(g);
				// check if square should show promotion sprite
				} else if (j == promotionSquare.getXSquare() && i == promotionSquare.getYSquare()) {
					if (promotionColor.equals(Color.WHITE)) { // to show white promotion sprite
						if (promotionSquare.getYSquare() == 7) // board is in default orientation
							g.drawImage(promotionSpriteWhite, promotionSquare.getXSquare() * squareDimension, 
									promotionSquare.getYSquare() * squareDimension, null);
						// else board will be in flipped orientation
						else g.drawImage(promotionSpriteWhite, promotionSquare.getXSquare() * squareDimension, 
								promotionSquare.getYSquare() * squareDimension - 3 * squareDimension, null);
					}
					else { // to show black promotion sprite
						if (promotionSquare.getYSquare() == 0) // to show board from default orientation
							g.drawImage(promotionSpriteBlack, promotionSquare.getXSquare() * squareDimension, 
									promotionSquare.getYSquare() * squareDimension - 3 * squareDimension, null);
						// else board will be in flipped orientation
						else g.drawImage(promotionSpriteBlack, promotionSquare.getXSquare() * squareDimension, 
								promotionSquare.getYSquare() * squareDimension, null);
					}
				} else // otherwise fill square normally as white or black
					g.fillRect(20 + 60 * j, 20 + 60 * i, squareDimension, squareDimension);
				if (j != 7) // flip color if not on the row's last square
					g.setColor(flipColor());
			}
		}
		// set color to black for printing row/file characters
		g.setColor(Color.BLACK);

		// draws file/row characters
		g.drawString(Character.toString(files.get(0)), 45, 510);
		g.drawString(Character.toString(files.get(1)), 105, 510);
		g.drawString(Character.toString(files.get(2)), 165, 510);
		g.drawString(Character.toString(files.get(3)), 225, 510);
		g.drawString(Character.toString(files.get(4)), 285, 510);
		g.drawString(Character.toString(files.get(5)), 345, 510);
		g.drawString(Character.toString(files.get(6)), 405, 510);
		g.drawString(Character.toString(files.get(7)), 465, 510);

		g.drawString(Character.toString(rows.get(0)), 10, 52);
		g.drawString(Character.toString(rows.get(1)), 10, 112);
		g.drawString(Character.toString(rows.get(2)), 10, 172);
		g.drawString(Character.toString(rows.get(3)), 10, 232);
		g.drawString(Character.toString(rows.get(4)), 10, 292);
		g.drawString(Character.toString(rows.get(5)), 10, 352);
		g.drawString(Character.toString(rows.get(6)), 10, 412);
		g.drawString(Character.toString(rows.get(7)), 10, 472);
	}

	private void loadPromotionSprite() {
		try {
			promotionSpriteWhite = ImageIO.read(new File("ChessPieces/promotionWhitex60.png"));
			promotionSpriteBlack = ImageIO.read(new File("ChessPieces/promotionBlackx60.png"));
		} catch (IOException e) {
			System.out.println("Error while loading chess piece png files.");
		}
	}

	private Color flipColor() {
		if (current == Color.BLACK)
			return current = Color.WHITE;
		else
			return current = Color.BLACK;
	}

	public void highlight(Square sq) {
		highlightSquare = sq;
	}

	public void stopHighlight(Square sq) {
		highlightSquare = new Square(-1, -1, false);
	}

	private boolean isInitialColorWhite(Square sq) {
		return (squareColors.get(sq.getXSquare() + sq.getYSquare() * 7) == 0) ? true : false;
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

	public void flipBoard() {
		if (orientation == Color.BLACK)
			orientation = Color.WHITE;
		else
			orientation = Color.BLACK;
		Collections.reverse(rows);
		Collections.reverse(files);
	}

	public void promoteSquare(Square sq, Color pieceColor) {
		promotionSquare = sq;
		promotionColor = pieceColor;
	}
	
	public void closePromotionMenu() {
		promotionSquare = new Square (-1, -1, false);
	}

	public Color getOrientation() {
		return orientation;
	}

	public int getSquareDimension() {
		return squareDimension;
	}

	public Square getPromotionSquare() {
		return promotionSquare;
	}
	
	public Color getPromotionColor() {
		return promotionColor;
	}
}