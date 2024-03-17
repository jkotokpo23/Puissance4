package composants;

public class Tableau {

    private static int DIMX = 7;
    private static int DIMY = 6;
    private static char vide = 'x';
    private static char joueur1 = '0';
    private static char joueur2 = '1';
    
    private Pion[][] table = new Pion[DIMX][DIMY];

    public Tableau(){
        for(int i = 0; i < DIMY; i++ ){
            for(int j = 0; j < DIMX; j++){
                table[j][i] = new Pion(0, 0, vide);
            }
        }
    }
    
    public Pion[][] getTable() {
        return table;
    }

	public Pion getPion(int line, int column) {
		return table[line][column];
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
