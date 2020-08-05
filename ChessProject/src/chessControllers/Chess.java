package chessControllers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chessObjects.ChessBoard;
import chessObjects.Move;
import chessObjects.Square;
import chessPieces.Bishop;
import chessPieces.ChessPiece;
import chessPieces.Knight;
import chessPieces.Pawn;
import chessPieces.Queen;
import chessPieces.Rook;

import javax.swing.JOptionPane;

/**
 * Controller for chess game. Includes Instances of ChessBoard and ChessPieceController. This class
 * registers user clicks and processes them through ChessBoard/ChessPieceController
 * @author Lorenzo Battigelli
 *
 */
public class Chess extends JPanel {

	/** Id declaration to avoid checkstyle warnings*/
	private static final long serialVersionUID = 1L;
	/** Width in pixels of border surrounding chess board */
	private int borderWidth = 20;
	/** Dimension in pixels of chess board square */
	private static int squareDimension = 60;
	/** X dimension in pixels of canvas window */
	private int canvasDimensionY = getSquareDimension() * 8 + borderWidth * 2;
	/** Y dimension in pixels of canvas window */
	private int canvasDimensionX = getSquareDimension() * 12 + borderWidth * 2;
	/** Instance of ChessPieces used to display piece graphics */
	private static ChessPieceController pieceGraphics = new ChessPieceController();
	/** Instance of ChessBoard used to display board graphics */
	private static ChessBoard boardGraphics = new ChessBoard();
	/** 2D array of Squares used to determine piece occupation (boolean) */
	private static Square[][] squares = new Square[8][8];
	/** Label used for detecting mouse clicks */
	private static Label mouseLabel;
	/** Number representing whether click will select piece to move (0) or square to move piece to (1) */
	private static int clickNum = 0;
	/** Square representing location of first click. This is used in functionality after second click is made */
	private static Square firstClick;
	/** Square representing location of last move's second click. This is used in functionality for en-passant */
	private static Square lastClick;
	/** Color representing current turn to move */
	private static Color turn;
	/** Color representing opposite side (whose turn it isn't) */
	private static Color oppositeTurn;
	/** Boolean for whether or not either king is in check */
	private static boolean check;
	/** Boolean for debugging (used for printing variables) */
	public static boolean debug;
	/** Stack of Moves before the current board position, used for navigating through the game */
	private static Stack<Move> movesBefore;
	/** Stack of Moves after the current board position, used for navigating through the game */
	private static Stack<Move> movesAfter;
	private static boolean promoting;

	/**
	 * Constructor - initializes all fields, MouseListener, and pieces
	 */
	public Chess() {
		turn = Color.WHITE;
		oppositeTurn = Color.BLACK;
		setupSquares();
		setupClicks();
		pieceGraphics.populatePieces();
		check = false;
		movesBefore = new Stack<Move>();
		movesAfter = new Stack<Move>();
		promoting = false;
		debug = true;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(canvasDimensionX, canvasDimensionY);
	}

	/**
	 * Setup for graphical window and click detection
	 */
	private static void createAndShowGui() {
		Chess ch = new Chess();
		JPanel buttonPanel = new JPanel();
		JFrame gameFrame = new JFrame("Chess");
		JButton flipBoardButton = new JButton("Flip board");
		buttonPanel.add(flipBoardButton);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.getContentPane().add(ch);
		gameFrame.pack();
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setVisible(true);
		gameFrame.add(buttonPanel, BorderLayout.EAST);
		gameFrame.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				mouseClick(e.getX(), e.getY());
				gameFrame.repaint();
			}                
		});
		flipBoardButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
			    flipBoard();
			    gameFrame.repaint();
			  } 
			} );
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		boardGraphics.paintBoard(g);
		pieceGraphics.paintPieces(g);
	}

	/**
	 * Functionality for after click is detected. Main flows are: click 0 - select piece and click 1 - select square
	 * @param x x-coordinate pixel of click
	 * @param y y-coordinate pixel of click
	 */
	private static void mouseClick(int x, int y) {
		//offsets are used to move origin (0,0) to top left of chess board
		printOccupied();
		x -= 27;
		y -= 50;
	//	System.out.println("xPos: " + x + '\n' + "yPos: " + y);
		if (x < 0 || x >= 480 || y < 0 || y >= 480)
			return;
		Square newSquare = coordToSquare(x, y);
	//	System.out.println(getPiece(newSquare).getClass().toString() + isOccupied(newSquare.getXSquare(), newSquare.getYSquare()));
		if (promoting) {
			Color orientation = boardGraphics.getOrientation();
			Color newPieceColor = boardGraphics.getPromotionColor();
			Square promoSquare = boardGraphics.getPromotionSquare();
			Square above1 = null, above2 = null, above3 = null, below1 = null, below2 = null, below3 = null;
			if (newPieceColor.equals(Color.BLACK) && orientation.equals(Color.WHITE) || orientation.equals(Color.BLACK) && newPieceColor.equals(Color.WHITE)) {
				above1 = squares[promoSquare.getXSquare()][promoSquare.getYSquare() - 1];
				above2 = squares[promoSquare.getXSquare()][promoSquare.getYSquare() - 2];
				above3 = squares[promoSquare.getXSquare()][promoSquare.getYSquare() - 3];
			} else {
				below1 = squares[promoSquare.getXSquare()][promoSquare.getYSquare() + 1];
				below2 = squares[promoSquare.getXSquare()][promoSquare.getYSquare() + 2];
				below3 = squares[promoSquare.getXSquare()][promoSquare.getYSquare() + 3];
			}
			char type = ' ';
			if (newPieceColor.equals(Color.WHITE)) {
				if (boardGraphics.getOrientation().equals(Color.WHITE)) {
					if (newSquare.equalsXY(promoSquare)) type = 'k';
					if (newSquare.equalsXY(below1)) type = 'b';
					if (newSquare.equalsXY(below2)) type = 'r';
					if (newSquare.equalsXY(below3)) type = 'q';
				} else {
					if (newSquare.equalsXY(promoSquare)) type = 'q';
					if (newSquare.equalsXY(above1)) type = 'r';
					if (newSquare.equalsXY(above2)) type = 'b';
					if (newSquare.equalsXY(above3)) type = 'k';
				}
			} else {
				if (boardGraphics.getOrientation().equals(Color.WHITE)) {
					if (newSquare.equalsXY(promoSquare)) type = 'q';
					if (newSquare.equalsXY(above1)) type = 'r';
					if (newSquare.equalsXY(above2)) type = 'b';
					if (newSquare.equalsXY(above3)) type = 'k';
				} else {
					if (newSquare.equalsXY(promoSquare)) type = 'k';
					if (newSquare.equalsXY(below1)) type = 'b';
					if (newSquare.equalsXY(below2)) type = 'r';
					if (newSquare.equalsXY(below3)) type = 'q';
				}
			}
			promoteReplace(newPieceColor, type);
			return;
		}
		switch(click()) {
		case 1:
	//		System.out.println("Click one.");
	//		System.out.println("Secondclick x: " + newSquare.getxSquare() + " Secondclick y: " + newSquare.getySquare());
			move(firstClick, newSquare);
			stopHighlight(firstClick);
			if (pieceGraphics.checkmate()) {
				String side = (turn.equals(Color.BLACK)) ? "White": "Black"; 
				popupMessage(side + " wins.", "Checkmate");
			}
			break;
		case 0:
	//		System.out.println("Click zero.");
			firstClick = newSquare;
			highlight(firstClick);
	//		if (Chess.debug) printSightSquares(firstClick);
	//		System.out.println("Firstclick x: " + firstClick.getxSquare() + " Firstclick y: " + firstClick.getySquare());
			break;
		default:
			throw new IllegalArgumentException("Error: clickNum is invalid");
		}
	}

	/**
	 * Creates label across chess board for click detection
	 */
	private void setupClicks() {
		mouseLabel = new Label();        
		mouseLabel.setAlignment(Label.CENTER);
		mouseLabel.setSize(480, 480);
	}

	/**
	 * Determines whether click is to select piece to move, or square to move selected piece to
	 * @return old clickNum value
	 */
	private static int click() {
		return clickNum == 0 ? clickNum++ : clickNum--;
	}
	
	/**
	 * Converts pixel x, y (0-479) to chess board square x, y (0-7)
	 * @param x x component of pixel
	 * @param y y component of pixel
	 * @return Square of converted x, y
	 */
	private static Square coordToSquare(int x, int y) {
		int xS = (int) Math.ceil(x / getSquareDimension());
		int yS = (int) Math.ceil(y / getSquareDimension());
		return squares[xS][yS];
		//System.out.println("xS: " + xS + '\n' + "yS: " + yS);
	}
	
	/**
	 * Returns whether or not specified square has piece or not
	 * @param x x component of chess board square
	 * @param y x component of chess board square
	 * @return true if occupied, else false
	 */
	public static boolean isOccupied(int x, int y) {
		if (x > 7 || y > 7 || x < 0 || y < 0) return false;
		return squares[x][y].isOccupied();
	}
	
	/**
	 * Returns the square of specified coordinates
	 * @param x coordinate of square
	 * @param y coordinate of square
	 * @return square of said coordinates
	 */
	public Square getSquare(int x, int y) {
		if (x > 7 || y > 7 || x < 0 || y < 0) return null;
		return squares[x][y];
	}
	
	/**
	 * Initializes board squares with characteristics of new chess game
	 */
	private void setupSquares() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				squares[i][j] = new Square(i, j, j < 6 && j > 1 ? false : true);
			}
		}
	}
	
	/**
	 * Universal movement for chess pieces
	 * @param start square of selected piece
	 * @param end square for piece to move to
	 * @return true if piece moves, false otherwise
	 */
	private static boolean move(Square start, Square end) {
		return pieceGraphics.movePiece(start.getXSquare(), start.getYSquare(), end.getXSquare(), end.getYSquare());
	}
	
	/**
	 * Returns the dimension of a single board square in pixels
	 * @return squareDimension
	 */
	public static int getSquareDimension() {
		return squareDimension;
	}
	
	/**
	 * Returns the piece of specified square coordinates
	 * @param x2 x coordinate of square to find piece
	 * @param y2 y coordinate of square to find piece
	 * @return piece if one exists, otherwise a blue pawn to avoid NPE
	 */
	public static ChessPiece getPiece(int x2, int y2) {
		return pieceGraphics.getPiece(x2, y2);
	}
	
	/**
	 * Returns piece of specified square
	 * @param sq square to find piece on
	 * @return piece if one exists, otherwise a blue pawn to avoid NPE
	 */
	public static ChessPiece getPiece(Square sq) {
		return getPiece(sq.getXSquare(), sq.getYSquare());
	}
	
	/**
	 * Sets square (x1, y1) occupied to false and square (x2, y2) occupied to true. This is used
	 * in chess piece movement functionality
	 * @param x1 x coordinate of square piece is moving from
	 * @param y1 y coordinate of square piece is moving from
	 * @param x2 x coordinate of square piece is moving to
	 * @param y2 y coordinate of square piece is moving to
	 */
	public static void switchOccupation(int x1, int y1, int x2, int y2) {
		squares[x1][y1].setOccupied(false);
		squares[x2][y2].setOccupied(true);
	}
	
	public static void setOccupation(int x, int y, boolean occupied) {
		squares[x][y].setOccupied(occupied);
	}
	
	/**
	 * Returns the square that was last clicked. Used for en-passant
	 * @return lastClick square
	 */
	public static Square getLastClick() {
		return lastClick;
	}
	
	/**
	 * [Debugging method] Prints all chess board square's occupied field
	 */
	public static void printOccupied() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print("(" + j + ", " + i + ") " + squares[j][i].isOccupied() + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Sets the lastClick field to a passed square
	 * @param sq square to set lastClick to
	 */
	public static void assignLastClick(Square sq) {
		lastClick = sq;
	}
	
	/**
	 * Highlights the passed square in either dark gray or light gray - whichever is
	 * closest to the square's initial color
	 * @param sq square to highlight
	 */
	public static void highlight(Square sq) {
		boardGraphics.highlight(sq);
	}
	
	/**
	 * Returns square to initial color
	 * @param sq square to stop highlighting
	 */
	public static void stopHighlight(Square sq) {
		boardGraphics.stopHighlight(sq);
	}
	
	/**
	 * Flips turn field to be opposite color
	 */
	public static void takeTurn() {
		turn = (turn.equals(Color.WHITE)) ? Color.BLACK: Color.WHITE; 
		oppositeTurn = (oppositeTurn.equals(Color.WHITE)) ? Color.BLACK: Color.BLACK; 
	}
	
	/**
	 * Returns the current turn color
	 * @return turn color
	 */
	public static Color getTurn() {
		return turn;
	}
	
	public static Color getOppositeTurn() {
		return oppositeTurn;
	}
	
	/**
	 * Returns true if specified square is not attacked by piece of opposite color as kingColor. Used
	 * in king movement functionality
	 * @param x2 x coordinate of square
	 * @param y2 y coordinate of square
	 * @param kingColor color of king
	 * @return true if not attacked, false otherwise
	 */
	public static boolean notAttacked(int x2, int y2, Color kingColor) {
		return ChessPieceController.notAttacked(x2, y2, kingColor);
	}
	
	/**
	 * Returns the King chess piece of specified color
	 * @param kingColor color of king to find
	 * @return king of passed color
	 */
	public static ChessPiece findKing(Color kingColor) {
		return ChessPieceController.findKing(kingColor);
	}
	
	/**
	 * Returns the check field, representing if either king is in check
	 * @return check boolean
	 */
	public static boolean getCheck() {
		return check;
	}
	
	/**
	 * TODO
	 * Text chess move inputs (algebraic notation)
	 * @param move the move string to parse
	 */
	public void textMove(String move) {
		if (move.length() < 2 ||  move.length() > 5)
			throw new IllegalArgumentException("Error: invalid text move");
		// 
	}
	
	/**
	 * [Debugging method] Prints the threatened squares of the piece of a passed square
	 * @param viewpoint square to print piece's threatened squares from
	 */
	public static void printSightSquares(Square viewpoint) {
		ChessPiece temp = getPiece(viewpoint);
		for (int i = 0; i < temp.getMoveList().size(); i++) {
			System.out.println(temp.getColor().toString() + " " + temp.getClass().toString() + 
					" of " + viewpoint.toString() + " can see " + temp.getMoveList().get(i).toString());
		}

	}
	
	public static void addMoveToStack(Move m) {
		movesBefore.push(m);
		movesAfter.clear();
	}
	
	public static void nextMove() {
		if (movesAfter.size() == 0)
			return;
		movesBefore.push(movesAfter.pop()).redo();
	}
	
	public static void lastMove() {
		if (movesBefore.size() == 0)
			return;
		movesAfter.push(movesBefore.pop()).undo();
	}
	
	public static void inCheck() {
		check = true;
	}
	
	public static void notInCheck() {
		check = false;
	}
	
	public static boolean kingInCheck(Color kingColor) {
		return pieceGraphics.kingInCheck(kingColor);
	}
	
//	public static void flashRed(Square sq) {
//		boardGraphics.flashRed(sq);
//	}
	
	public static void popupMessage(String messageText, String titleText)
    {
        JOptionPane.showMessageDialog(null, messageText, titleText, JOptionPane.INFORMATION_MESSAGE);
    }
	
	public static void flipBoard() {
		boardGraphics.flipBoard();
		pieceGraphics.flipBoard();
		flipOccupation();
		ChessPieceController.updatePieceLists();
	}
	
	public static Color getOrientation() {
		return boardGraphics.getOrientation();
	}
	
	public static void flipOccupation() {
		boolean[][] oldOcc = new boolean[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				oldOcc[i][j] = isOccupied(i, j);
			}
		}
		int max = 7;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				squares[max - i][max - j].setOccupied(oldOcc[i][j]);
			}
		}
		stopHighlight(firstClick);
//		if (firstClick != null) firstClick = new Square(max - firstClick.getXSquare(), max - firstClick.getYSquare(), firstClick.isOccupied());
//		if (lastClick != null) lastClick = new Square(max - lastClick.getXSquare(), max - lastClick.getYSquare(), lastClick.isOccupied());
	}
	
	public static void promotePawn(Pawn p) {
		//open graphic for square cutting into quarters, using letters
		boardGraphics.promoteSquare(new Square(p.getXSquare(), p.getYSquare(), false), p.getColor());
		promoting = true;
		p.removePiece();
	}
	
	public static void promoteReplace(Color c, char type) {
		ChessPiece newPiece = null;
		int newX = boardGraphics.getPromotionSquare().getXSquare();
		int newY = boardGraphics.getPromotionSquare().getYSquare();
		switch(type) {
		case ('b'): newPiece = new Bishop(newX, newY, newX * squareDimension, newY * squareDimension, c);
			break;
		case ('k'): newPiece = new Knight(newX, newY, newX * squareDimension, newY * squareDimension, c);
			break;
		case ('r'): newPiece = new Rook(newX, newY, newX * squareDimension, newY * squareDimension, c);
			break;
		case ('q'): newPiece = new Queen(newX, newY, newX * squareDimension, newY * squareDimension, c);
			break;
		default:
			stopHighlight(firstClick);
			boardGraphics.closePromotionMenu();
			break;
		}
		ChessPieceController.putPieceOnBoard(newPiece);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}
}
