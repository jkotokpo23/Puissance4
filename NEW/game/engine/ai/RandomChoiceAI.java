package engine.ai;

import engine.board.Board;
import engine.board.Move;
import engine.util.Constants;

import java.util.Random;

public class RandomChoiceAI extends AI {

	private final int numOfColumns;

	public RandomChoiceAI() {
		super(Constants.P2);
		this.numOfColumns = Constants.CONNECT_4_NUM_OF_COLUMNS;
	}

	public RandomChoiceAI(int aiPlayer, int numOfColumns) {
		super(aiPlayer);
		this.numOfColumns = numOfColumns;
	}

	// Initiates the random move.
	@Override
	public Move getNextMove(Board board) {
		Random r = new Random();

		int col;
		do {
			col = r.nextInt(this.numOfColumns);
		} while (board.checkFullColumn(col));

		int row = board.getEmptyRowPosition(col);

		return new Move(row, col, getAiPlayer());
	}

}
