package levelSolver.lowerBoundTranspositionTable;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        SolverVF solver = new SolverVF();

        boolean weak = false;
        if (args.length > 0 && args[0].equals("-w")) {
            weak = true;
        }
        
        String [] test = {"3232716"};
        // String [] test = {"2252576253462244111563365343671351441", 
        //     "7422341735647741166133573473242566",
        //     "23163416124767223154467471272416755633",
        //     "71255763773133525731261364622167124446454",
        //     "65214673556155731566316327373221417",
        //     "52677675164321472411331752454",
        //     "71165555742443273243763213427724",
        //     "562154564361751726662253737734213275114",
        //     "233377345754465174223731671122611552",
        //     "6763525635134453444361412671365712",
        //     "32113735114523512272777153652673 3",
        //     "652331172222457134742634464673135775",
        //     "41157313573623152655422176126627",
        //     "52751626222765267437674374433416331155511",
        //     "45512272164216512124665566753",
        //     "34441157374153736716346556774311552222",
        //     "557671311761447661663222331375",
        //     "73711455213245356452463362145367227167174",
        //     "57571521273372553771133411265426",
        //     "63411624267622313431756753411657735527244",
        //     "743147475311235374153667575364254661",
        //     "13221637674233315611217775236",
        //     "35371324637717563211665236277265",
        //     "73337267741442232214473741513516265",
        //     "4652554254441727611466627637231573115733",
        //     "422273444275564571264157762567533",
        //     "6343274434344672215273311526556215"};

        try (Scanner scanner = new Scanner(System.in)) {
            //while (scanner.hasNextLine()) {
            int i = 0;
            long nbNoeud = 0;
            long temps = 0;
            while (i < test.length) {
                String line = test[i];
                PositionVF P = new PositionVF();
                int movesProcessed = P.play(line);

                if (movesProcessed != line.length()) {
                    System.err.println("Invalid move " + (P.nbMoves() + 1) + " \"" + line + "\"");
                } else {
                    solver.reset();
                    long startTime = getTimeMicrosec();
                    int score = solver.solve(P, weak);
                    long endTime = getTimeMicrosec();

                    long duration = endTime - startTime;
                    long node = solver.getNodeCount();
                    nbNoeud = nbNoeud + node;
                    temps = temps + duration;

                    System.out.println(line + " Score : " + score + "; " + node + " " + (duration));
                }
                i++;
            }
            System.out.println("Noeud " + nbNoeud / test.length + "; Temps : " + (temps / test.length));
        }
    }

    // Get micro-second precision timestamp using System.currentTimeMillis() in Java
    private static long getTimeMicrosec() {
        return System.currentTimeMillis() * 1000L;
    }
}
