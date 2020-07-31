package chess;

/**
 * Class representing one move in Chess. Used for check functionality and in tracking game moves
 * @author Lorenzo Battigelli
 *
 */
public class Move {

	/** Square that piece has moved from */
	private Square from;
	/** Square that piece has moved to */
	private Square to;
	/** Piece that is moving */
	private ChessPiece piece;
	/** Optional piece that was captured */
	private ChessPiece captured;
	
	/**
	 * Constructor with coordinates and all fields
	 * @param piece the chess piece being moved
	 * @param x1 x coordinate of square piece moved from
	 * @param y1 y coordinate of square piece moved from
	 * @param x2 x coordinate of square piece moved to
	 * @param y2 x coordinate of square piece moved to
	 * @param (optional) captured piece if square (x2, y2) had a piece before this move
	 */
	public Move(ChessPiece piece, int x1, int y1, int x2, int y2, ChessPiece captured) {
		this.piece = piece;
		from = new Square(x1, y1, Chess.isOccupied(x1, y1));
		to = new Square(x2, y2, Chess.isOccupied(x2, y2));
		this.captured = captured;
	}
	
	/**
	 * Constructor with squares instead of coordinates
	 */
	public Move(ChessPiece piece, Square from, Square to, ChessPiece captured) {
		this(piece, from.getXSquare(), from.getYSquare(), to.getXSquare(), to.getYSquare(), captured);
	}
	
	/**
	 * Constructor with coordinates and no captured piece
	 */
	public Move(ChessPiece piece, int x1, int y1, int x2, int y2) {
		this(piece, x1, y1, x2, y2, null);
	}
	
	/**
	 * Constructor with squares and no captured piece
	 */
	public Move(ChessPiece piece, Square from, Square to) {
		this(piece, from.getXSquare(), from.getYSquare(), to.getXSquare(), to.getYSquare(), null);
	}
	
	public void undo() {
		int xDisplacement = (to.getXSquare() - from.getXSquare()) * Chess.getSquareDimension();
		int yDisplacement = (to.getYSquare() - from.getYSquare()) * Chess.getSquareDimension();
		piece.move(-xDisplacement, -yDisplacement);
		ChessPieces.switchOccupation(to.getXSquare(), to.getYSquare(), from.getXSquare(), from.getYSquare());
		if (captured != null) {
			captured.move(-600, 0);
			Chess.setOccupation(captured.getXSquare(), captured.getYSquare(), true);
		}
	}
	
	public void redo() {
		int xDisplacement = (to.getXSquare() - from.getXSquare()) * Chess.getSquareDimension();
		int yDisplacement = (to.getYSquare() - from.getYSquare()) * Chess.getSquareDimension();
		piece.move(xDisplacement, yDisplacement);
		//TODO: incomplete
	}
	
	public ChessPiece getPiece() {
		return piece;
	}
	
	public Square getFrom() {
		return from;
	}
	
	public Square getTo() {
		return to;
	}
	
}
