package sudoku;

import java.io.Serializable;

public class SudokuCell implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int value;
	// private int userValue;
	private boolean show;

	public SudokuCell() {
		show = false;
		// userValue = 0;
	}

	/*
	 * public int getUserValue() { return userValue; }
	 * 
	 * public void setUserValue(int userValue) { this.userValue = userValue; }
	 * 
	 * public boolean isFilled() { return userValue != 0; }
	 */

	public int getValue() {
		return value;
	}

	// used by the board generator
	public void setValue(final int number) {
		show = true;
		value = number;
	}

	public void reset() {
		value = 0;
		show = false;
	}

	public void show() {
		show = true;
	}

	public void hide() {
		show = false;
	}

	public boolean isShown() {
		return show;
	}

}
