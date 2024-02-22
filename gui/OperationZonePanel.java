package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Font;


import javax.swing.JPanel;
import javax.swing.JButton;

public class OperationZonePanel extends JPanel {
    
    private JButton rang1 = new JButton("1");
    private JButton rang2 = new JButton("2");
    private JButton rang3 = new JButton("3");
    private JButton rang4 = new JButton("4");
    private JButton rang5 = new JButton("5");
    private JButton rang6 = new JButton("6");
    private JButton rang7 = new JButton("7");

	private Font font = new Font(Font.MONOSPACED, Font.BOLD, 15);

	private static final long serialVersionUID = 1L;

    public OperationZonePanel(){
        setLayout(new GridLayout(1,7,5,0));
        
		setBackground(Color.WHITE);

        rang1.setFont(font);
        rang2.setFont(font);
        rang3.setFont(font);
        rang4.setFont(font);
        rang5.setFont(font);
        rang6.setFont(font);
        rang7.setFont(font);

        add(rang1);
        add(rang2);
        add(rang3);
        add(rang4);
        add(rang5);
        add(rang6);
        add(rang7);
        
    }


}
