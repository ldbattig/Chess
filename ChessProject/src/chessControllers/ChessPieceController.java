package chessControllers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import chessObjects.Move;
import chessObjects.Square;
import chessPieces.Bishop;
import chessPieces.ChessPiece;
import chessPieces.King;
import chessPieces.Knight;
import chessPieces.Pawn;
import chessPieces.Queen;
import chessPieces.Rook;

/**
 * Controller for ChessPiece graphics and high-level movement functionality
 * Maintains lists of ChessPieces on and off board for gameplay
 * @author Lorenzo Battigelli
 *
 */
public class ChessPieceController {

	// All initial x and y coordinates (in pixels) for chess pieces
	private int whiteKingX = 260;
	private int whiteQueenX = 200;
	private int whiteKnightAX = 80;
	private int whiteKnightBX = 380;
	private int whiteBishopAX = 140;
	private int whiteBishopBX = 320;
	private int whiteRookAX = 20;
	private int whiteRookBX = 440;
	private int whiteKingY = 438;
	private int whiteQueenY = 438;
	private int whiteKnightAY = 438;
	private int whiteKnightBY = 438;
	private int whiteBishopAY = 438;
	private int whiteBishopBY = 438;
	private int whiteRookAY = 438;
	private int whiteRookBY = 438;

	private int whitePawnAX = 20;
	private int whitePawnBX = 80;
	private int whitePawnCX = 140;
	private int whitePawnDX = 200;
	private int whitePawnEX = 260;
	private int whitePawnFX = 320;
	private int whitePawnGX = 380;
	private int whitePawnHX = 440;
	private int whitePawnAY = 378;
	private int whitePawnBY = 378;
	private int whitePawnCY = 378;
	private int whitePawnDY = 378;
	private int whitePawnEY = 378;
	private int whitePawnFY = 378;
	private int whitePawnGY = 378;
	private int whitePawnHY = 378;

	private int blackKingX = 260;
	private int blackQueenX = 200;
	private int blackKnightAX = 80;
	private int blackKnightBX = 380;
	private int blackBishopAX = 140;
	private int blackBishopBX = 320;
	private int blackRookAX = 20;
	private int blackRookBX = 440;
	private int blackKingY = 18;
	private int blackQueenY = 18;
	private int blackKnightAY = 18;
	private int blackKnightBY = 18;
	private int blackBishopAY = 18;
	private int blackBishopBY = 18;
	private int blackRookAY = 18;
	private int blackRookBY = 18;

	private int blackPawnAX = 20;
	private int blackPawnBX = 80;
	private int blackPawnCX = 140;
	private int blackPawnDX = 200;
	private int blackPawnEX = 260;
	private int blackPawnFX = 320;
	private int blackPawnGX = 380;
	private int blackPawnHX = 440;
	private int blackPawnAY = 78;
	private int blackPawnBY = 78;
	private int blackPawnCY = 78;
	private int blackPawnDY = 78;
	private int blackPawnEY = 78;
	private int blackPawnFY = 78;
	private int blackPawnGY = 78;
	private int blackPawnHY = 78;

	// Chess piece fields for graphics
	private BufferedImage whiteKing = null;
	private BufferedImage whiteQueen = null;
	private BufferedImage whiteKnightA = null;
	private BufferedImage whiteKnightB = null;
	private BufferedImage whiteBishopA = null;
	private BufferedImage whiteBishopB = null;
	private BufferedImage whiteRookA = null;
	private BufferedImage whiteRookB = null;
	private BufferedImage whitePawnA = null;
	private BufferedImage whitePawnB = null;
	private BufferedImage whitePawnC = null;
	private BufferedImage whitePawnD = null;
	private BufferedImage whitePawnE = null;
	private BufferedImage whitePawnF = null;
	private BufferedImage whitePawnG = null;
	private BufferedImage whitePawnH = null;

	private BufferedImage blackKing = null;
	private BufferedImage blackQueen = null;
	private BufferedImage blackKnightA = null;
	private BufferedImage blackKnightB = null;
	private BufferedImage blackBishopA = null;
	private BufferedImage blackBishopB = null;
	private BufferedImage blackRookA = null;
	private BufferedImage blackRookB = null;
	private BufferedImage blackPawnA = null;
	private BufferedImage blackPawnB = null;
	private BufferedImage blackPawnC = null;
	private BufferedImage blackPawnD = null;
	private BufferedImage blackPawnE = null;
	private BufferedImage blackPawnF = null;
	private BufferedImage blackPawnG = null;
	private BufferedImage blackPawnH = null;

	/** List of pieces on board (in play) */
	private static List<ChessPiece> piecesOn = new ArrayList<ChessPiece>();
	/** List of pieces off board (not in play) */
	private static List<ChessPiece> piecesOff = new ArrayList<ChessPiece>();

	/** Chess piece declaration (char 1 = piece type, char 2 = piece color, char 3 = letter used to differentiate 
	* pieces of the same type) */
	// White pieces
	private ChessPiece pWA, pWB, pWC, pWD, pWE, pWF, pWG, pWH, kWA, kWB, bWA, bWB, rWA, rWB, qW, kW;
	// Black pieces
	private ChessPiece pBA, pBB, pBC, pBD, pBE, pBF, pBG, pBH, kBA, kBB, bBA, bBB, rBA, rBB, qB, kB;

	/**
	 * Loads chess piece image files and paints them at corresponding coordinates
	 * @param g Graphics object for painting
	 */
	public void paintPieces(Graphics g) {
		loadPieces();
		g.drawImage(whiteKing, kW.getXCoord(), kW.getYCoord(), null);
		g.drawImage(whiteQueen, qW.getXCoord(), qW.getYCoord(), null);
		g.drawImage(whiteKnightA, kWA.getXCoord(), kWA.getYCoord(), null);
		g.drawImage(whiteKnightB, kWB.getXCoord(), kWB.getYCoord(), null);
		g.drawImage(whiteBishopA, bWA.getXCoord(), bWA.getYCoord(), null);
		g.drawImage(whiteBishopB, bWB.getXCoord(), bWB.getYCoord(), null);
		g.drawImage(whiteRookA, rWA.getXCoord(), rWA.getYCoord(), null);
		g.drawImage(whiteRookB, rWB.getXCoord(), rWB.getYCoord(), null);

		g.drawImage(whitePawnA, pWA.getXCoord(), pWA.getYCoord(), null);
		g.drawImage(whitePawnB, pWB.getXCoord(), pWB.getYCoord(), null);
		g.drawImage(whitePawnC, pWC.getXCoord(), pWC.getYCoord(), null);
		g.drawImage(whitePawnD, pWD.getXCoord(), pWD.getYCoord(), null);
		g.drawImage(whitePawnE, pWE.getXCoord(), pWE.getYCoord(), null);
		g.drawImage(whitePawnF, pWF.getXCoord(), pWF.getYCoord(), null);
		g.drawImage(whitePawnG, pWG.getXCoord(), pWG.getYCoord(), null);
		g.drawImage(whitePawnH, pWH.getXCoord(), pWH.getYCoord(), null);

		g.drawImage(blackKing, kB.getXCoord(), kB.getYCoord(), null);
		g.drawImage(blackQueen, qB.getXCoord(), qB.getYCoord(), null);
		g.drawImage(blackKnightA, kBA.getXCoord(), kBA.getYCoord(), null);
		g.drawImage(blackKnightB, kBB.getXCoord(), kBB.getYCoord(), null);
		g.drawImage(blackBishopA, bBA.getXCoord(), bBA.getYCoord(), null);
		g.drawImage(blackBishopB, bBB.getXCoord(), bBB.getYCoord(), null);
		g.drawImage(blackRookA, rBA.getXCoord(), rBA.getYCoord(), null);
		g.drawImage(blackRookB, rBB.getXCoord(), rBB.getYCoord(), null);

		g.drawImage(blackPawnA, pBA.getXCoord(), pBA.getYCoord(), null);
		g.drawImage(blackPawnB, pBB.getXCoord(), pBB.getYCoord(), null);
		g.drawImage(blackPawnC, pBC.getXCoord(), pBC.getYCoord(), null);
		g.drawImage(blackPawnD, pBD.getXCoord(), pBD.getYCoord(), null);
		g.drawImage(blackPawnE, pBE.getXCoord(), pBE.getYCoord(), null);
		g.drawImage(blackPawnF, pBF.getXCoord(), pBF.getYCoord(), null);
		g.drawImage(blackPawnG, pBG.getXCoord(), pBG.getYCoord(), null);
		g.drawImage(blackPawnH, pBH.getXCoord(), pBH.getYCoord(), null);
	}

	/**
	 * Loads chess piece image files
	 */
	private void loadPieces() {
		try {
			whiteKing = ImageIO.read(new File("ChessPieces/ChessWhiteKingx60.png"));
			whiteQueen = ImageIO.read(new File("ChessPieces/ChessWhiteQueenx60.png"));
			whiteKnightA = ImageIO.read(new File("ChessPieces/ChessWhiteKnightx60.png"));
			whiteKnightB = ImageIO.read(new File("ChessPieces/ChessWhiteKnightx60.png"));
			whiteBishopA = ImageIO.read(new File("ChessPieces/ChessWhiteBishopx60.png"));
			whiteBishopB = ImageIO.read(new File("ChessPieces/ChessWhiteBishopx60.png"));
			whiteRookA = ImageIO.read(new File("ChessPieces/ChessWhiteRookx60.png"));
			whiteRookB = ImageIO.read(new File("ChessPieces/ChessWhiteRookx60.png"));

			whitePawnA = ImageIO.read(new File("ChessPieces/ChessWhitePawnx60.png"));
			whitePawnB = ImageIO.read(new File("ChessPieces/ChessWhitePawnx60.png"));
			whitePawnC = ImageIO.read(new File("ChessPieces/ChessWhitePawnx60.png"));
			whitePawnD = ImageIO.read(new File("ChessPieces/ChessWhitePawnx60.png"));
			whitePawnE = ImageIO.read(new File("ChessPieces/ChessWhitePawnx60.png"));
			whitePawnF = ImageIO.read(new File("ChessPieces/ChessWhitePawnx60.png"));
			whitePawnG = ImageIO.read(new File("ChessPieces/ChessWhitePawnx60.png"));
			whitePawnH = ImageIO.read(new File("ChessPieces/ChessWhitePawnx60.png"));

			blackPawnA = ImageIO.read(new File("ChessPieces/ChessBlackPawnx60.png"));
			blackPawnB = ImageIO.read(new File("ChessPieces/ChessBlackPawnx60.png"));
			blackPawnC = ImageIO.read(new File("ChessPieces/ChessBlackPawnx60.png"));
			blackPawnD = ImageIO.read(new File("ChessPieces/ChessBlackPawnx60.png"));
			blackPawnE = ImageIO.read(new File("ChessPieces/ChessBlackPawnx60.png"));
			blackPawnF = ImageIO.read(new File("ChessPieces/ChessBlackPawnx60.png"));
			blackPawnG = ImageIO.read(new File("ChessPieces/ChessBlackPawnx60.png"));
			blackPawnH = ImageIO.read(new File("ChessPieces/ChessBlackPawnx60.png"));

			blackKing = ImageIO.read(new File("ChessPieces/ChessBlackKingx60.png"));
			blackQueen =  ImageIO.read(new File("ChessPieces/ChessBlackQueenx60.png"));
			blackKnightA = ImageIO.read(new File("ChessPieces/ChessBlackKnightx60.png"));
			blackKnightB = ImageIO.read(new File("ChessPieces/ChessBlackKnightx60.png"));
			blackBishopA = ImageIO.read(new File("ChessPieces/ChessBlackBishopx60.png"));
			blackBishopB = ImageIO.read(new File("ChessPieces/ChessBlackBishopx60.png"));
			blackRookA = ImageIO.read(new File("ChessPieces/ChessBlackRookx60.png"));
			blackRookB = ImageIO.read(new File("ChessPieces/ChessBlackRookx60.png"));
		} catch (IOException e) {
			System.out.println("Error while loading chess piece png files.");
		}
	}

	/**
	 * Initializes all pieces on standard starting squares on chess board
	 * All pieces are added to PiecesOn list at the start of the game
	 */
	public void populatePieces() {
		pWA = new Pawn(0, 6, whitePawnAX, whitePawnAY, Color.WHITE);
		pWB = new Pawn(1, 6, whitePawnBX, whitePawnBY, Color.WHITE);
		pWC = new Pawn(2, 6, whitePawnCX, whitePawnCY, Color.WHITE);
		pWD = new Pawn(3, 6, whitePawnDX, whitePawnDY, Color.WHITE);
		pWE = new Pawn(4, 6, whitePawnEX, whitePawnEY, Color.WHITE);
		pWF = new Pawn(5, 6, whitePawnFX, whitePawnFY, Color.WHITE);
		pWG = new Pawn(6, 6, whitePawnGX, whitePawnGY, Color.WHITE);
		pWH = new Pawn(7, 6, whitePawnHX, whitePawnHY, Color.WHITE);
		kWA = new Knight(1, 7, whiteKnightAX, whiteKnightAY, Color.WHITE);
		kWB = new Knight(6, 7, whiteKnightBX, whiteKnightBY, Color.WHITE);
		bWA = new Bishop(2, 7, whiteBishopAX, whiteBishopAY, Color.WHITE);
		bWB = new Bishop(5, 7, whiteBishopBX, whiteBishopBY, Color.WHITE);
		rWA = new Rook(0, 7, whiteRookAX, whiteRookAY, Color.WHITE);
		rWB = new Rook(7, 7, whiteRookBX, whiteRookBY, Color.WHITE);
		qW = new Queen(3, 7, whiteQueenX, whiteQueenY, Color.WHITE);
		kW = new King(4, 7, whiteKingX, whiteKingY, Color.WHITE);

		pBA = new Pawn(0, 1, blackPawnAX, blackPawnAY, Color.BLACK);
		pBB = new Pawn(1, 1, blackPawnBX, blackPawnBY, Color.BLACK);
		pBC = new Pawn(2, 1, blackPawnCX, blackPawnCY, Color.BLACK);
		pBD = new Pawn(3, 1, blackPawnDX, blackPawnDY, Color.BLACK);
		pBE = new Pawn(4, 1, blackPawnEX, blackPawnEY, Color.BLACK);
		pBF = new Pawn(5, 1, blackPawnFX, blackPawnFY, Color.BLACK);
		pBG = new Pawn(6, 1, blackPawnGX, blackPawnGY, Color.BLACK);
		pBH = new Pawn(7, 1, blackPawnHX, blackPawnHY, Color.BLACK);
		kBA = new Knight(1, 0, blackKnightAX, blackKnightAY, Color.BLACK);
		kBB = new Knight(6, 0, blackKnightBX, blackKnightBY, Color.BLACK);
		bBA = new Bishop(2, 0, blackBishopAX, blackBishopAY, Color.BLACK);
		bBB = new Bishop(5, 0, blackBishopBX, blackBishopBY, Color.BLACK);
		rBA = new Rook(0, 0, blackRookAX, blackRookAY, Color.BLACK);
		rBB = new Rook(7, 0, blackRookBX, blackRookBY, Color.BLACK);
		qB = new Queen(3, 0, blackQueenX, blackQueenY, Color.BLACK);
		kB = new King(4, 0, blackKingX, blackKingY, Color.BLACK);

		piecesOn.add(pWA);
		piecesOn.add(pWB);
		piecesOn.add(pWC);
		piecesOn.add(pWD);
		piecesOn.add(pWE);
		piecesOn.add(pWF);
		piecesOn.add(pWG);
		piecesOn.add(pWH);
		piecesOn.add(kWA);
		piecesOn.add(kWB);
		piecesOn.add(bWA);
		piecesOn.add(bWB);
		piecesOn.add(rWA);
		piecesOn.add(rWB);
		piecesOn.add(qW);
		piecesOn.add(kW);

		piecesOn.add(pBA);
		piecesOn.add(pBB);
		piecesOn.add(pBC);
		piecesOn.add(pBD);
		piecesOn.add(pBE);
		piecesOn.add(pBF);
		piecesOn.add(pBG);
		piecesOn.add(pBH);
		piecesOn.add(kBA);
		piecesOn.add(kBB);
		piecesOn.add(bBA);
		piecesOn.add(bBB);
		piecesOn.add(rBA);
		piecesOn.add(rBB);
		piecesOn.add(qB);
		piecesOn.add(kB);
	}

	/**
	 * Returns the piece of a specified square, or blue pawn if no piece exists
	 * @param x2 x coordinate of square to find piece on
	 * @param y2 y coordinate of square to find piece on
	 * @return chess piece of desired square
	 */
	public ChessPiece getPiece(int x2, int y2) {
		for (int i = 0; i < piecesOn.size(); i++) {
			if (piecesOn.get(i).getXSquare() == x2 && piecesOn.get(i).getYSquare() == y2)
				return piecesOn.get(i);
		}
		return new Pawn(-10, -10, 0, 0, Color.BLUE);
	}

	/**
	 * Attempts to move piece at square (x1, y1) to square (x2, y2). Move includes capture and castle
	 * @param x1 x coordinate of square to move piece from
	 * @param y1 y coordinate of square to move piece from
	 * @param x2 x coordinate of square to move piece to
	 * @param y2 y coordinate of square to move piece to
	 * @return true if piece can move, false otherwise
	 */
	public boolean movePiece(int x1, int y1, int x2, int y2) {
		if (!Chess.isOccupied(x1, y1) || (x1 == x2 && y1 == y2) || !Chess.getPiece(x1, y1).getColor().equals(Chess.getTurn()))
			return false;
		ChessPiece a = getPiece(x1, y1);
		if (a.canMove(x2, y2)) {
			int xDisplacement = (x2 - x1) * Chess.getSquareDimension();
			int yDisplacement = (y2 - y1) * Chess.getSquareDimension();
			a.move(xDisplacement, yDisplacement);
			Chess.addMoveToStack(new Move(a, x1, y1, x2, y2));
			switchOccupation(x1, y1, x2, y2);
			updatePieceLists();

			if (kingInCheck(Chess.getTurn())) {
				Chess.lastMove();
				updatePieceLists();
//				Square temp = new Square(findKing(Chess.getTurn()).getXSquare(), findKing(Chess.getTurn()).getYSquare(), false);
//				Chess.flashRed(temp);
				return false;
			}

			Chess.assignLastClick(new Square(x2, y2, true));
			if (Chess.debug) System.out.println("Successful move.");
			Chess.takeTurn();
			return true;
		} else if (a.canCapture(x2, y2)) {
			ChessPiece b = a.capture(x2, y2);
			int xDisplacement = (x2 - x1) * Chess.getSquareDimension();
			int yDisplacement = (y2 - y1) * Chess.getSquareDimension();
			a.move(xDisplacement, yDisplacement);
			Chess.addMoveToStack(new Move(a, x1, y1, x2, y2, b));
			switchOccupation(x1, y1, x2, y2);
			piecesOn.remove(b);
			piecesOff.add(b);
			updatePieceLists();
//			System.out.println(Chess.getTurn());
//			System.out.println(kingInCheck(Chess.getTurn()));
			if (kingInCheck(Chess.getTurn())) {
				Chess.lastMove();
				updatePieceLists();
//				Square temp = new Square(findKing(Chess.getTurn()).getXSquare(), findKing(Chess.getTurn()).getYSquare(), false);
//				Chess.flashRed(temp);
				return false;
			}
			if (Chess.debug) System.out.println("Successful capture.");
			Chess.takeTurn();
			return true;
		} else if (!Chess.isOccupied(x2, y2))
			return false;
		if (Chess.debug) System.out.println("Unsuccessful move/capture.");
		if (Chess.debug) valueTotals(); //TODO: relocate
		return false;
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
		Chess.switchOccupation(x1, y1, x2, y2);
	}

	/**
	 * [Debugging method] Prints each side's total piece values
	 */
	public void valueTotals() {
		int totalW = 0;
		int totalB = 0;
		for (int i = 0; i < piecesOn.size(); i++) {
			if (piecesOn.get(i).getColor().equals(Color.WHITE))
				totalW += piecesOn.get(i).getValue();
			if (piecesOn.get(i).getColor().equals(Color.BLACK))
				totalB += piecesOn.get(i).getValue();
		}
		System.out.println("White piece total: " + totalW + "." + '\n' + "Black piece total: " + totalB + ".");
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
		Square temp = new Square(x2, y2, false);
		for (int i = 0; i < piecesOn.size(); i++) {
			if (!piecesOn.get(i).getClass().toString().equals("class chessPieces.King") && piecesOn.get(i).canMove(temp.getXSquare(), temp.getYSquare())
					&& !piecesOn.get(i).getColor().equals(kingColor))
				return false;
		}
		return true;
	}
	
	/**
	 * Returns true if king can move or capture on specified square, false otherwise
	 * @param x2 x index of square array
	 * @param y2 y index of square array
	 * @return true or false
	 */
	public static boolean kingCanMove(int x2, int y2, Color kingColor) {
		for (int i = 0; i < piecesOn.size(); i++) {
			if (piecesOn.get(i).getMoveList().contains(new Square(x2, y2, false)) 
					&& !piecesOn.get(i).getColor().equals(kingColor)) {
//				Square temp = new Square(piecesOn.get(i).getXSquare(), piecesOn.get(i).getYSquare(), false);
//				System.out.println(piecesOn.get(i).getColor() + piecesOn.get(i).getClass().toString() + 
//						" of " + temp.toString() + " can see " + (new Square(x2, y2, false)).toString());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns the King chess piece of specified color
	 * @param kingColor color of king to find
	 * @return king of passed color
	 */
	public static ChessPiece findKing(Color kingColor) {
		for (int i = 0; i < piecesOn.size(); i++) {
			if (piecesOn.get(i).getClass().toString().equals("class chessPieces.King") && piecesOn.get(i).getColor().equals(kingColor))
				return piecesOn.get(i);
		}
		throw new IllegalArgumentException("Error: cannot find " + kingColor + " king.");
	}
	
	public boolean kingInCheck(Color kingColor) {
		for (int i = 0; i < piecesOn.size(); i++) {
			if (!piecesOn.get(i).getColor().equals(kingColor) && 
					piecesOn.get(i).getMoveList().contains(new Square(findKing(kingColor).getXSquare(), findKing(kingColor).getYSquare(), false)))
				return true;
		}
		return false;
	}
	
	public static void updatePieceLists() {
		for (int i = 0; i < piecesOn.size(); i++) {
			piecesOn.get(i).updateMoves();
		}
	}
	
	public static void putPieceOnBoard(ChessPiece piece) {
		piecesOff.remove(piece);
		piecesOn.add(piece);
		
		// make new method to handle graphics for new piece (if ChessPieceGraphic is finished/implemented, delete this
	}
	
	public void flipBoard() {
		int max = 7;
		for (ChessPiece temp: piecesOn) {
			int newX = max - temp.getXSquare();
			int newY = max - temp.getYSquare();
			int xDisplacement = (newX - temp.getXSquare()) * Chess.getSquareDimension();
			int yDisplacement = (newY - temp.getYSquare()) * Chess.getSquareDimension();
			String pType = temp.getClass().toString();
			boolean pHasMoved = true;
			if (pType.equals("class chessPieces.Rook")) {
				pHasMoved = ((Rook) temp).getHasMoved();
			} else if (pType.equals("class chessPieces.Pawn")) {
				pHasMoved = ((Pawn) temp).getHasMoved();
			} else if (pType.equals("class chessPieces.King")) {
				pHasMoved = ((King) temp).getHasMoved();
			}
			temp.move(xDisplacement, yDisplacement);
			//TODO: always sets to false, should only set to false if was previously false
			switch (pType) {
			default: break;
			case ("class chessPieces.King"):
				if (!pHasMoved)
				((King) temp).resetHasMoved();
			break;
			case ("class chessPieces.Rook"):
				if (!pHasMoved)
				((Rook) temp).resetHasMoved();
			break;
			case ("class chessPieces.Pawn"):
				if (!pHasMoved)
				((Pawn) temp).resetHasMoved();
				((Pawn) temp).flipMovementDirection();
			break;
			}
		}
	}
	
	public boolean checkmate() {
		List<ChessPiece> backup = new ArrayList<ChessPiece>();
		for (ChessPiece c: piecesOn) {
			backup.add(c);
		}
		int legalMoves = 0;
		if (kingInCheck(Chess.getTurn())) {
			for (int i = 0; i < piecesOn.size(); i++) {
				if (piecesOn.get(i).getColor().equals(Chess.getOppositeTurn())) {
					if (i == piecesOn.size() - 1) return true;
					i++;
				}
				for (int j = 0; j < 8; j++) {
					for (int k = 0; k < 8; k++) {
						if (legalMoves > 0) return false;
						//TODO: doesn't handle castling or en passant correctly
						if (movePiece(piecesOn.get(i).getXSquare(),  piecesOn.get(i).getYSquare(), j, k)) {
							legalMoves++;
							Chess.lastMove();
							Chess.takeTurn();
//							piecesOn.clear();
//							for (ChessPiece c: backup) {
//								piecesOn.add(c);
//							}
						}
					}
				}
			}
		} else legalMoves = -1;
		return legalMoves == 0;
	}
	
	public List<ChessPiece> getPiecesOn() {
		return piecesOn;
	}
}
