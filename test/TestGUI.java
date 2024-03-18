package test;

import gui.MainGUI;


public class TestGUI {

	public static void main(String[] args) {

		MainGUI gameMainGUI = new MainGUI();

		Thread gameThread = new Thread(gameMainGUI);

		gameThread.start();
	}

}