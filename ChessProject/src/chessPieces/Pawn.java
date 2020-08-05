package chessPieces;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import chessControllers.Chess;
import chessObjects.Square;

/**
 * Pawn object class. Includes universal behaviors from ChessPiece interface and pawn-exclusive 
 * movement/capturing
 * @author Lorenzo Battigelli
 *
 */
public class Pawn implements ChessPiece {

	/** x component of square array (0-7) */ 
	private int xSquare;
	/** y component of square array (0-7) */
	private int ySquare;
	/** x component of pixel array (0-479) */
	private int xCoord;
	/** y component of pixel array (0-479) */
	private int yCoord;
	/** boolean for two square move ability of pawn's first move */
	private boolean hasMoved;
	/** boolean for canMove() and canCapture() methods */
	private Color pieceColor;
	/** integer used for move direction based on piece color */
	private int movementDirection;
	/** Piece value for tracking instantaneous advantage and piece tracking - Pawn = 1, Bishop/Knight = 3, Rook = 5, Queen = 9 */
	private int value;
	/** Boolean used for en-passant */
	private boolean justMovedTwo;
	/** List of squares that can be reached by this piece, used for check/check-mate functionality */
	private List<Square> moveList = new ArrayList<Square>();

	/**
	 * constructor for Pawn chess piece
	 * @param xStart x-component of piece's square
	 * @param yStart y-component of piece's square
	 * @param xC x-coordinate of piece
	 * @param yC y-coordinate of piece
	 * @param pc piece color
	 */
	public Pawn(int xStart, int yStart, int xC, int yC, Color pc) {
		xSquare = xStart;
		ySquare = yStart;
		xCoord = xC;
		yCoord = yC;
		hasMoved = false;
		pieceColor = pc;
		movementDirection = pieceColor.equals(Color.WHITE) ? 1: -1;
		value = 1;
		justMovedTwo = false;
		populateMoves();
	}

	// TODO does not take into account en-passant
	@Override
	public boolean canMove(int x2, int y2) {
		return ((x2 == xSquare) && ((y2 == ySquare - 1 * movementDirection) || (y2 == ySquare - 2 * movementDirection && 
				!hasMoved && !Chess.isOccupied(x2, y2 + movementDirection)))) && !Chess.isOccupied(x2, y2);
	}

	/**
	 * Logic explained from perspective of white pieces for reference:
	 * return (square is adjacent left OR square is adjacent right) AND (square is one above) AND square is occupied AND square's piece is 
	 * opposite color
	 */
	@Override
	public boolean canCapture(int x2, int y2) {
		return (((x2 == xSquare - 1 * movementDirection || x2 == xSquare + 1 * movementDirection) && (y2 == ySquare - 1 * 
				movementDirection)) && Chess.isOccupied(x2, y2) && !Chess.getPiece(x2, y2).getColor().equals(getColor())) || canEnPassant(x2, y2);
	}


	/**
	 * Logic explained from perspective of white pieces for reference:
	 * return (below square has pawn AND below square's pawn moved last move AND such pawn is opposite color
	 * @param x2
	 * @param y2
	 * @return
	 */
	public boolean canEnPassant(int x2, int y2) {
		//TODO: check whether adjacent pieces exist first to avoid NPE, NPE still caused if pawn is a or h
		//TODO: needs re-factoring/simplification
		int ySquareForEnPassant = (pieceColor.equals(Color.WHITE)) ? 3 : 4; 
		if (!Chess.isOccupied(x2, y2 + movementDirection) || ySquare != ySquareForEnPassant || !(xSquare <= x2 + 1 && xSquare >= x2 - 1))
			return false;
		System.out.println("Last x: " + Chess.getLastClick().getXSquare() + ", last y: " + Chess.getLastClick().getYSquare());
		if (!Chess.getPiece(x2, y2).getClass().toString().equals("class chessPieces.Pawn")) return false;
		Pawn otherPiece = (Pawn) Chess.getPiece(x2, y2 + movementDirection);
		boolean thatPawnJustMovedTwo = otherPiece.getJustMovedTwo();
		boolean belowSquareIsPawn = Chess.getPiece(x2, y2 + movementDirection).getClass().toString().equals("class chessPieces.Pawn");
		boolean lastMoveSquareX = Chess.getLastClick().getXSquare() == x2;
		boolean lastMoveSquareY = Chess.getLastClick().getYSquare() == y2 + movementDirection;
		System.out.println(thatPawnJustMovedTwo + " " + belowSquareIsPawn + " " + lastMoveSquareX + " " + lastMoveSquareY + 
				" " + !Chess.getPiece(x2, y2 + movementDirection).getColor().equals(pieceColor));
		return thatPawnJustMovedTwo && belowSquareIsPawn && lastMoveSquareX && lastMoveSquareY && !Chess.getPiece(x2, y2 + movementDirection).getColor().equals(getColor());
	}

	@Override
	public void move(int xDisplacement, int yDisplacement) {
		justMovedTwo = (yDisplacement == 120 || yDisplacement == -120) ? true: false;
		xCoord += xDisplacement;
		yCoord += yDisplacement;
		xSquare += (xDisplacement / Chess.getSquareDimension());
		ySquare += (yDisplacement / Chess.getSquareDimension());
		hasMoved = true;
	}

	@Override
	public ChessPiece capture(int x2, int y2) {
		if (canEnPassant(x2, y2))
			return Chess.getPiece(x2, y2 + movementDirection).removePiece();
		return Chess.getPiece(x2, y2).removePiece();
	}

	public void promote() {
		if (Chess.getOrientation().equals(Color.WHITE)) {
			if (pieceColor.equals(Color.WHITE)) {
				if (ySquare == 0) {
					//promote
					Chess.promotePawn(this);
				}
			} else {
				if (ySquare == 7) {
					//promote
					Chess.promotePawn(this);
				}
			}
		} else {
			if (pieceColor.equals(Color.WHITE)) {
				if (ySquare == 7) {
					//promote
					Chess.promotePawn(this);
				}
			} else {
				if (ySquare == 0) {
					//promote
					Chess.promotePawn(this);
				}
			}
		}
	}

	@Override
	public ChessPiece removePiece() {
		//TODO: this is bad etiquette
		xSquare += 10;
		xCoord += 600;
		return this;
	}

	@Override
	public List<Square> getMoveList() {
		return moveList;
	}

	@Override
	public void populateMoves() {
		int yBound = pieceColor.equals(Color.WHITE) ? 0: 7;
		if (xSquare + 1 <= 7) {
			if (pieceColor.equals(Color.WHITE)) {
				if (ySquare - movementDirection >= yBound)
					moveList.add(new Square(xSquare + 1, ySquare - movementDirection, false));
			} else if (ySquare - movementDirection <= yBound)
				moveList.add(new Square(xSquare + 1, ySquare - movementDirection, false));
		}	
		if (xSquare - 1 >= 0) {
			if (pieceColor.equals(Color.WHITE)) {
				if (ySquare - movementDirection >= yBound)
					moveList.add(new Square(xSquare - 1, ySquare - movementDirection, false));
			} else if (ySquare - movementDirection <= yBound)
				moveList.add(new Square(xSquare - 1, ySquare - movementDirection, false));
		}
	}

	@Override
	public void updateMoves() {
		List<Square> updatedMoveList = new ArrayList<Square>();
		int yBound = pieceColor.equals(Color.WHITE) ? 0: 7;
		if (xSquare + 1 <= 7) {
			if (pieceColor.equals(Color.WHITE)) {
				if (ySquare - movementDirection >= yBound)
					updatedMoveList.add(new Square(xSquare + 1, ySquare - movementDirection, false));
			} else if (ySquare - movementDirection <= yBound)
				updatedMoveList.add(new Square(xSquare + 1, ySquare - movementDirection, false));
		}	
		if (xSquare - 1 >= 0) {
			if (pieceColor.equals(Color.WHITE)) {
				if (ySquare - movementDirection >= yBound)
					updatedMoveList.add(new Square(xSquare - 1, ySquare - movementDirection, false));
			} else if (ySquare - movementDirection <= yBound)
				updatedMoveList.add(new Square(xSquare - 1, ySquare - movementDirection, false));
		}
		moveList = updatedMoveList;
	}

	@Override
	public boolean canSee(int x2, int y2) {
		Square temp = new Square(x2, y2, false);
		return moveList.contains(temp);
	}
	
	public void resetHasMoved() {
		hasMoved = false;
	}
	
	public void flipMovementDirection() {
		movementDirection *= -1;
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

	public boolean getJustMovedTwo() {
		return justMovedTwo;
	}
	
	public boolean getHasMoved() {
		return hasMoved;
	}
}
