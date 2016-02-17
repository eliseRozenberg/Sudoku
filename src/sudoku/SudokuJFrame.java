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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame; // Uses Swing's Container/Components
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

// Uses AWT's Event Handlers

//remove variables and fix
///Organize code

/**
 * The Sudoku game. To solve the number puzzle, each row, each column, and each
 * of the nine 3×3 sub-grids shall contain all of the digits from 1 to 9
 */
public class SudokuJFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int boardSize = 9; // Size of the board
	private int subGridSize = 3; // Size of the sub-grid
	private int cellSize = 60; // Cell width/height in pixels
	private int boardWidth = cellSize * boardSize;
	private int boardHeight = cellSize * boardSize;

	private Color backgroundColor = Color.pink;
	private Color emptyCellColor = Color.white;
	private Color filledCellColor = Color.YELLOW;
	private Font numbersFont = new Font("Monospaced", Font.BOLD, 20);

	private Container container;
	private JTextField[][] guiBoard;
	private SudokuCell[][] board;
	private SudokuBoard sudokuBoard;

	private JButton easy;
	private JButton medium;
	private JButton hard;
	private JButton check;
	private JButton reset;
	private JButton hint;

	private JLabel messageLabel;

	private JPanel sudokuPanel;
	private JPanel levelPanel;
	private JPanel utilitiesPanel;

	private int hintAmount = 3;
	private int easyAmount = 2;
	private int mediumAmount = 45;
	private int hardAmount = 50;
	private int level;
	private int hiddenAmount;

	/**
	 * Constructor to setup the game and the UI Components
	 */
	public SudokuJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle window closing
		setTitle("Sudoku");

		container = getContentPane();

		sudokuPanel = new JPanel();
		levelPanel = new JPanel();
		utilitiesPanel = new JPanel();

		messageLabel = new JLabel("SUDOKU");

		sudokuBoard = new SudokuBoard();
		guiBoard = new JTextField[boardSize][boardSize];

		easy = new JButton();
		medium = new JButton();
		hard = new JButton();
		check = new JButton("Check Board");
		hint = new JButton("Hint (" + hintAmount + ")");
		reset = new JButton("Reset");

		// Construct 9x9 JTextFields and add to the content-pane
		for (int row = 0; row < boardSize; ++row) {
			for (int col = 0; col < boardSize; ++col) {
				// set the size to only accept 1 number and only numbers
				// make listeners for keyboard arrows
				guiBoard[row][col] = new JTextField();// array
				// guiBoard[row][col].setBorder(new LineBorder(Color.black, 1,
				// true));
				sudokuPanel.add(guiBoard[row][col]); // ContentPane adds
				// JTextField

				// Beautify all the cells
				guiBoard[row][col].setHorizontalAlignment(JTextField.CENTER);
				guiBoard[row][col].setFont(numbersFont);

				guiBoard[row][col].addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						JTextField textField = (JTextField) e.getSource();
						for (int row = 0; row < boardSize; ++row) {
							for (int col = 0; col < boardSize; ++col) {
								if (guiBoard[row][col] == textField) {
									setBorder(row, col);
									return;
								}
							}
						}
					}
				});
			}
		}

		format();
		addPanels();
		setGame(easyAmount);

		// Set the size of the content-pane and pack all the components
		// under this container.
		sudokuPanel.setPreferredSize(new Dimension(boardWidth, boardHeight));

		addActionListeners();
		pack();

	}

	public void format() {
		container.setLayout(new BorderLayout());
		container.setBackground(backgroundColor);

		sudokuPanel.setLayout(new GridLayout(boardSize, boardSize)); // 9x9
		sudokuPanel.setBorder(new LineBorder(Color.BLACK, 3, true));

		levelPanel.setBackground(backgroundColor);
		levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));

		easy.setHorizontalAlignment(JButton.CENTER);
		easy.setIcon(new ImageIcon("easy.jpg"));
		easy.setBackground(backgroundColor);
		easy.setBorder(new LineBorder(Color.black, 0, true));

		medium.setHorizontalAlignment(JButton.CENTER);
		medium.setBackground(backgroundColor);
		medium.setIcon(new ImageIcon("medium.jpg"));
		medium.setBorder(new LineBorder(Color.black, 0, true));

		hard.setHorizontalAlignment(JButton.CENTER);
		hard.setBackground(backgroundColor);
		hard.setBorder(new LineBorder(Color.black, 0, true));
		hard.setIcon(new ImageIcon("hard.jpg"));

		utilitiesPanel.setBackground(backgroundColor);
		utilitiesPanel.setLayout(new FlowLayout());

		messageLabel.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 70));
	}

	public void addPanels() {

		levelPanel.add(Box.createRigidArea(new Dimension(5, 30)));
		levelPanel.add(easy);
		levelPanel.add(Box.createRigidArea(new Dimension(5, 20)));
		levelPanel.add(medium);
		levelPanel.add(Box.createRigidArea(new Dimension(5, 20)));
		levelPanel.add(hard);
		container.add(levelPanel, BorderLayout.EAST);

		utilitiesPanel.add(hint);
		utilitiesPanel.add(check);
		utilitiesPanel.add(reset);
		container.add(utilitiesPanel, BorderLayout.SOUTH);

		container.add(messageLabel, BorderLayout.NORTH);
		container.add(sudokuPanel);
	}

	public void newGame(int difficulty) {
		messageLabel.setText("SUDOKU");
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		sudokuBoard.clearTable();
		sudokuBoard.fillBoard(difficulty);
		board = sudokuBoard.getBoard();
		hint.setEnabled(true);
		hintAmount = 3;
		hint.setText("Hint (" + hintAmount + ")");
		// Construct 9x9 JTextFields and add to the content-pane
		for (int row = 0; row < boardSize; ++row) {
			for (int col = 0; col < boardSize; ++col) {

				if (!board[row][col].isShown()) {
					guiBoard[row][col].setText(""); // set to empty string
					guiBoard[row][col].setEditable(true);
					guiBoard[row][col].setBackground(emptyCellColor);

				} else {
					guiBoard[row][col].setText(board[row][col].getValue() + "");
					guiBoard[row][col].setEditable(false);
					guiBoard[row][col].setBackground(filledCellColor);
				}
				setBorder(row, col);
			}
		}
	}

	public void setBorder(int row, int col) {
		guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

		/*
		 * if (row == guiBoard[0].length - 1) {
		 * guiBoard[row][col].setBorder(BorderFactory.createMatteBorder( 1, 1,
		 * 4, 1, Color.BLACK));
		 * 
		 * } if (col == guiBoard.length - 1) {
		 * guiBoard[row][col].setBorder(BorderFactory.createMatteBorder( 1, 1,
		 * 1, 4, Color.BLACK));
		 * 
		 * }
		 */

		if (col % 3 == 0) { // add logic for grid borders
			// set left border....
			guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(1, 4, 1, 1, Color.BLACK));
		}
		if (row % 3 == 0) {
			guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(4, 1, 1, 1, Color.BLACK));
		}
		if (col % 3 == 0 && row % 3 == 0) {
			guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(4, 4, 1, 1, Color.BLACK));
		}

	}

	public void setGame(int levelAmount) {
		newGame(levelAmount);
		level = levelAmount;
		hiddenAmount = levelAmount;
	}

	public void addActionListeners() {
		//
		easy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setGame(easyAmount);
			}
		});
		medium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setGame(mediumAmount);
			}
		});
		hard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setGame(hardAmount);
			}
		});
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int checkedAmount = 0;
				for (int row = 0; row < board.length; row++) {
					for (int col = 0; col < board.length; col++) {
						if (!board[row][col].isShown() && !guiBoard[row][col].getText().equals("")) {
							// if not shown and not empty text, then check box
							if (!sudokuBoard.check(Integer.parseInt(guiBoard[row][col].getText()), row, col)) {
								// if not correct set border to red
								guiBoard[row][col].setBorder(new LineBorder(Color.red, 2, true));

								// when does it change back - after input?
								// add listener
							} else {
								checkedAmount++;
								guiBoard[row][col].setEditable(false);
								guiBoard[row][col].setBackground(filledCellColor);
							}
						}
					}
				}
				if (checkedAmount == level) {
					win();
				}
			}
		});

		// randomizer to choose hint cell or can do sequentially
		// loop to go in order
		// also a limit that will reset when use reset button or new game

		hint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (hasEmpty()) {
					hint.setText("Hint (" + --hintAmount + ")");
					Random rand = new Random();
					int row, col;
					do {
						row = rand.nextInt(9);
						col = rand.nextInt(9);
					} while (!guiBoard[row][col].getText().equals(""));
					guiBoard[row][col].setText(board[row][col].getValue() + "");
					guiBoard[row][col].setBackground(filledCellColor);
					guiBoard[row][col].setEditable(false);
					if (hintAmount == 0) {
						hint.setEnabled(false);
						return;
					}
				} else {
					hint.setEnabled(false);
				}
			}
		});
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int row = 0; row < board.length; row++) {
					for (int col = 0; col < board.length; col++) {
						if (!board[row][col].isShown()) {
							guiBoard[row][col].setText("");
							guiBoard[row][col].setEditable(true);
							setBorder(row, col);
							hint.setEnabled(true);
							hintAmount = 3;
							hint.setText("Hint (" + hintAmount + ")");
						}
					}
				}
			}

		});
	}

	public boolean hasEmpty() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				if (!board[row][col].isShown() && guiBoard[row][col].getText().equals("")) {
					return true;
				}
			}
		}
		return false;
	}

	public void win() {
		messageLabel.setText("You Won!!");
		hint.setEnabled(false);
		reset.setEnabled(false);
		check.setEnabled(false);
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				if (!board[row][col].isShown()) {
					guiBoard[row][col].setEditable(false);
				}
			}
		}
	}

	/** The entry main() entry method */
	public static void main(String[] args) {
		SudokuJFrame frame = new SudokuJFrame();
		frame.setVisible(true);
	}

}
