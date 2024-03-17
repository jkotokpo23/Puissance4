package process;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import composants.Pion;
import composants.Tableau;


public class Manager {
    private Tableau tableau;
    int tour;
    private static int livingCellNumber=0;        
	private boolean gameOver = false;
    
    public Manager(Tableau tableau){
        this.tableau=tableau;
        this.tour=0;
    }
		
	// }

        /**
     * Game winning state
     * If the four cell is same, user 1 will win the game
     * @param winner integer If the player 1 is equal to 1, otherwise 2
     */
    public void winnerPlayer(int winner)
    {
        for(int i=0; i<7; ++i)
        {         
            for(int j=0; j<6; ++j)
            {     
                if(tableau.getPion(i,j).getCouleur() == winner)
                {    
                    // CHECK UP TO DOWN POSITIONS
                    if(i+3<7)
                    {
                        if(tableau.getPion(i+1,j).getCouleur() == winner && tableau.getPion(i+2,j).getCouleur() == winner && tableau.getPion(i+3,j).getCouleur() == winner)  
                        {
                            if(winner==1)
                                showResult(1);
                            else
                                showResult(2);
                        }
                    }
                    // CHECK LEFT TO RIGHT POSITION
                    if(j + 3 <6)
                    { 
                        if(tableau.getPion(i,j+1).getCouleur() == winner && tableau.getPion(i,j+2).getCouleur() == winner && tableau.getPion(i,j+3).getCouleur() == winner)
                        { 
                            if(winner==1)
                                showResult(1);
                            else
                                showResult(2);
                        }
                    }

                    // CHECK DIAGONAL LEFT TO RIGHT POSITION
                    if(i  < 7-3 && j < 6-3)
                    {
                        if(tableau.getPion(i+1,j+1).getCouleur() == winner && tableau.getPion(i+2,j+2).getCouleur() == winner && tableau.getPion(i+3,j+3).getCouleur() == winner)
                        {  
                            if(winner==1)
                                showResult(1);
                            else
                                showResult(2);
                        }   
                    }

                    // CHECK DIAGONAL RIGHT TO LEFT POSITION
                    if(i  < 7-3 && j - 3 >= 0 )
                    {
                        if(tableau.getPion(i+1,j-1).getCouleur() == winner && tableau.getPion(i+2,j-2).getCouleur() == winner && tableau.getPion(i+3,j-3).getCouleur() == winner)
                        {
                            if(winner==1)
                                showResult(1);
                            else
                                showResult(2);
                        } 
                    }                             
                    
                    // CHECK DIAGONAL RIGHT TO LEFT POSITION
                    if(livingCellNumber == 42)
                    {
                        showResult(0);
                    }                      
                }         
            }             
        } 
    } // End winnerPlayer function
    
   
    /**
     * Show winner player on the new frame
     * @param winnerPlayer integer if the parameter is equal to 1,player 1 is winner.Otherwise, player 2
     */
   public void showResult(int winnerPlayer)
   {
       JFrame frameShowResult = new JFrame();       
       if(winnerPlayer==1)
       {
            JOptionPane.showMessageDialog(frameShowResult,
            "\nGagnant : Joueur 1\n\nUne nouvelle partie va commencer.\n\n",
            "Fin de partie",
            JOptionPane.INFORMATION_MESSAGE);
            //startAgain(); 
       }
       else if(winnerPlayer==2)
       {
            JOptionPane.showMessageDialog(frameShowResult,
            "\nGagnant : Joueur 2\n\nUne nouvelle partie va commencer.\n\n",
            "Fin de partie",
            JOptionPane.INFORMATION_MESSAGE); 
            //startAgain();    
       }
       else
       {
            JOptionPane.showMessageDialog(frameShowResult,
            "\nMatch nul \n\nUne nouvelle partie va commencer.\n\n",
            "Fin de partie",
            JOptionPane.INFORMATION_MESSAGE); 
            //startAgain();    
       }
       gameOver=true;
   }

   /* A ARRANGER POUR LES ACTIONS LISTENERS */

    // /**
    //  *
    //  * Action listener to game button
    //  * Computer vs Player 1
    //  */
    // private class player1Listenner implements ActionListener
    // {
    //     @Override
    //     public void actionPerformed(ActionEvent e)
    //     {
    //         try { 
    //             if(0 == tour%2)   // Player 1 operations
    //             { 
    //                 for(int k=0; k<7; ++i)
    //                 {
    //                     // Player 1 Operations
    //                     // Fill the tableau from down to up
    //                     if(tableau.getPion(i-k,j).getCouleur() == 0)
    //                     {
    //                         buttons[i-k][j].setIcon(player1);           // Change button icon
    //                         tableau.getPion(i-k,j).setAllPosition('X', i);   // Set cell parameters
    //                         tableau.getPion(i-k,j).setCellState(1);          // Set cell state
    //                         ++livingCellNumber;                         // Increase living cell number
    //                         winnerPlayer(1);                            // Check game winning state
    //                         break; 
    //                     } 
    //                 }

    //                 setUpperCellToEmpty(i,j); // Set the upper cells to empty cell to listen button
    //                 System.out.println("le joueur 1 à joué ");
    //                 ++tour;  // Change player order from player 1 to computer
    //                 break;    
    //             }

    //             // Computer Operations
    //             // Basic idea is filling the cells left to right
                
    //             if(1 == tour%2) 
    //             { 
    //                 moveComputer(i);
    //                 System.out.println("l'IA à joué");
    //                 ++tour; // Change player order from computer to player 1
    //                 break;
    //             }
    //             else 
    //             {
    //                 warningMessage();
    //             }
    //         } // END TRY
    //         catch(Exception ex)
    //         { 
    //             warningMessage();
    //         }
    //     } // END ACTION PERFORMED
              
    // } // END listenButtonOnePlayer CLASS

    // public void warningMessage()
    // {
    //     JFrame frameWarning = new JFrame();           
    //     JOptionPane.showMessageDialog(frameWarning,
    //     "Invalid Movement !!\nThe cell is not empty.", "Warning",
    //     JOptionPane.WARNING_MESSAGE);
    // }
}
