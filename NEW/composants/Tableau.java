package composants;

public class Tableau {

    private static int DIMX = 7;
    private static int DIMY = 6;
    private static char vide = 'x';
    private static char joueur1 = '0';
    private static char joueur2 = '1';
    
    private Pion[][] table = new Pion[DIMY][DIMX];

    public Tableau(){
        for(int i = 0; i < DIMY; i++ ){
            for(int j = 0; j < DIMX; j++){
                table[i][j] = new Pion(j, i, vide);
            }
        }
    }
    
    public Pion[][] getTable() {
        return table;
    }

	public Pion getPion(int line, int column) {
		return table[line][column];
	}
    
    public Pion lastPion(int j){
        Pion pion,next;
        int i=0;
        pion=table[i][j];
        if(pion.getCouleur()==vide){
            next=table[i+1][j];
            while(i<5 && next.getCouleur()==vide){
                pion=table[i][j];
                next=table[i+1][j];
                i++;
            }
            if(next.getCouleur()==vide){
               return next; 
            }
        }
        return pion;
    }

    public void posePion(int j, int joueur){
        Pion pion = lastPion(j);
        int i = pion.getY();
        if(joueur==1){
            table[i][j].setCouleur(joueur1);
        }
        else {
            table[i][j].setCouleur(joueur2);
        }
    }

    public boolean colPleine(int colonne){
        Pion pion = table[0][colonne];
        return pion.getCouleur()!=vide;
    }


    public boolean firstPlayer(int colonne){
        int row = 0;
        while(row < DIMY && table[colonne][row].getCouleur() != vide){
            row++;
        }
        if(row == DIMY) return false;
        table[colonne][row] = new Pion(colonne, row, joueur1);
        return true;
    }

    public boolean secondPlayer(int colonne){
        int row = 0;
        while(row < DIMY && table[colonne][row].getCouleur() != vide){
            row++;
        }
        if(row == DIMY) return false;
        table[colonne][row] = new Pion(colonne, row, joueur2);
        return true;
        
    }

    @Override
    public String toString(){
        String resultat = "";
        for(int i = 0; i < DIMY; i++ ){
            for(int j = 0; j < DIMX; j++){
                resultat += "| " + table[j][i].getCouleur() + " |";
            }
            resultat += "\n";
        }
        return resultat;
    }
    
}
