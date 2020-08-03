package chessPieces;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import chessControllers.Chess;
import chessObjects.Square;

/**
 * Rook object class. Includes universal behaviors from ChessPiece interface and rook-exclusive 
 * movement/capturing
 * @author Lorenzo Battigelli
 *
 */
public class Rook implements ChessPiece {

	/** x component of square array (0-7) */ 
	private int xSquare;
	/** y component of square array (0-7) */
	private int ySquare;
	/** x component of pixel array (0-479) */
	private int xCoord;
	/** y component of pixel array (0-479) */
	private int yCoord;
	/** boolean for canMove() and canCapture() methods */
	private Color pieceColor;
	/** Piece value for tracking instantaneous advantage and piece tracking - Pawn = 1, Bishop/Knight = 3, Rook = 5, Queen = 9 */
	private int value;
	/** Boolean used for castling */
	private boolean hasMoved;
	/** List of squares that can be reached by this piece, used for check/check-mate functionality */
	private List<Square> moveList = new ArrayList<Square>();
	
	/**
	 * Constructor for Rook chess piece
	 * @param xStart x-component of piece's square
	 * @param yStart y-component of piece's square
	 * @param xC x-coordinate of piece
	 * @param yC y-coordinate of piece
	 * @param pc piece color
	 */
	public Rook(int xStart, int yStart, int xC, int yC, Color pc) {
		xSquare = xStart;
		ySquare = yStart;
		xCoord = xC;
		yCoord = yC;
		pieceColor = pc;
		value = 5;
		hasMoved = false;
		populateMoves();
	}
	
	@Override
	public boolean canMove(int x2, int y2) {
		boolean validSquare = x2 == xSquare || y2 == ySquare;
		return validSquare && !Chess.isOccupied(x2, y2) && !hasTravelIntersections(x2, y2);
	}

	@Override
	public boolean canCapture(int x2, int y2) {
		boolean validSquare = x2 == xSquare || y2 == ySquare;
		return validSquare && Chess.isOccupied(x2, y2) && !hasTravelIntersections(x2, y2) && !Chess.getPiece(x2, y2).getColor().equals(pieceColor);
	}

	@Override
	public void move(int xDisplacement, int yDisplacement) {
		hasMoved = true;
		xCoord += xDisplacement;
		yCoord += yDisplacement;
		xSquare += (xDisplacement / Chess.getSquareDimension());
		ySquare += (yDisplacement / Chess.getSquareDimension());
	}

	@Override
	public ChessPiece capture(int x2, int y2) {
		return Chess.getPiece(x2, y2).removePiece();
	}

	@Override
	public ChessPiece removePiece() {
		//TODO: this is bad etiquette
		xSquare += 10;
		xCoord += 600;
		return this;
	}
	
	public boolean hasTravelIntersections(int x2, int y2) {
		boolean n = false, s = false, e = false, w = false;
		if (xSquare == x2) {
			n = true;
			s = true;
		} else {
			w = true;
			e = true;
		}
		if (y2 > ySquare) {
			n = false;
		} else if (x2 > xSquare) {
			w = false;
		} else if (ySquare > y2) {
			s = false;
		} else if (xSquare > x2) {
			e = false;
		}
		int difference = 0;
		if (n || s)
			difference = Math.abs(ySquare - y2);
		if (w || e)
			difference = Math.abs(xSquare - x2);
		for (int i = 1; i < difference; i++) {
			if (n) {
				if (Chess.isOccupied(xSquare, ySquare - i))
					return true;
			} if (s) {
				if (Chess.isOccupied(xSquare, ySquare + i))
					return true;
			} if (e) {
				if (Chess.isOccupied(xSquare + i, ySquare))
					return true;
			} if (w) {
				if (Chess.isOccupied(xSquare - i, ySquare))
					return true;
			}
		}
		return false;
	}

	@Override
	public void populateMoves() {
		for (int i = 1; i < 8; i++) {
			if (xSquare - i >= 0 && !hasTravelIntersections(xSquare - i, ySquare))
				moveList.add(new Square(xSquare - i, ySquare, false));
			if (xSquare + i <= 7 && !hasTravelIntersections(xSquare + i, ySquare))
				moveList.add(new Square(xSquare + i, ySquare, false));
			if (ySquare - i >= 0 && !hasTravelIntersections(xSquare, ySquare - i))
				moveList.add(new Square(xSquare, ySquare - i, false));
			if (ySquare + i <= 7 && !hasTravelIntersections(xSquare, ySquare + i))
				moveList.add(new Square(xSquare, ySquare + i, false));
		}
	}
	
	@Override
	public void updateMoves() {
		List<Square> updatedPossibleMoves = new ArrayList<Square>();
		for (int i = 1; i < 8; i++) {
			if (xSquare - i >= 0 && !hasTravelIntersections(xSquare - i, ySquare))
				updatedPossibleMoves.add(new Square(xSquare - i, ySquare, false));
			if (xSquare + i <= 7 && !hasTravelIntersections(xSquare + i, ySquare))
				updatedPossibleMoves.add(new Square(xSquare + i, ySquare, false));
			if (ySquare - i >= 0 && !hasTravelIntersections(xSquare, ySquare - i))
				updatedPossibleMoves.add(new Square(xSquare, ySquare - i, false));
			if (ySquare + i <= 7 && !hasTravelIntersections(xSquare, ySquare + i))
				updatedPossibleMoves.add(new Square(xSquare, ySquare + i, false));
		}
		moveList = updatedPossibleMoves;
	}
	
	@Override
	public List<Square> getMoveList() {
		return moveList;
	}

	@Override
	public boolean canSee(int x2, int y2) {
		boolean nextSquareBehindPiece = false;
		boolean n = false, s = false, e = false, w = false;
		if (xSquare == x2) {
			n = true;
			s = true;
		} else {
			w = true;
			e = true;
		}
		if (y2 > ySquare) {
			n = false;
		} else if (x2 > xSquare) {
			w = false;
		} else if (ySquare > y2) {
			s = false;
		} else if (xSquare > x2) {
			e = false;
		}
		int difference = 0;
		if (n || s)
			difference = Math.abs(ySquare - y2);
		if (w || e)
			difference = Math.abs(xSquare - x2);
		for (int i = 1; i < difference; i++) {
			if (n) {
				if (Chess.isOccupied(xSquare, ySquare - i)) {
					if (nextSquareBehindPiece) return true;
					nextSquareBehindPiece = true;
				}
			} if (s) {
				if (Chess.isOccupied(xSquare, ySquare + i)) {
					if (nextSquareBehindPiece) return true;
					nextSquareBehindPiece = true;
				}
			} if (e) {
				if (Chess.isOccupied(xSquare + i, ySquare)) {
					if (nextSquareBehindPiece) return true;
					nextSquareBehindPiece = true;
				}
			} if (w) {
				if (Chess.isOccupied(xSquare - i, ySquare)) {
					if (nextSquareBehindPiece) return true;
					nextSquareBehindPiece = true;
				}
			}
		}
		return false;
	}
	
	public void resetHasMoved() {
		hasMoved = false;
	}
	
	@Override
	public int getXSquare() {
		return xSquare;
	}

	@Override
	public int getYSquare() {
		return ySquare;
	}

	@Override
	public int getXCoord() {
		return xCoord;
	}

	@Override
	public int getYCoord() {
		return yCoord;
	}

	@Override
	public Color getColor() {
		return pieceColor;
	}

	@Override
	public int getValue() {
		return value;
	}
	
	public boolean getHasMoved() {
		return hasMoved;
	}
}
