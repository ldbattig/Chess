package chess;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Queen implements ChessPiece {

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
	/** Temporary (not actually on board) piece used for Queen movement (queen = bishop + rook) */
	private ChessPiece tempRook;
	/** Temporary (not actually on board) piece used for Queen movement (queen = bishop + rook) */
	private ChessPiece tempBishop;
	/** List of squares that can be reached by this piece, used for check/check-mate functionality */
	private List<Square> moveList = new ArrayList<Square>();
	
	/**
	 * constructor for Queen chess piece
	 * @param xStart x-component of piece's square
	 * @param yStart y-component of piece's square
	 * @param xC x-coordinate of piece
	 * @param yC y-coordinate of piece
	 * @param pc piece color
	 */
	public Queen(int xStart, int yStart, int xC, int yC, Color pc) {
		xSquare = xStart;
		ySquare = yStart;
		xCoord = xC;
		yCoord = yC;
		pieceColor = pc;
		value = 9; 
		populateMoves();
	}
	
	@Override
	public boolean canMove(int x2, int y2) {
		boolean divZero = false;
		if (y2 - ySquare == 0) divZero = true;
		int slope = 0;
		if (!divZero)
			slope = (x2 - xSquare) / (y2 - ySquare);
		boolean validBishopMove = slope == 1 || slope == -1;
		boolean validRookMove = x2 == xSquare || y2 == ySquare;
		boolean validSquare = validRookMove || validBishopMove;
		return validSquare && !Chess.isOccupied(x2, y2) && hasNoTravelIntersections(x2, y2);
	}

	@Override
	public boolean canCapture(int x2, int y2) {
				boolean divZero = false;
		if (y2 - ySquare == 0) divZero = true;
		int slope = 0;
		if (!divZero)
			slope = (x2 - xSquare) / (y2 - ySquare);
		boolean validBishopMove = slope == 1 || slope == -1;
		boolean validRookMove = x2 == xSquare || y2 == ySquare;
		boolean validSquare = validRookMove || validBishopMove;
		return validSquare && Chess.isOccupied(x2, y2) && !Chess.getPiece(x2, y2).getColor().equals(pieceColor)
				&& hasNoTravelIntersections(x2, y2);
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
	
	public boolean hasNoTravelIntersections(int x2, int y2) {
		if (x2 == xSquare || y2 == ySquare) {
			tempRook = new Rook(xSquare, ySquare, 0, 0, pieceColor);
			return tempRook.canMove(x2, y2) || tempRook.canCapture(x2, y2);
		} else {
			tempBishop = new Bishop(xSquare, ySquare, 0, 0, pieceColor);
			return tempBishop.canMove(x2, y2) || tempBishop.canCapture(x2, y2);
		}
	}

	@Override
	public void populateMoves() {
		tempRook = new Rook(xSquare, ySquare, 0, 0, pieceColor);
		moveList = tempRook.getMoveList();
		tempBishop = new Bishop(xSquare, ySquare, 0, 0, pieceColor);
		for (int i = 0; i < tempBishop.getMoveList().size(); i++) {
			moveList.add(tempBishop.getMoveList().get(i));
		}
	}
	
	@Override
	public void updateMoves() {
		List<Square> updatedMoveList = new ArrayList<Square>();
		tempRook = new Rook(xSquare, ySquare, 0, 0, pieceColor);
		updatedMoveList = tempRook.getMoveList();
		tempBishop = new Bishop(xSquare, ySquare, 0, 0, pieceColor);
		for (int i = 0; i < tempBishop.getMoveList().size(); i++) {
			updatedMoveList.add(tempBishop.getMoveList().get(i));
		}
		moveList = updatedMoveList;
	}
	
	@Override
	public List<Square> getMoveList() {
		return moveList;
	}
	
	@Override
	public boolean canSee(int x2, int y2) {
		if (x2 == xSquare || y2 == ySquare) {
			tempRook = new Rook(xSquare, ySquare, 0, 0, pieceColor);
			return tempRook.canSee(x2, y2);
		} else {
			tempBishop = new Bishop(xSquare, ySquare, 0, 0, pieceColor);
			return tempBishop.canSee(x2, y2);
		}
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
