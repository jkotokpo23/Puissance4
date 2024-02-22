package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import engine.map.Map;
import config.GameConfiguration;


/**
 * Manage all the informations between every Panels.
 */
public class MainGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private Map map;

	private GameDisplay dashboard;

	private OperationZonePanel operationZonePanel;

	private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);

	public MainGUI(String title) {
		super(title);
		init();
	}

	private void init() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,20));

		map = new Map();
		dashboard = new GameDisplay(map);
		operationZonePanel = new OperationZonePanel();

		operationZonePanel.setPreferredSize(new Dimension(GameConfiguration.WINDOW_WIDTH,50));
		dashboard.setPreferredSize(new Dimension(GameConfiguration.WINDOW_WIDTH,685));
		
		contentPane.add(dashboard, BorderLayout.NORTH);
		contentPane.add(operationZonePanel, BorderLayout.SOUTH);
				
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setBackground(Color.black);
		setVisible(true);
		setPreferredSize(preferredSize);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	@Override
	public void run() {
		
		while (true) {
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			dashboard.repaint();
		}
	}

}
