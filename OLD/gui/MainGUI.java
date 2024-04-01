package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import composants.Tableau;
import config.GameConfiguration;
import process.Manager;


/**
 * Manage all the informations between every Panels.
 */
public class MainGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private Tableau tableau;
	private GameDisplay dashboard;
	private Manager manager;

	private OperationZonePanel operationZonePanel;

	private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);

	public MainGUI() {
		super("Puissance 4");
		init();
	}

	private void init() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(0,20));

		tableau = new Tableau();
		manager = new Manager(tableau);
		dashboard = new GameDisplay(tableau);
		operationZonePanel = new OperationZonePanel(manager);

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
		
		while (!manager.fin()) {
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			dashboard.repaint();
		}
	}

}
