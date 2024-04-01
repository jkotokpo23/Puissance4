package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JButton;

import process.Manager;


public class OperationZonePanel extends JPanel {
    
    private JButton buttons[];

	private Font font = new Font(Font.MONOSPACED, Font.BOLD, 15);

	private static final long serialVersionUID = 1L;

    public OperationZonePanel(Manager manager){
        setLayout(new GridLayout(1,7,5,0));
        
		setBackground(Color.WHITE);

        buttons=manager.getButtons();
        for(int i=0; i<7; i++){
            buttons[i].setFont(font);
            buttons[i].addActionListener(manager.playerListenner());
            add(buttons[i]);
        }
    }


}
