package sudoku;

import java.awt.BorderLayout;
import java.awt.Color; // Uses AWT's Layout Managers
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame; // Uses Swing's Container/Components
import javax.swing.JPanel;
import javax.swing.JTextField;

// Uses AWT's Event Handlers

/**
 * The Sudoku game. To solve the number puzzle, each row, each column, and each
 * of the nine 3×3 sub-grids shall contain all of the digits from 1 to 9
 */
public class SudokuJFrame extends JFrame {
	// Name-constants for the game properties
	public static final int GRID_SIZE = 9; // Size of the board
	public static final int SUBGRID_SIZE = 3; // Size of the sub-grid

	// Name-constants for UI control (sizes, colors and fonts)
	public static final int CELL_SIZE = 60; // Cell width/height in pixels
	public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
	public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
	// Board width/height in pixels
	public static final Color OPEN_CELL_BGCOLOR = Color.YELLOW;
	public static final Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0); // RGB
	public static final Color OPEN_CELL_TEXT_NO = Color.RED;
	public static final Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240); // RGB
	public static final Color CLOSED_CELL_TEXT = Color.BLACK;
	public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

	// The game board composes of 9x9 JTextFields,
	// each containing String "1" to "9", or empty String
	private JTextField[][] guiBoard = new JTextField[GRID_SIZE][GRID_SIZE];
	private JPanel sudokuPanel;
	private SudokuCell[][] board;
	private SudokuBoard sudokuBoard = new SudokuBoard();
	private JButton easy;
	private JButton medium;
	private JButton hard;
	private JButton check;
	private JButton hint;
	private JPanel levelPanel;
	private JPanel utilitiesPanel;

	/**
	 * Constructor to setup the game and the UI Components
	 */
	public SudokuJFrame() {
		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		sudokuPanel = new JPanel();
		sudokuPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE)); // 9x9
		// GridLayout

		levelPanel = new JPanel();
		levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));

		easy = new JButton("Easy");
		medium = new JButton("Medium");
		hard = new JButton("Hard");
		check = new JButton("Check Board");
		hint = new JButton("Hint");

		levelPanel.add(easy);
		levelPanel.add(medium);
		levelPanel.add(hard);

		container.add(levelPanel, BorderLayout.EAST);

		utilitiesPanel = new JPanel();
		utilitiesPanel.setLayout(new FlowLayout());

		utilitiesPanel.add(hint);
		utilitiesPanel.add(check);

		container.add(utilitiesPanel, BorderLayout.SOUTH);

		// Construct 9x9 JTextFields and add to the content-pane
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				// set the size to only accept 1 number
				guiBoard[row][col] = new JTextField(); // Allocate element of
				// array
				sudokuPanel.add(guiBoard[row][col]); // ContentPane adds
				// JTextField

				// Beautify all the cells
				guiBoard[row][col].setHorizontalAlignment(JTextField.CENTER);
				guiBoard[row][col].setFont(FONT_NUMBERS);
			}
		}

		newGame(40);

		// Set the size of the content-pane and pack all the components
		// under this container.
		sudokuPanel.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		container.add(sudokuPanel);
		addActionListeners();
		pack();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle window closing
		setTitle("Sudoku");
		setVisible(true);
	}

	public void newGame(int difficulty) {
		sudokuBoard.fillBoard(difficulty);
		board = sudokuBoard.getBoard();

		// Construct 9x9 JTextFields and add to the content-pane
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (!board[row][col].isShown()) {
					guiBoard[row][col].setText(""); // set to empty string
					guiBoard[row][col].setEditable(true);
					guiBoard[row][col].setBackground(OPEN_CELL_BGCOLOR);

				} else {
					guiBoard[row][col].setText(board[row][col].getValue() + "");
					guiBoard[row][col].setEditable(false);
					guiBoard[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					guiBoard[row][col].setForeground(CLOSED_CELL_TEXT);
				}
			}
		}
	}

	public void addActionListeners() {
		//fix levels
		easy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newGame(40);
			}
		});
		medium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newGame(50);
			}
		});
		hard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newGame(60);
			}
		});
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board.length; j++) {
						if (!board[i][j].isShown() && guiBoard[i][j].getText() != "") {
							if (!sudokuBoard.check(guiBoard[i][j].getText(), i, j)) {
								guiBoard[i][j].setBackground(Color.RED);
								// when does it change back - after input?
							}
						}

					}
				}
			}
		});
		hint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
	}

	/** The entry main() entry method */
	public static void main(String[] args) {
		SudokuJFrame frame = new SudokuJFrame();
		frame.setVisible(true);
	}
}
