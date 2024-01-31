package composants;

public class Test {
    public static void main(String[] args) {
        Solver s = new Solver();
        int[] tab = new int[7];
        Position p = new Position();
        String sequence = "75671334317317336771215665546";
        p.play(sequence); 
        int score = s.solveNegamaxWT(p,tab);
        //int score = s.solveNegamax(p);
        for(int i = 0; i<7; i++){
            System.out.print(tab[i] + " | ");
        }
    }
}
