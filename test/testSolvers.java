package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import composants.Solver;
import composants.Position;

/**
 *  SOME ELEMENTS OF BENCHMARKS FOR TESTS
 * 
    2252576253462244111563365343671351441 -1
    7422341735647741166133573473242566 1
    23163416124767223154467471272416755633 0
    71255763773133525731261364622167124446454 0
    65214673556155731566316327373221417 -1
    52677675164321472411331752454 0
    3135151421347443544172316522225776773566 0
    562154564361751726662253737734213275114 0
    233377345754465174223731671122611552 1
    6763525635134453444361412671365712 -1
    211376455663355325112113664364524722 0
    3146762114467714356347741621375222 -1
    67152117737262713366376314254 6
    2762751722231276466633475674533 5
    3642756176227637211322113551637574556 2
    22647455554314246733661634615122372377511 0
    427566236745127177115664464254 2
    7172212567451542223676134464437761515 0
    641154574541323641152467137655232232366 0
    5775265212657176476365522624313714333 2
    3575316255751336464276636772271112 -3
 */

public class testSolvers {
    Solver solver;
    String sequence;
    Position tableau;
    int cost;
    int score;

    @Before
    public void prepareTable(){
        solver = new Solver();
        tableau = new Position();
        cost = 0;
    }

    @Test
    public void testBenchMarksNegamax(){
        sequence = "2252576253462244111563365343671351441";
        score = -1;
        tableau.play(sequence);
        assertEquals(solver.solveNegamax(tableau), score);
        tableau = new Position();

        sequence = "427566236745127177115664464254";
        score = 2;
        tableau.play(sequence);
        assertEquals(solver.solveNegamax(tableau), score);
        tableau = new Position();

        sequence = "3575316255751336464276636772271112";
        score = -3;
        tableau.play(sequence);
        assertEquals(solver.solveNegamax(tableau), score);
        tableau = new Position();
    }

    @Test
    public void testBenchMarksAlphaBeta(){
        sequence = "2252576253462244111563365343671351441";
        score = -1;
        tableau.play(sequence);
        assertEquals(solver.solveAlphaBeta(tableau), score);
        tableau = new Position();

        sequence = "427566236745127177115664464254";
        score = 2;
        tableau.play(sequence);
        assertEquals(solver.solveAlphaBeta(tableau), score);
        tableau = new Position();

        sequence = "3575316255751336464276636772271112";
        score = -3;
        tableau.play(sequence);
        assertEquals(solver.solveAlphaBeta(tableau), score);
        tableau = new Position();
    }

    @Test
    public void testAlphaBetaMoreEffectiveThanNegamax(){
        sequence = "655651721435342216255374674123";
        //Alpha-Beta
        tableau.play(sequence);
        long startTime = System.nanoTime();
            int scoreAB = solver.solveAlphaBeta(tableau);
        long endTime = System.nanoTime();
        int nbNodeAB = (int) solver.getNodeCount();
        double durationAB = endTime - startTime;
        endTime = 0; startTime = 0;

        //Negamax
        tableau = new Position();
        tableau.play(sequence);
        startTime = System.nanoTime();
            int scoreN = solver.solveNegamax(tableau);
        endTime = System.nanoTime();
        int nbNodeN = (int) solver.getNodeCount();
        double durationN = endTime - startTime;

        assertTrue(scoreN == scoreAB);               // Same result
        assertTrue(durationAB <= durationN);         // Duration
        assertTrue(nbNodeAB <= nbNodeN);             // Explored node
    }
}