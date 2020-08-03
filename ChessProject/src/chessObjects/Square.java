package chessObjects;

/**
 * Object representing a single chess board square by means of x and y coordinates
 * @author Lorenzo Battigelli
 *
 */
public class Square {

	/** x component of square array (0-7) */
	private int xSquare;
	/** y component of square array (0-7) */
	private int ySquare;
	/** true if square has chess piece on it, else false */
	private boolean occupied;
	
	/**
	 * Constructor, sets fields
	 * @param x component of square array
	 * @param y component of square array
	 * @param occ whether or not piece is on square
	 */
	public Square(int x, int y, boolean occ) {
		setXSquare(x);
		setYSquare(y);
		setOccupied(occ);
	}
	
	public int getXSquare() {
		return xSquare;
	}

	public void setXSquare(int xSquare) {
		this.xSquare = xSquare;
	}

	public int getYSquare() {
		return ySquare;
	}

	public void setYSquare(int ySquare) {
		this.ySquare = ySquare;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	/**
	 * Comparison method, compares all square fields
	 */
	public boolean equals(Object o) {
		if (!o.getClass().toString().equals("class chess.Square"))
			return false;
		Square other = (Square) o;
		return other.getXSquare() == xSquare && other.getYSquare() == ySquare && other.isOccupied() == occupied;
	}
	
	/**
	 * Comparison method, only comparing x and y fields
	 */
	public boolean equalsXY(Object o) {
		if (!o.getClass().toString().equals("class chess.Square"))
			return false;
		Square other = (Square) o;
		return other.getXSquare() == xSquare && other.getYSquare() == ySquare && other.isOccupied() == occupied;
	}
	
	/**
	 * Custom toString used for testing/debugging
	 */
	public String toString() {
		String not = "";
		if (!isOccupied()) not = "not ";
		return "Square (" + getXSquare() + ", " + getYSquare() + ") " + not + "occupied";
	}
	
}
