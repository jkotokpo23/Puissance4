package composants;

public class Test {
    public static void main(String[] args) {
        Solver s = new Solver();
        int[] tab = new int[7];
        Position p = new Position();
        String sequence = "2762751722231276466633475674533";
        p.play(sequence); 
        int score = s.solveNegamaxWT(p,tab);
        //int score = s.solveNegamax(p);
        for(int i = 0; i<7; i++){
            if(tab[i] != Position.WORSTSCORE)
                System.out.print(tab[i] + " | ");
            else
                System.out.print("X" + " | ");
        }
        System.out.println("Score " + score);
    }
}
