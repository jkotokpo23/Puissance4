package process;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//import composants.Pion;
import composants.Tableau;


public class Manager {
    private Tableau tableau;
    private JButton buttons[];
    private int tour;     
	private boolean gameOver = false;
    
    public Manager(Tableau tableau){
        this.tableau=tableau;
        this.tour=0;
        buttons = new JButton[7];
        for(int i=0; i<7; i++){
            buttons[i]=new JButton(Integer.toString(i));
        }
    }
		
    public JButton[] getButtons(){
        return buttons;
    }

    /**
    * Game winning state
    * If the four cell is same, user 1 will win the game
    * @param winner integer If the player 1 is equal to 1, otherwise 2
    **/
    public void winnerPlayer(char winner){
        for(int i=0; i<6; ++i){         
            for(int j=0; j<7; ++j){     
                if(tableau.getPion(i,j).getCouleur() == winner){    
                    // Vérification de la colonne
                    if(i+3<6){
                        if(tableau.getPion(i+1,j).getCouleur() == winner && tableau.getPion(i+2,j).getCouleur() == winner && tableau.getPion(i+3,j).getCouleur() == winner)  {
                            if(winner=='1')
                                showResult(1);
                            else
                                showResult(2);
                        }
                    }

                    // Vérification de la ligne gauche -> droite
                    if(j + 3 <7){ 
                        if(tableau.getPion(i,j+1).getCouleur() == winner && tableau.getPion(i,j+2).getCouleur() == winner && tableau.getPion(i,j+3).getCouleur() == winner){ 
                            if(winner=='1')
                                showResult(1);
                            else
                                showResult(2);
                        }
                    }
                    
                    // Vérification de la diagonale gauche -> droite
                    if(i  < 6-3 && j < 7-3){
                        if(tableau.getPion(i+1,j+1).getCouleur() == winner && tableau.getPion(i+2,j+2).getCouleur() == winner && tableau.getPion(i+3,j+3).getCouleur() == winner){  
                            if(winner=='1')
                                showResult(1);
                            else
                                showResult(2);
                        }   
                    }

                    // Vérification de la diagonale gauche -> droite
                    if(i  < 6-3 && j - 3 >= 0 ){
                        if(tableau.getPion(i+1,j-1).getCouleur() == winner && tableau.getPion(i+2,j-2).getCouleur() == winner && tableau.getPion(i+3,j-3).getCouleur() == winner){
                            if(winner=='1')
                                showResult(1);
                            else
                                showResult(2);
                        } 
                    }                             
                    
                    // Vérification de la diagonale droite -> gauche
                    if(tour == 42){
                        showResult(0);
                    }                      
                }         
            }             
        } 
    } 
    
   
    /**
     * Show winner player on the new frame
     * @param winnerPlayer integer if the parameter is equal to 1,player 1 is winner.Otherwise, player 2
     */
   public void showResult(int winnerPlayer){
       JFrame frameShowResult = new JFrame();       
       if(winnerPlayer==1){
            JOptionPane.showMessageDialog(frameShowResult,
            "\nGagnant : Joueur 1\n\nUne nouvelle partie va commencer.\n\n",
            "Fin de partie",
            JOptionPane.INFORMATION_MESSAGE);
            gameOver = true;
            //startAgain(); 
       }
       else if(winnerPlayer==2){
            JOptionPane.showMessageDialog(frameShowResult,
            "\nGagnant : Joueur 2\n\nUne nouvelle partie va commencer.\n\n",
            "Fin de partie",
            JOptionPane.INFORMATION_MESSAGE); 
            gameOver = true;
            //startAgain();    
       }
       else{
            JOptionPane.showMessageDialog(frameShowResult,
            "\nMatch nul \n\nUne nouvelle partie va commencer.\n\n",
            "Fin de partie",
            JOptionPane.INFORMATION_MESSAGE); 
            gameOver = true;
            //startAgain();    
       }
       gameOver=true;
   }

   /* A ARRANGER POUR LES ACTIONS LISTENERS */

    /**
     *
     * Action listener to game button
     * Computer vs Player 1
     */
    public class PlayerListenner implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try { 
                int j=0;
                // Get the button component that was clicked
                while(buttons[j] != e.getSource() && j<6){
                    j++;
                }
                if (tableau.colPleine(j)){
                    warningMessage();
                }
                else {
                    if (tour%2 == 0 ){
                        tableau.posePion(j,1);                    
                        ++tour;  // Change player order from player 1 to computer
                        winnerPlayer('0');    // Check game winning state
                    }
                    else {
                        tableau.posePion(j,2);              
                        // System.out.println("le joueur 2 à joué dans la colonne: "+j);
                        ++tour;  // Change player order from player 1 to computer
                        winnerPlayer('1');    // Check game winning state
                    }
                }
            } // END TRY
            catch(Exception ex){ 
                warningMessage();
                System.err.println(ex);
            }
        } // END ACTION PERFORMED
              
    } // END listenButtonOnePlayer CLASS

    public void warningMessage(){
        JFrame frameWarning = new JFrame();           
        JOptionPane.showMessageDialog(frameWarning,
        "Mouvement impossible!!\nLa colonne est pleine.", "Warning",
        JOptionPane.WARNING_MESSAGE);
    }

    public PlayerListenner playerListenner(){
		return new PlayerListenner();
	}

    public boolean fin(){
        return gameOver;
    }

}
