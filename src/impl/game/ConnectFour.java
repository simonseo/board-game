package impl.game;

import java.util.Random;

import api.Chip;
import api.Game;
import exc.GameIndexOutOfBoundsException;
import exc.GameStateException;

public class ConnectFour extends Game {
	private int rows, columns;
	private Chip[][] board;
	private Chip currentPlayer;
	private Chip winner;
	private boolean gameIsOver;
	
	
	public ConnectFour() {
		/*
		 * Constructor initiates variables to the first 
		 */
		this.rows = 5;
		this.columns = 6;
		this.currentPlayer = new Chip[]{Chip.BLUE, Chip.RED}[new Random().nextInt(2)];
		this.winner = null;
		this.gameIsOver = false;
		this.board = new Chip[this.rows][this.columns];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				board[i][j] = Chip.EMPTY;
			}
		}
	}
	
	public void run() {
		// TODO figure out game flow
		/*
		 * Called by update function in Console
		 * 1. ask for user input
		 * 2. place (check win, 
		 * 4. 
		 */
		assert !this.isGameOver();
		
		//do stuff
		
		
		if (!this.isGameOver()) {
			this.notifyObservers("player so and so");
		} else {
			this.notifyObservers("game over");
		}
	}
	

	@Override
	public int getRows() {
		return this.rows;
	}

	@Override
	public int getColumns() {
		return this.columns;
	}

	@Override
	public Chip getChip(int row, int col) {
		if (row < 0 || row > this.getRows()-1 ||
				col < 0 || col > this.getRows()-1) {
			throw new GameIndexOutOfBoundsException(row, col);
		}
		return board[row][col];
	}

	@Override
	public void placeChip(int row, int col) throws GameStateException, GameIndexOutOfBoundsException {
		/*
		 * Return the chip currently placed at a given coordinate. 
		 * If a player were physically sitting at a table viewing an upright Connect Four board, 
		 * the upper left corner would correspond to row zero, column zero; 
		 * while the lower right corner would correspond to row five, column six.
		 */
		if (this.isGameOver()) {
			throw new GameStateException();
		}
		if (row != 0 || col < 0 || col > this.getRows()-1) {
			throw new GameIndexOutOfBoundsException(row, col);
		}
		if ((row = this.getPlaceableRow(col)) < 0) {
			// If column is full
			throw new GameIndexOutOfBoundsException(row, col);
		};

		Chip p = this.getCurrentPlayer();
		board[row][col] = p;
		if (this.checkWin(p)) {
			this.winner = p;
			this.gameIsOver = true;
		} else if (this.boardIsFull()) {
			this.winner = Chip.EMPTY;
			this.gameIsOver = true;
		}
		this.switchPlayer();
	}

	private boolean boardIsFull() {
		/*
		 * Checks if board is full (true) or has any empty space (false)
		 */
		for (int col = 0; col < this.getColumns(); col++) {
			if (this.getPlaceableRow(col) >= 0) {
				return false;
			}
		}
		return true;
	}

	private int getPlaceableRow(int col) {
		/*
		 * Returns the lowest row that is empty for given column 
		 * assumes that col is within bounds
		 */
		assert col >= 0 && col < this.getColumns();
		for (int row = this.getRows() - 1; row >= 0; row--) {
			if (this.getChip(row, col).equals(Chip.EMPTY)) {
				return row;
			}
		}
		return -1;
	}

	private boolean checkWin(Chip p) {
		// Awesome for-loops -- can we call all logic algorithm? -- that checks if player wins 
		return false;
	}

	private void switchPlayer() {
		/*
		 * Change the currentPlayer to the other player
		 * If game is over, set it to empty
		 */
		assert this.currentPlayer != null;
		this.currentPlayer = (this.isGameOver()) ? Chip.EMPTY :
			(this.currentPlayer.equals(Chip.BLUE)) ? Chip.RED : Chip.BLUE;
	}

	@Override
	public Chip getWinningPlayer() throws GameStateException {
		/*
		 * getter function for winner. expects that the game is over. 
		 * If there is no winner, throw an exception
		 * winner is set in placeChip
		 */
		if (!this.isGameOver() || this.winner == null || this.winner.equals(Chip.EMPTY)) {
			throw new GameStateException();
		}
		return this.winner;
	}

	
	@Override
	public Chip getCurrentPlayer() {
		return this.currentPlayer; 
	}

	@Override
	public boolean isGameOver() {
		/*
		 * getter function for gameIsOver
		 * gameIsOver is set in placeChip
		 * The game is over if it is a tie or a winner has been determined.
		 */
		return this.gameIsOver;
	}
	
}