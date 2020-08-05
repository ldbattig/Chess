package chessPieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ChessPieceGraphic {

	private BufferedImage img;
	private String imgFile;
	private ChessPiece c;
	private Graphics g;
	
	public ChessPieceGraphic(String file, ChessPiece piece, Graphics gr) {
		imgFile = file;
		c = piece;
		g = gr;
		if (initializeImage())
			paintComponent(g);
	}
	
	public boolean initializeImage() {
		try {
			img = ImageIO.read(new File(imgFile));
			return true;
		} catch (IOException e) {
			System.out.println("Error while loading chess piece png file.");
			return false;
		}
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(img, c.getXCoord(), c.getYCoord(), null);
	}
}
