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
		Pion pion;
		char couleur;
		player1 = PicConstruction.player1();
		player2 = PicConstruction.player2();

		for (int lineIndex = 0; lineIndex < 6; lineIndex++) {
			for (int columnIndex = 0; columnIndex < 7; columnIndex++) {
				//Pion pion = pions[lineIndex][columnIndex];
				pion = tableau.getPion(lineIndex,columnIndex);
				couleur = pion.getCouleur();
				graphics.drawLine(columnIndex * blockSize, lineIndex * blockSize, (columnIndex+1) * blockSize,  lineIndex * blockSize);
				graphics.drawLine(columnIndex * blockSize, lineIndex * blockSize, columnIndex * blockSize,  (lineIndex+1) * blockSize);
				
				graphics.drawLine(columnIndex * blockSize, (lineIndex+1) * blockSize, (columnIndex+1) * blockSize,  (lineIndex+1) * blockSize);
				graphics.drawLine((columnIndex+1) * blockSize, lineIndex * blockSize, (columnIndex+1) * blockSize,  (lineIndex+1) * blockSize);
				
				if(couleur!='x'){
					if(couleur=='0'){
						((Graphics2D) graphics).drawImage(player1, columnIndex * blockSize, lineIndex * blockSize,blockSize,blockSize, null);
					}
					else{
						((Graphics2D) graphics).drawImage(player2, columnIndex * blockSize, lineIndex * blockSize,blockSize,blockSize, null);
					}
				}
			}
		}
	}

}
