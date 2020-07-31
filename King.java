package chess;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class King implements ChessPiece {

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
	/** boolean representing whether or not the king has moved in the current game, used for castling */
	private boolean hasMoved;
	/** List of squares that can be reached by this piece, used for check/check-mate functionality */
	private List<Square> moveList = new ArrayList<Square>();
	
	/**
	 * Constructor for King chess piece
	 * @param xStart x-component of piece's square
	 * @param yStart y-component of piece's square
	 * @param xC x-coordinate of piece
	 * @param yC y-coordinate of piece
	 * @param pc piece color
	 */
	public King(int xStart, int yStart, int xC, int yC, Color pc) {
		xSquare = xStart;
		ySquare = yStart;
		xCoord = xC;
		yCoord = yC;
		pieceColor = pc;
		hasMoved = false;
		populateMoves();
	}
	
	@Override
	public boolean canMove(int x2, int y2) {
		boolean movable = !Chess.isOccupied(x2, y2) && squareInMoves(x2, y2) && ChessPieces.kingCanMove(x2, y2, pieceColor);
		boolean castleable = canCastle(x2, y2) && ChessPieces.kingCanMove(x2, y2, pieceColor) && !Chess.kingInCheck(pieceColor);
		return movable || castleable;
	}

	@Override
	public boolean canCapture(int x2, int y2) {
		return Chess.isOccupied(x2, y2) && squareInMoves(x2, y2) && ChessPieces.kingCanMove(x2, y2, pieceColor) 
				&& !Chess.getPiece(x2, y2).getColor().equals(pieceColor);
	}

	@Override
	public void move(int xDisplacement, int yDisplacement) {
		int xDispSquares = (xDisplacement / Chess.getSquareDimension());
		int xDisplacementInSquares = (xDisplacement / Chess.getSquareDimension());
		int yDisplacementInSquares = (yDisplacement / Chess.getSquareDimension());
		if (xDisplacement == 120 || xDisplacement == -120) { // 2 square move = castle attempt (60 pixel per square)
			// move rook over 2
			if (pieceColor.equals(Color.WHITE)) {
				if (xDispSquares > 0) { // king side castle
					Chess.getPiece(xSquare + 3, ySquare).move(-xDisplacement, yDisplacement);
					ChessPieces.switchOccupation(xSquare + 3, ySquare, xSquare + 3 - xDisplacementInSquares, 
							ySquare + yDisplacementInSquares);
				} else if (xDispSquares < 0) { // queen side castle
					xDisplacement *= 1.5;
					xDisplacementInSquares = (xDisplacement / Chess.getSquareDimension());
					Chess.getPiece(xSquare - 4, ySquare).move(-xDisplacement, yDisplacement);
					ChessPieces.switchOccupation(xSquare - 4, ySquare, xSquare - 4 - xDisplacementInSquares, 
							ySquare + yDisplacementInSquares);
					xDisplacement /= 1.5;
				}
			} else {
				if (xDispSquares > 0) { // king side castle
					Chess.getPiece(xSquare + 3, ySquare).move(-xDisplacement, yDisplacement);
					ChessPieces.switchOccupation(xSquare + 3, ySquare, xSquare + 3 - xDisplacementInSquares, 
							ySquare + yDisplacementInSquares);
				} else if (xDispSquares < 0) { // queen side castle
					xDisplacement *= 1.5;
					xDisplacementInSquares = (xDisplacement / Chess.getSquareDimension());
					Chess.getPiece(xSquare - 4, ySquare).move(-xDisplacement, yDisplacement);
					ChessPieces.switchOccupation(xSquare - 4, ySquare, xSquare - 4 - xDisplacementInSquares, 
							ySquare + yDisplacementInSquares);
					xDisplacement /= 1.5;
				}
			}
		}
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

	@Override
	public void populateMoves() {
		if (xSquare - 1 >= 0 && ySquare - 1 >= 0)
			moveList.add(new Square(xSquare - 1, ySquare - 1, false));
		if (ySquare - 1 >= 0)
			moveList.add(new Square(xSquare, ySquare - 1, false));
		if (xSquare - 1 >= 0)
			moveList.add(new Square(xSquare - 1, ySquare, false));
		if (xSquare - 1 >= 0 && ySquare + 1 <= 7)
			moveList.add(new Square(xSquare - 1, ySquare + 1, false));
		if (xSquare + 1 <= 7 && ySquare - 1 >= 0)
			moveList.add(new Square(xSquare + 1, ySquare - 1, false));
		if (xSquare + 1 <= 7)
			moveList.add(new Square(xSquare + 1, ySquare, false));
		if (ySquare + 1 <= 7)
			moveList.add(new Square(xSquare, ySquare + 1, false));
		if (xSquare + 1 <= 7 && ySquare + 1 <= 7)
			moveList.add(new Square(xSquare + 1, ySquare + 1, false));
	}
	
	@Override
	public void updateMoves() {
		List<Square> updatedMoveList = new ArrayList<Square>();
		if (xSquare - 1 >= 0 && ySquare - 1 >= 0)
			updatedMoveList.add(new Square(xSquare - 1, ySquare - 1, false));
		if (ySquare - 1 >= 0)
			updatedMoveList.add(new Square(xSquare, ySquare - 1, false));
		if (xSquare - 1 >= 0)
			updatedMoveList.add(new Square(xSquare - 1, ySquare, false));
		if (xSquare - 1 >= 0 && ySquare + 1 <= 7)
			updatedMoveList.add(new Square(xSquare - 1, ySquare + 1, false));
		if (xSquare + 1 <= 7 && ySquare - 1 >= 0)
			updatedMoveList.add(new Square(xSquare + 1, ySquare - 1, false));
		if (xSquare + 1 <= 7)
			updatedMoveList.add(new Square(xSquare + 1, ySquare, false));
		if (ySquare + 1 <= 7)
			updatedMoveList.add(new Square(xSquare, ySquare + 1, false));
		if (xSquare + 1 <= 7 && ySquare + 1 <= 7)
			updatedMoveList.add(new Square(xSquare + 1, ySquare + 1, false));
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
	
	private boolean canCastle(int x2, int y2) {
		boolean squaresNotInCheck = false;
		boolean notOccupied = false;
		boolean rookNotMoved = false;
		Rook tempRook;
		if (pieceColor.equals(Color.WHITE)) {
			if (x2 > xSquare) { // king side castle
				squaresNotInCheck = Chess.notAttacked(x2, y2, pieceColor) && Chess.notAttacked(x2 - 1, y2, pieceColor);
				notOccupied = !Chess.isOccupied(x2, y2) && !Chess.isOccupied(x2 - 1, y2);
				if (Chess.getPiece(x2 + 1, y2).getClass().toString().equals("class chess.Rook")) {
					tempRook = (Rook) Chess.getPiece(x2 + 1, y2);
					rookNotMoved = tempRook.getHasMoved();
				}
			} else if (x2 < xSquare) { // queen side castle
				squaresNotInCheck = Chess.notAttacked(x2, y2, pieceColor) && Chess.notAttacked(x2 + 1, y2, pieceColor);
				notOccupied = !Chess.isOccupied(x2, y2) && !Chess.isOccupied(x2 + 1, y2) && 
						!Chess.isOccupied(x2 - 1, y2);
				if (Chess.getPiece(x2 - 2, y2).getClass().toString().equals("class chess.Rook")) {
					tempRook = (Rook) Chess.getPiece(x2 - 2, y2);
					rookNotMoved = tempRook.getHasMoved();
				}
			}
		} else {
			if (x2 > xSquare) { // king side castle
				squaresNotInCheck = Chess.notAttacked(x2, y2, pieceColor) && Chess.notAttacked(x2 - 1, y2, pieceColor);
				notOccupied = !Chess.isOccupied(x2, y2) && !Chess.isOccupied(x2 - 1, y2);
				if (Chess.getPiece(x2 + 1, y2).getClass().toString().equals("class chess.Rook")) {
					tempRook = (Rook) Chess.getPiece(x2 + 1, y2);
					rookNotMoved = tempRook.getHasMoved();
				}
			} else if (x2 < xSquare) { // queen side castle
				squaresNotInCheck = Chess.notAttacked(x2, y2, pieceColor) && Chess.notAttacked(x2 + 1, y2, pieceColor);
				notOccupied = !Chess.isOccupied(x2, y2) && !Chess.isOccupied(x2 + 1, y2) && 
						!Chess.isOccupied(x2 - 1, y2);
				if (Chess.getPiece(x2 - 2, y2).getClass().toString().equals("class chess.Rook")) {
					tempRook = (Rook) Chess.getPiece(x2 - 2, y2);
					rookNotMoved = tempRook.getHasMoved();
				}

			}
		}
		return squaresNotInCheck && notOccupied && !hasMoved && !rookNotMoved && Chess.notAttacked(xSquare, ySquare, pieceColor);
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
		return 0;
	}

}
