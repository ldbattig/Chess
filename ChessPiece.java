package chess;

import java.awt.Color;
import java.util.List;

/**
 * Interface for universal chess piece methods with behavior described in comments
 * @author Lorenzo Battigelli
 */
public interface ChessPiece {
	
	/**
	 * Determines whether or not piece can move to specified square. Used for movement
	 * @param x2 of desired square
	 * @param y2 of desired square
	 * @return true (can move) or false (cannot move)
	 */
	public boolean canMove(int x2, int y2);
	
	/**
	 * Determines whether or not piece can capture at specified square. Used for capturing
	 * @param x2 of desired square
	 * @param y2 of desired square
	 * @return true (can capture) or false (cannot capture)
	 */
	public boolean canCapture(int x2, int y2);
	
	/**
	 * Determines whether or not piece can see specified square. Used for check/check-mate
	 * @param x2 of desired square
	 * @param y2 of desired square
	 * @return true (can see) or false (cannot see)
	 */
	public boolean canSee(int x2, int y2);
	
	/**
	 * Moves piece to specified square
	 * @param x2 of desired square
	 * @param y2 of desired square
	 */
	public void move(int xDisplacement, int yDisplacement);
	
	/**
	 * Captures piece at specified square. This method is used in the move() method
	 * @param x2 of desired square
	 * @param y2 of desired square
	 */
	public ChessPiece capture(int x2, int y2);
	
	/**
	 * Removes piece from board by setting coordinates/squares off screen
	 * @return removed ChessPiece
	 */
	public ChessPiece removePiece();
	
	/**
	 * Returns list of squares that the piece can reach. Used for check/checkmate functionality
	 * TODO: maybe use for highlighting reachable squares after click 0
	 * @return piece's move list
	 */
	public List<Square> getMoveList();
	
	/**
	 * Populates the piece's move list upon start of the game
	 */
	public void populateMoves();
	
	/**
	 * Updates the piece's move list upon moving the piece
	 */
	public void updateMoves();
	
	/**
	 * Returns x-component of piece's square
	 * @return xSquare
	 */
	public int getXSquare();
	
	/**
	 * Returns y-component of piece's square
	 * @return ySquare
	 */
	public int getYSquare();
	
	/**
	 * Returns x-coordinate of piece
	 * @return xCoord
	 */
	public int getXCoord();
	
	/**
	 * Returns y-coordinate of piece
	 * @return yCoord
	 */
	public int getYCoord();
	
	/**
	 * Returns Color of piece
	 * @return pieceColor
	 */
	public Color getColor();
	
	/**
	 * Returns value of piece
	 * @return value
	 */
	public int getValue();
	
}
