package composants;

public class Test {
    public static void main(String[] args) {
        Solver s = new Solver();
        int[] tab = new int[7];
        Position p = new Position();
        String sequence = "275737723461";
        p.play(sequence); 
        int score = s.solveAlphBetaWT(p,tab);
        for(int i = 0; i<7; i++){
            if(tab[i] != Position.WORSTSCORE)
                System.out.print(tab[i] + " | ");
            else
                System.out.print("X" + " | ");
        }
        System.out.println("\nMoves " + p.nbMoves());
        System.out.println("Score " + score);
    }
}
