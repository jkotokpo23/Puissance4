package engine.ai;


import engine.board.Board;
import engine.board.Move;
import engine.util.Constants;

import java.util.ArrayList;
import java.util.Random;


public class MinimaxAlphaBetaPruningAI extends MinimaxAI {

	public MinimaxAlphaBetaPruningAI() {
		super(Constants.DEFAULT_MAX_DEPTH, Constants.P2);
	}

	public MinimaxAlphaBetaPruningAI(int maxDepth, int aiPlayer) {
		super(maxDepth, aiPlayer);
	}

	@Override
	public Move getNextMove(Board board) {
		// If P1 plays then it wants to MAXimize the heuristics value.
		if (getAiPlayer() == Constants.P1) {
			return maxAlphaBeta(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
		// If P2 plays then it wants to MINimize the heuristics value.
		else {
			return minAlphaBeta(new Board(board), 0, Double.MIN_VALUE, Double.MAX_VALUE);
		}
	}

	// The max and min functions are called interchangeably, one after another until a max depth is reached.
	private Move maxAlphaBeta(Board board, int depth, double a, double b) {
		Random r = new Random();

		/* If MAX is called on a state that is terminal or after a maximum depth is reached,
		 * then a heuristic is calculated on the state and the move returned.
		 */
		if ((board.checkForGameOver()) || (depth == super.getMaxDepth())) {
			return new Move(board.getLastMove().getRow(), board.getLastMove().getColumn(), board.evaluate());
		}
		// The children-moves of the state are calculated
		ArrayList<Board> children = new ArrayList<>(board.getChildren(Constants.P1));
		Move maxMove = new Move(Integer.MIN_VALUE);
		for (Board child : children) {
			// And for each child min is called, on a lower depth.
			Move move = minAlphaBeta(child, depth + 1, a, b);
			// The child-move with the greatest value is selected and returned by max.
			if (move.getValue() >= maxMove.getValue()) {
				if (move.getValue() > maxMove.getValue()
						// If the heuristic has the same value then we randomly choose one of the two moves
						|| r.nextInt(2) == 0 || move.getValue() == Integer.MIN_VALUE) {
					maxMove.setRow(child.getLastMove().getRow());
					maxMove.setColumn(child.getLastMove().getColumn());
					maxMove.setValue(move.getValue());
				}
			}

			// Update the "a" of the current max node.
			a = (a > maxMove.getValue()) ? a : maxMove.getValue();

			// Beta pruning.
			if (a >= b) {
				return maxMove;
			}
		}
		return maxMove;
	}

	// Min works similarly to max.
	private Move minAlphaBeta(Board board, int depth, double a, double b) {
		Random r = new Random();

		if ((board.checkForGameOver()) || (depth == super.getMaxDepth())) {
			return new Move(board.getLastMove().getRow(), board.getLastMove().getColumn(), board.evaluate());
		}
		ArrayList<Board> children = new ArrayList<>(board.getChildren(Constants.P2));
		Move minMove = new Move(Integer.MAX_VALUE);
		for (Board child : children) {
			Move move = maxAlphaBeta(child, depth + 1, a, b);
			if (move.getValue() <= minMove.getValue()) {
				if (move.getValue() < minMove.getValue()
						|| r.nextInt(2) == 0 || move.getValue() == Integer.MAX_VALUE) {
					minMove.setRow(child.getLastMove().getRow());
					minMove.setColumn(child.getLastMove().getColumn());
					minMove.setValue(move.getValue());
				}
			}

			// Update the "b" of the current min node.
			b = (b < minMove.getValue()) ? b : minMove.getValue();

			// Alpha pruning
			if (b <= a) {
				return minMove;
			}
		}
		return minMove;
	}

}
