package composants;

import java.util.Scanner;

public class Solver {

    private long nodeCount;

    public int solveNegamax(Position P) {
        nodeCount = 0;
        return negamax(P);
    }

    public int solveAlphaBeta(Position P) {
        nodeCount = 0;
        return alpha_beta(P,-42,42);
    }

    public int alpha_beta(Position P, int alpha, int beta) {
        nodeCount++;
        if(P.nbMoves() == Position.WIDTH * Position.HEIGHT)
            return 0;

        for(int x = 0; x < Position.WIDTH; x++)
            if(P.canPlay(x) && P.isWinningMove(x))
                return (Position.WIDTH * Position.HEIGHT + 1 - P.nbMoves()) / 2;

        int max = (Position.WIDTH * Position.HEIGHT - 1 - P.nbMoves()) / 2;
        if(beta > max) {
            beta = max;
            if(alpha >= beta) return beta;
        }

        for(int x = 0; x < Position.WIDTH; x++)
            if(P.canPlay(x)) {
                Position P2 = Position.copy(P);
                P2.play(x);
                int score = -alpha_beta(P2, -beta, -alpha);

                if(score >= beta) return score;
                if(score > alpha) alpha = score;
            }
        return alpha;
    }

    private int negamax(Position P) {
        nodeCount++;

        if (P.nbMoves() == Position.WIDTH * Position.HEIGHT) // Vérifier le match nul.
            return 0;

        for (int x = 0; x < Position.WIDTH; x++)
            if (P.canPlay(x) && P.isWinningMove(x))
                return (Position.WIDTH * Position.HEIGHT + 1 - P.nbMoves()) / 2;

        int bestScore = -Position.WIDTH * Position.HEIGHT;

        for (int x = 0; x < Position.WIDTH; x++) 
            if (P.canPlay(x)) {
                Position P2 = Position.copy(P);
                P2.play(x);
                int score = -negamax(P2);
                if (score > bestScore) bestScore = score;
            }

        return bestScore;
    }

    public long getNodeCount() {
        return nodeCount;
    }

    public static void main(String[] args) {

        Solver solver = new Solver();
        Scanner scanner = new Scanner(System.in);

       
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Position P = new Position();
            if (P.play(line) != line.length()) {
                System.err.println("Invalid move: \"" + line + "\"");
            } else {
                long startTime = System.nanoTime();

                    //Test negamax
                    int score = solver.solveNegamax(P);

                    //int score = solver.solveAlphaBeta(P);

                long endTime = System.nanoTime();
                System.out.println("Sequence : " +line + ";  Score : " + score + ";  Iterations : " + solver.getNodeCount() + ";  Temps de calcul en ms : " + (endTime - startTime) / 1000);
                System.out.println(P);
            }
        }

        scanner.close();
    }
}
