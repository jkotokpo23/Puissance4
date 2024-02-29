package levelSolver.lowerBoundTranspositionTable;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        SolverVF solver = new SolverVF();

        boolean weak = false;
        if (args.length > 0 && args[0].equals("-w")) {
            weak = true;
        }

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            PositionVF P = new PositionVF();
            int movesProcessed = P.play(line);

            if (movesProcessed != line.length()) {
                System.err.println("Invalid move " + (P.nbMoves() + 1) + " \"" + line + "\"");
            } else {
                solver.reset();
                long startTime = getTimeMicrosec();
                int score = solver.solve(P, weak);
                long endTime = getTimeMicrosec();
                System.out.println(line + " Score : " + score + "; " + solver.getNodeCount() + " " + (endTime - startTime)/1000);
            }
        }
    }

    // Get micro-second precision timestamp using System.currentTimeMillis() in Java
    private static long getTimeMicrosec() {
        return System.currentTimeMillis() * 1000L;
    }
}
