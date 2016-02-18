package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Paths;
import java.util.Random;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class SudokuJFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int boardSize;
	private int subGridSize;
	private int cellSize;
	private int boardWidth;
	private int boardHeight;

	private Color backgroundColor;
	private Color emptyCellColor;
	private Color filledCellColor;
	private Color borderColor;
	private Color buttonColor;
	private Color fontColor;
	private Font numbersFont;
	private Font messageLabelFont;
	private Font buttonFont;
	private Border levelButtonBorder;

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

	private int hintAmount;
	private int easyAmount;
	private int mediumAmount;
	private int hardAmount;
	private int level;

	/**
	 * Constructor to setup the game and the UI Components
	 */
	public SudokuJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle window closing
		setTitle("Sudoku");
		container = getContentPane();
		setDetails();

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

		// Construct 9x9 JTextFields and add to the container
		for (int row = 0; row < boardSize; ++row) {
			for (int col = 0; col < boardSize; ++col) {
				guiBoard[row][col] = new JTextField();
				sudokuPanel.add(guiBoard[row][col]);
				guiBoard[row][col].setHorizontalAlignment(JTextField.CENTER);
				guiBoard[row][col].setForeground(fontColor);
				guiBoard[row][col].setFont(numbersFont);
				guiBoard[row][col].addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						JTextField textField = (JTextField) e.getSource();
						if (textField.getText().length() > 1) {
							textField.setText(textField.getText().charAt(0) + "");
						}
						if (!textField.getText().matches("^[1-9]")) {
							textField.setText("");
						}
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
		addActionListeners();
		pack();
	}

	public void setDetails() {
		boardSize = 9; // Size of the board
		subGridSize = 3; // Size of the sub-grid
		cellSize = 60; // Cell width/height in pixels
		boardWidth = cellSize * boardSize;
		boardHeight = cellSize * boardSize;

		backgroundColor = Color.pink;
		emptyCellColor = Color.white;
		filledCellColor = new Color(255, 250, 205);
		borderColor = new Color(218, 165, 32);
		buttonColor = new Color(135, 206, 235);
		fontColor = new Color(144, 238, 144);
		numbersFont = new Font("Calibri", Font.BOLD, 40);
		messageLabelFont = new Font("Rockwell Extra Bold", Font.PLAIN, 100);
		buttonFont = new Font("Calibri", Font.BOLD, 40);
		levelButtonBorder = new LineBorder(Color.black, 0, true);

		hintAmount = 3;
		easyAmount = 40;
		mediumAmount = 45;
		hardAmount = 50;
	}

	public void format() {
		container.setLayout(new BorderLayout());
		container.setBackground(backgroundColor);

		sudokuPanel.setLayout(new GridLayout(boardSize, boardSize)); // 9x9
		sudokuPanel.setBorder(new LineBorder(borderColor, 1, false));
		sudokuPanel.setPreferredSize(new Dimension(boardWidth, boardHeight));

		levelPanel.setBackground(backgroundColor);
		levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));

		easy.setHorizontalAlignment(JButton.CENTER);
		easy.setIcon(new ImageIcon("easy.jpg"));
		easy.setBackground(backgroundColor);
		easy.setBorder(levelButtonBorder);

		medium.setHorizontalAlignment(JButton.CENTER);
		medium.setBackground(backgroundColor);
		medium.setIcon(new ImageIcon("medium.jpg"));
		medium.setBorder(levelButtonBorder);

		hard.setHorizontalAlignment(JButton.CENTER);
		hard.setBackground(backgroundColor);
		hard.setBorder(levelButtonBorder);
		hard.setIcon(new ImageIcon("hard.jpg"));

		hint.setPreferredSize(new Dimension(160, 50));
		hint.setFont(buttonFont);
		hint.setBackground(buttonColor);
		hint.setForeground(Color.white);

		check.setPreferredSize(new Dimension(250, 50));
		check.setFont(buttonFont);
		check.setForeground(Color.white);
		check.setBackground(buttonColor);

		reset.setPreferredSize(new Dimension(130, 50));
		reset.setFont(buttonFont);
		reset.setBackground(buttonColor);
		reset.setForeground(Color.white);

		utilitiesPanel.setBackground(backgroundColor);
		utilitiesPanel.setLayout(new FlowLayout());

		messageLabel.setFont(messageLabelFont);
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		messageLabel.setForeground(Color.WHITE);
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
		sudokuBoard.clearTable();
		sudokuBoard.fillBoard(difficulty);
		board = sudokuBoard.getBoard();
		check.setEnabled(true);
		reset.setEnabled(true);
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

	// add logic for grid borders
	public void setBorder(int row, int col) {
		guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor));
		if (col % subGridSize == 0) {
			guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(1, 4, 1, 1, borderColor));
		}
		if (row % subGridSize == 0) {
			guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(4, 1, 1, 1, borderColor));
		}
		if (col % subGridSize == 0 && row % subGridSize == 0) {
			guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(4, 4, 1, 1, borderColor));
		}
		if (col == boardSize - 1) {
			guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 4, borderColor));
			if (row == 0) {
				guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(4, 1, 1, 4, borderColor));
			}
			if (row % subGridSize == 0) {
				guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(4, 1, 1, 4, borderColor));
			}
			if (row == boardSize - 1) {
				guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, borderColor));
			}
		}
		if (row == boardSize - 1) {
			guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 4, 1, borderColor));
			if (col % subGridSize == 0) {
				guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(1, 4, 4, 1, borderColor));
			}
			if (col == boardSize - 1) {
				guiBoard[row][col].setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, borderColor));
			}
		}
	}

	public void setGame(int levelAmount) {
		newGame(levelAmount);
		level = levelAmount;
	}

	public void addActionListeners() {
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
								guiBoard[row][col].setBorder(new LineBorder(Color.red, 3, true));
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
				} else {// board is complete
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
							guiBoard[row][col].setBackground(emptyCellColor);
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

	public static void main(String[] args) {
		SudokuJFrame frame = new SudokuJFrame();
		frame.setVisible(true);
		JFXPanel fxPanel = new JFXPanel();
		Media media = new Media(Paths.get("song.mp3").toUri().toString());
		MediaPlayer player = new MediaPlayer(media);
		player.setOnEndOfMedia(new Runnable() {
			public void run() {
				player.seek(Duration.ZERO);
			}
		});
		player.play();
	}

}
