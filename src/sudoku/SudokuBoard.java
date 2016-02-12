package sudoku;

import java.util.Random;

public class SudokuBoard {

	private final int blockSize;
	private final int boardSize;
	private final int numberOfCells;
	private SudokuCell[][] board;

	public SudokuBoard() {
		blockSize = 3;
		boardSize = blockSize * blockSize;
		numberOfCells = boardSize * boardSize;
		board = new SudokuCell[9][9];
		for (int i = 0; i < 9; ++i) {
			for (int j = 0; j < 9; ++j) {
				board[i][j] = new SudokuCell();
			}
		}

	}

	public SudokuCell[][] getBoard() {
		return board;
	}

	public void fillBoard(int difficulty) {
		nextCell(0, 0);
		makeHoles(difficulty);
	}

	/**
	 * Recursive method that attempts to place every number in a cell.
	 *
	 * @param x
	 *            x value of the current cell
	 * @param y
	 *            y value of the current cell
	 * @return true if the board completed legally, false if this cell has no
	 *         legal solutions.
	 */
	public boolean nextCell(int x, int y) {
		int nextX = x;
		int nextY = y;
		int[] toCheck = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		Random r = new Random();
		int tmp = 0;
		int current = 0;
		int top = toCheck.length;

		for (int i = top - 1; i > 0; i--) {
			current = r.nextInt(i);
			tmp = toCheck[current];
			toCheck[current] = toCheck[i];
			toCheck[i] = tmp;
		}

		for (int i = 0; i < toCheck.length; i++) {
			if (legalMove(x, y, toCheck[i])) {
				board[x][y].setValue(toCheck[i]);
				if (x == 8) {
					if (y == 8) {
						return true;// We're done! Yay!
					} else {
						nextX = 0;
						nextY = y + 1;
					}
				} else {
					nextX = x + 1;
				}
				if (nextCell(nextX, nextY)) {
					return true;
				}
			}
		}
		board[x][y].setValue(0);
		return false;
	}

	/**
	 * Given a cell's coordinates and a possible number for that cell, determine
	 * if that number can be inserted into said cell legally.
	 *
	 * @param x
	 *            x value of cell
	 * @param y
	 *            y value of cell
	 * @param current
	 *            The value to check in said cell.
	 * @return True if current is legal, false otherwise.
	 */
	private boolean legalMove(int x, int y, int current) {
		for (int i = 0; i < 9; i++) {
			if (current == board[x][i].getValue()) {
				return false;
			}
		}
		for (int i = 0; i < 9; i++) {
			if (current == board[i][y].getValue()) {
				return false;
			}
		}
		int cornerX = 0;
		int cornerY = 0;
		if (x > 2) {
			if (x > 5) {
				cornerX = 6;
			} else {
				cornerX = 3;
			}
		}
		if (y > 2) {
			if (y > 5) {
				cornerY = 6;
			} else {
				cornerY = 3;
			}
		}
		for (int i = cornerX; i < 10 && i < cornerX + 3; i++) {
			for (int j = cornerY; j < 10 && j < cornerY + 3; j++) {
				if (current == board[i][j].getValue()) {
					return false;
				}
			}
		}
		return true;
	}

	public void makeHoles(int holesToMake) {
		/*
		 * We define difficulty as follows: Easy: 32+ clues (49 or fewer holes)
		 * Medium: 27-31 clues (50-54 holes) Hard: 26 or fewer clues (54+ holes)
		 * This is human difficulty, not algorighmically (though there is some
		 * correlation)
		 */
		double remainingSquares = 81;
		double remainingHoles = holesToMake;

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				double holeChance = remainingHoles / remainingSquares;
				if (Math.random() <= holeChance) {
					board[i][j].hide();
					remainingHoles--;
				}
				remainingSquares--;
			}
		}
	}

	public boolean check(int userVal, int row, int column) {
		if (userVal == board[row][column].getValue()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Prints a representation of board on stdout
	 */
	public void print() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(board[i][j].getValue() + "  ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		SudokuBoard myBoard = new SudokuBoard();
		myBoard.fillBoard(45);
		myBoard.print();
	}
}