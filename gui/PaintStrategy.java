package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import config.GameConfiguration;

import composants.Pion;
import composants.Tableau;

public class PaintStrategy {

	private BufferedImage player1;
	private BufferedImage player2;
	//private BufferedImage emptycell;

	public void paint(Tableau tableau, Graphics graphics) {

		int blockSize = GameConfiguration.BLOCK_SIZE;
		//Pion[][] pions = tableau.getTable();

		player1 = PicConstruction.player1();
		player2 = PicConstruction.player2();
		//emptycell = PicConstruction.emptycell();

		for (int lineIndex = 0; lineIndex < 6; lineIndex++) {
			for (int columnIndex = 0; columnIndex < 7; columnIndex++) {
				//Pion pion = pions[lineIndex][columnIndex];
				//Pion pion = tableau.getPion(lineIndex,columnIndex);
			
				graphics.drawLine(columnIndex * blockSize, lineIndex * blockSize, (columnIndex+1) * blockSize,  lineIndex * blockSize);
				graphics.drawLine(columnIndex * blockSize, lineIndex * blockSize, columnIndex * blockSize,  (lineIndex+1) * blockSize);
				
				graphics.drawLine(columnIndex * blockSize, (lineIndex+1) * blockSize, (columnIndex+1) * blockSize,  (lineIndex+1) * blockSize);
				graphics.drawLine((columnIndex+1) * blockSize, lineIndex * blockSize, (columnIndex+1) * blockSize,  (lineIndex+1) * blockSize);
			}
		}
	}

	public void paint(Pion pion, Graphics graphics) {
		int blockSize = GameConfiguration.BLOCK_SIZE;

		int y = pion.getY();
		int x = pion.getX();
		char couleur = pion.getCouleur();

		if(couleur=='0'){
			((Graphics2D) graphics).drawImage(player1, x * blockSize, y * blockSize,blockSize,blockSize, null);
		}
		else{
			((Graphics2D) graphics).drawImage(player2, x * blockSize, y * blockSize,blockSize,blockSize, null);
		}
	}

}
