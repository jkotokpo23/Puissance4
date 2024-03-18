package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class reads the different pictures of the game.
 */
public class PicConstruction {
	
	private static BufferedImage pic=null;

	public static BufferedImage player1() {
		try {
			pic=ImageIO.read(new File("./gui/pictures/player1.png"));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pic;
	}

	public static BufferedImage player2() {
		try {
			pic=ImageIO.read(new File("./gui/pictures/player2.png"));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pic;
	}

	public static BufferedImage emptycell() {
		try {
			pic=ImageIO.read(new File("./gui/pictures/emptycell.png"));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pic;
	}
}