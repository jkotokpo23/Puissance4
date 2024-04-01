package levelSolver.finalVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Launch {

    public static void main(String[] args) {
        Solver solver = new Solver();
        boolean weak = false;

        if (args.length > 0 && args[0].equals("-w")) {
            weak = true;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line ;

        try {
            while ((line = reader.readLine()) != null) {
                Position P = new Position();

                int movesPlayed = P.play(line);
                
                if (movesPlayed != line.length()) {
                    System.err.println("Invalid move " + (P.nbMoves() + 1) + " \"" + line + "\"");
                } else {
                    solver.reset();
                    long startTime = System.nanoTime();
                    int score = solver.solve(P, weak);
                    long endTime = System.nanoTime();
                    System.out.println(line + " " + score + " " + solver.getNodeCount() + " " + (endTime - startTime));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
