package chess;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Bishop implements ChessPiece {

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
	/** List of squares that can be reached by this piece, used for check/check-mate functionality */
	private List<Square> moveList = new ArrayList<Square>();
	private boolean nextSquareBehindPiece;

	/**
	 * constructor for Bishop chess piece
	 * @param xStart x-component of piece's square
	 * @param yStart y-component of piece's square
	 * @param xC x-coordinate of piece
	 * @param yC y-coordinate of piece
	 * @param pc piece color
	 */
	public Bishop(int xStart, int yStart, int xC, int yC, Color pc) {
		xSquare = xStart;
		ySquare = yStart;
		xCoord = xC;
		yCoord = yC;
		pieceColor = pc;
		value = 3;
		populateMoves();
		nextSquareBehindPiece = false;
	}

	@Override
	public boolean canMove(int x2, int y2) {
		if (y2 - ySquare == 0) return false;
		int slope = (x2 - xSquare) / (y2 - ySquare);
		boolean validSquare = slope == 1 || slope == -1;
		return validSquare && !Chess.isOccupied(x2, y2) && !hasTravelIntersections(x2, y2);
	}

	@Override
	public boolean canCapture(int x2, int y2) {
		if (y2 - ySquare == 0) return false;
		int slope = (x2 - xSquare) / (y2 - ySquare);
		boolean validSquare = slope == 1 || slope == -1;
		return validSquare && Chess.isOccupied(x2, y2) && !Chess.getPiece(x2, y2).getColor().equals(pieceColor) && !hasTravelIntersections(x2, y2);
	}

	@Override
	public void move(int xDisplacement, int yDisplacement) {
		xCoord += xDisplacement;
		yCoord += yDisplacement;
		xSquare += (xDisplacement / Chess.getSquareDimension());
		ySquare += (yDisplacement / Chess.getSquareDimension());
		updateMoves();
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
		int slope = (y2 - ySquare) / (x2 - xSquare);
		boolean ne = false, nw = false, sw = false, se = false;
		boolean ne2 = false, nw2 = false, sw2 = false, se2 = false;
		if (slope == 1) {
			nw = true;
			se = true;
		} else {
			ne = true;
			sw = true;
		}
		if (y2 > ySquare) {
			if (nw) se2 = true;
			else if (ne) sw2 = true;
			nw = false;
			ne = false;
		} else {
			if (sw) ne2 = true;
			else if (se) nw2 = true;
			se = false;
			sw = false;
		}
		int difference = Math.abs(x2 - xSquare);
		for (int i = 1; i < difference; i++) {
			if (nw2) {
				if (Chess.isOccupied(xSquare - slope * i, ySquare - slope * i))
					return true;
			}
			if (ne2) {
				if (Chess.isOccupied(xSquare - slope * i, ySquare + slope * i))
					return true;
			}
			if (sw2) {
				if (Chess.isOccupied(xSquare + slope * i, ySquare - slope * i))
					return true;
			}
			if (se2) {
				if (Chess.isOccupied(xSquare + slope * i, ySquare + slope * i))
					return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean canSee(int x2, int y2) {
		int slope = (y2 - ySquare) / (x2 - xSquare);
		boolean ne = false, nw = false, sw = false, se = false;
		boolean ne2 = false, nw2 = false, sw2 = false, se2 = false;
		if (slope == 1) {
			nw = true;
			se = true;
		} else {
			ne = true;
			sw = true;
		}
		if (y2 > ySquare) {
			if (nw) se2 = true;
			else if (ne) sw2 = true;
			nw = false;
			ne = false;
		} else {
			if (sw) ne2 = true;
			else if (se) nw2 = true;
			se = false;
			sw = false;
		}
		int difference = Math.abs(x2 - xSquare);
		for (int i = 0; i < difference; i++) {
			if (nextSquareBehindPiece) return false;
			if (nw2) {
				if (Chess.isOccupied(xSquare - slope * (i + 1), ySquare - slope * (i + 1))) {
					nextSquareBehindPiece = true;
				}
			}
			if (ne2) {
				if (Chess.isOccupied(xSquare - slope * (i + 1), ySquare + slope * (i + 1))) {
					nextSquareBehindPiece = true;
				}
			}
			if (sw2) {
				if (Chess.isOccupied(xSquare + slope * (i + 1), ySquare - slope * (i + 1))) {
					nextSquareBehindPiece = true;
				}
			}
			if (se2) {
				if (Chess.isOccupied(xSquare + slope * (i + 1), ySquare + slope * (i + 1))) {
					nextSquareBehindPiece = true;
				}
			}
		}
		return true;
	}
	
	@Override
	public void populateMoves() {
		for (int i = 1; i < 8; i++) {
			if (xSquare - i >= 0 && ySquare - i >= 0 && canSee(xSquare - i, ySquare - i))
				moveList.add(new Square(xSquare - i, ySquare - i, false));
			nextSquareBehindPiece = false;
			if (xSquare - i >= 0 && ySquare + i <= 7 && canSee(xSquare - i, ySquare + i))
				moveList.add(new Square(xSquare - i, ySquare + i, false));
			nextSquareBehindPiece = false;
			if (xSquare + i <= 7 && ySquare - i >= 0 && canSee(xSquare + i, ySquare - i))
				moveList.add(new Square(xSquare + i, ySquare - i, false));
			nextSquareBehindPiece = false;
			if (xSquare + i <= 7 && ySquare + i <= 7 && canSee(xSquare + i, ySquare + i))
				moveList.add(new Square(xSquare + i, ySquare + i, false));
			nextSquareBehindPiece = false;
		}
	}
	
	@Override
	public void updateMoves() {
		List<Square> updatedPossibleMoves = new ArrayList<Square>();
		for (int i = 1; i < 8; i++) {
			if (xSquare - i >= 0 && ySquare - i >= 0 && canSee(xSquare - i, ySquare - i))
				updatedPossibleMoves.add(new Square(xSquare - i, ySquare - i, false));
			nextSquareBehindPiece = false;
			if (xSquare - i >= 0 && ySquare + i <= 7 && canSee(xSquare - i, ySquare + i))
				updatedPossibleMoves.add(new Square(xSquare - i, ySquare + i, false));
			nextSquareBehindPiece = false;
			if (xSquare + i <= 7 && ySquare - i >= 0 && canSee(xSquare + i, ySquare - i))
				updatedPossibleMoves.add(new Square(xSquare + i, ySquare - i, false));
			nextSquareBehindPiece = false;
			if (xSquare + i <= 7 && ySquare + i <= 7 && canSee(xSquare + i, ySquare + i))
				updatedPossibleMoves.add(new Square(xSquare + i, ySquare + i, false));
			nextSquareBehindPiece = false;
		}
		moveList = updatedPossibleMoves;
	}
	
	@Override
	public List<Square> getMoveList() {
		return moveList;
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

}
