package gui;

// import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.GridLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

import engine.map.Map;
// import composants.Pion;

public class GameDisplay extends JPanel {
	
    private static final long serialVersionUID = 1L;
    
    private Map map;
	private PaintStrategy paintStrategy = new PaintStrategy();

    public GameDisplay(Map map){
        this.map = map;
        // setLayout(new GridLayout(1,7,5,0));
        
		// setBackground(Color.WHITE);
    }

	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);

		paintStrategy.paint(map, g);
		// paintStrategy.paint(pion, g);
	}
    
}