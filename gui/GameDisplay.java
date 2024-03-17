package gui;

// import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.GridLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

import composants.Tableau;
// import composants.Pion;

public class GameDisplay extends JPanel {
	
    private static final long serialVersionUID = 1L;
    
    private Tableau tableau;
	private PaintStrategy paintStrategy = new PaintStrategy();

    public GameDisplay(Tableau tableau){
        this.tableau = tableau;
    }

	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintStrategy.paint(tableau, g);
	}
    
}