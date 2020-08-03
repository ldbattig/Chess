package chessPieces;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import chessControllers.Chess;
import chessObjects.Square;

/**
 * Knight object class. Includes universal behaviors from ChessPiece interface and knight-exclusive 
 * movement/capturing
 * @author Lorenzo Battigelli
 *
 */
public class Knight implements ChessPiece {

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
	/** List of squares that can be reached by this piece, used for check/checkmate functionality */
	private List<Square> moveList = new ArrayList<Square>();
	
	/**
	 * constructor for Knight chess piece
	 * @param xStart x-component of piece's square
	 * @param yStart y-component of piece's square
	 * @param xC x-coordinate of piece
	 * @param yC y-coordinate of piece
	 * @param pc piece color
	 */
	public Knight(int xStart, int yStart, int xC, int yC, Color pc) {
		xSquare = xStart;
		ySquare = yStart;
		xCoord = xC;
		yCoord = yC;
		pieceColor = pc;
		value = 3;
		populateMoves();
	}
	
	@Override
	public boolean canMove(int x2, int y2) {
		return !Chess.isOccupied(x2, y2) && squareInMoves(x2, y2);
	}

	@Override
	public boolean canCapture(int x2, int y2) {
		return Chess.isOccupied(x2, y2) && squareInMoves(x2, y2) && !Chess.getPiece(x2, y2).getColor().equals(pieceColor);
	}

	@Override
	public void move(int xDisplacement, int yDisplacement) {
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
	
	@Override
	public void populateMoves() {
		if (xSquare - 1 >= 0 && ySquare - 2 >= 0)
			moveList.add(new Square(xSquare - 1, ySquare - 2, false));
		if (xSquare - 2 >= 0 && ySquare - 1 >= 0)
			moveList.add(new Square(xSquare - 2, ySquare - 1, false));
		if (xSquare + 1 <= 7 && ySquare - 2 >= 0)
			moveList.add(new Square(xSquare + 1, ySquare - 2, false));
		if (xSquare - 2 >= 0 && ySquare + 1 <= 7)
			moveList.add(new Square(xSquare - 2, ySquare + 1, false));
		if (xSquare - 1 >= 0 && ySquare + 1 <= 7)
			moveList.add(new Square(xSquare - 1, ySquare + 2, false));
		if (xSquare + 2 <= 7 && ySquare - 1 >= 0)
			moveList.add(new Square(xSquare + 2, ySquare - 1, false));
		if (xSquare + 1 <= 7 && ySquare + 2 <= 7)
			moveList.add(new Square(xSquare + 1, ySquare + 2, false));
		if (xSquare + 2 <= 7 && ySquare + 1 <= 7)
			moveList.add(new Square(xSquare + 2, ySquare + 1, false));
	}

	@Override
	public void updateMoves() {
		List<Square> updatedMoveList = new ArrayList<Square>();
		if (xSquare - 1 >= 0 && ySquare - 2 >= 0)
			updatedMoveList.add(new Square(xSquare - 1, ySquare - 2, false));
		if (xSquare - 2 >= 0 && ySquare - 1 >= 0)
			updatedMoveList.add(new Square(xSquare - 2, ySquare - 1, false));
		if (xSquare + 1 <= 7 && ySquare - 2 >= 0)
			updatedMoveList.add(new Square(xSquare + 1, ySquare - 2, false));
		if (xSquare - 2 >= 0 && ySquare + 1 <= 7)
			updatedMoveList.add(new Square(xSquare - 2, ySquare + 1, false));
		if (xSquare - 1 >= 0 && ySquare + 1 <= 7)
			updatedMoveList.add(new Square(xSquare - 1, ySquare + 2, false));
		if (xSquare + 2 <= 7 && ySquare - 1 >= 0)
			updatedMoveList.add(new Square(xSquare + 2, ySquare - 1, false));
		if (xSquare + 1 <= 7 && ySquare + 2 <= 7)
			updatedMoveList.add(new Square(xSquare + 1, ySquare + 2, false));
		if (xSquare + 2 <= 7 && ySquare + 1 <= 7)
			updatedMoveList.add(new Square(xSquare + 2, ySquare + 1, false));
		moveList = updatedMoveList;
	}
	
	private boolean squareInMoves(int x2, int y2) {
		for (int i = 0; i < moveList.size(); i++) {
			if (moveList.get(i).getXSquare() == x2 && moveList.get(i).getYSquare() == y2)
				return true;
		}
		return false;
	}
	
	@Override
	public List<Square> getMoveList() {
		return moveList;
	}
	
	@Override
	public boolean canSee(int x2, int y2) {
		Square temp = new Square(x2, y2, false);
		return moveList.contains(temp);
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
