package engine.ai;

import engine.board.Board;
import engine.board.Move;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AI {

	// Variable that holds which player plays.
	private int aiPlayer;

	public AI(int aiPlayer) {
		this.aiPlayer = aiPlayer;
	}

	public abstract Move getNextMove(Board board);

}
