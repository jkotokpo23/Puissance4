package engine.board;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Move {

	private int row;
	private int column;
	private int value;

	public Move(int row, int col) {
		this.row = row;
		this.column = col;
	}

	public Move(int value) {
		this.value = value;
	}

	public Move(int row, int col, int value) {
		this.row = row;
		this.column = col;
		this.value = value;
	}

}
