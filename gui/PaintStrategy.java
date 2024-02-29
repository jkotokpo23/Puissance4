package gui;

import java.awt.Color;
import java.awt.Graphics;

import config.GameConfiguration;

import engine.map.Block;
import engine.map.Map;

import composants.Pion;


public class PaintStrategy {

	public void paint(Map map, Graphics graphics) {

		int blockSize = GameConfiguration.BLOCK_SIZE;
		Block[][] blocks = map.getBlocks();

		for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
			for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
				Block block = blocks[lineIndex][columnIndex];
			
				graphics.drawLine(block.getColumn() * blockSize, block.getLine() * blockSize, (block.getColumn()+1) * blockSize,  block.getLine() * blockSize);
				graphics.drawLine(block.getColumn() * blockSize, block.getLine() * blockSize, block.getColumn() * blockSize,  (block.getLine()+1) * blockSize);
				
				graphics.drawLine(block.getColumn() * blockSize, (block.getLine()+1) * blockSize, (block.getColumn()+1) * blockSize,  (block.getLine()+1) * blockSize);
				graphics.drawLine((block.getColumn()+1) * blockSize, block.getLine() * blockSize, (block.getColumn()+1) * blockSize,  (block.getLine()+1) * blockSize);
			}
		}
	}

	public void paint(Pion pion, Graphics graphics) {
		int blockSize = GameConfiguration.BLOCK_SIZE;

		int y = pion.getY();
		int x = pion.getX();
		char couleur = pion.getCouleur();

		if(couleur=='0'){
			graphics.setColor(Color.RED);
		}
		else{
			graphics.setColor(Color.YELLOW);
		}
		graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
	}

}
