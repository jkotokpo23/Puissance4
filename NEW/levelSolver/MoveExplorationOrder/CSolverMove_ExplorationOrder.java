package levelSolver.MoveExplorationOrder;

import java.util.Scanner;
import composants.Position;


public class CSolverMove_ExplorationOrder {

    private long nodeCount;
    private int[] columnOrder; // column exploration order

    public int solveAlpha_Beta_Move_ExplorationOrder(Position P, boolean weak) 
    {
      nodeCount = 0;
      if(weak) 
        return alpha_beta(P, -1, 1);
      else 
        return alpha_beta(P, -Position.WIDTH*Position.HEIGHT/2, Position.WIDTH*Position.HEIGHT/2);
    }

    public CSolverMove_ExplorationOrder() {
        nodeCount = 0;
        columnOrder = new int[Position.WIDTH];
        for (int i = 0; i < Position.WIDTH; i++)
            columnOrder[i] = Position.WIDTH / 2 + (1 - 2 * (i % 2)) * (i + 1) / 2;
    }

    public int solveNegamax(Position P) {
        nodeCount = 0;
        return negamax(P);
    }

    @SuppressWarnings("unused")
    private int[] initTab(int[] tab){
        for(int i = 0; i < Position.WIDTH; i++){
            tab[i] = Position.WORSTSCORE;
        }
        return tab;
    }

    @SuppressWarnings("unused")
    private int[] copyTab(int[] tab){
        int [] tabR = new int[7];
        for(int i = 0; i < Position.WIDTH; i++){
            tabR[i] = tab[i];
        }
        return tab;
    }

    public int solveAlphaBeta(Position P) {
        nodeCount = 0;
        return alpha_beta(P, -42,42);
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

        CSolverMove_ExplorationOrder solver = new CSolverMove_ExplorationOrder();
        Scanner scanner = new Scanner(System.in);
        boolean weak = false;


       
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Position P = new Position();
            if (P.play(line) != line.length()) {
                System.err.println("Invalid move: \"" + line + "\"");
            } else {
                long startTime = System.nanoTime();

                    int score = solver.solveAlpha_Beta_Move_ExplorationOrder(P, weak);

                long endTime = System.nanoTime();
                System.out.println("Sequence : " +line + ";  Score : " + score + ";  Iterations : " + solver.getNodeCount() + ";  Temps de calcul en ms : " + (endTime - startTime) / 1000);
                System.out.println(P);
            }
        }

        scanner.close();
    }
}
