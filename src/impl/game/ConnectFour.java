package impl.game;

import java.util.Random;

import api.Chip;
import api.Game;
import exc.GameIndexOutOfBoundsException;
import exc.GameStateException;

public class ConnectFour extends Game {
	private int rows, columns;
	private final int CONNECT = 4; // required to connect 4 in a row 
	private Chip[][] board;
	private Chip currentPlayer;
	private Chip winner;
	private boolean gameIsOver;
	
	
	
	public ConnectFour() {
		/*
		 * Constructor initiates variables to the first 
		 */
		this.rows = 6;
		this.columns = 7;
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
	
	@Override
	public int getRows() {
		return this.rows;
	}

	@Override
	public int getColumns() {
		return this.columns;
	}
	
	@Override
	public Chip getCurrentPlayer() {
		return this.currentPlayer; 
	}

	@Override
	public Chip getWinningPlayer() throws GameStateException {
		/*
		 * getter function for winner. expects that the game is over. 
		 * If there is no winner, throw an exception
		 * winner is set in placeChip
		 */
		if (!this.isGameOver() || this.winner == null || this.winner.equals(Chip.EMPTY)) {
			System.out.println("Error: winner does not exist");
			throw new GameStateException();
		}
		return this.winner;
	}

	private void switchPlayer() {
		/*
		 * Change the currentPlayer to the other player
		 * If game is over, set it to empty
		 * Doesn't assume, but should be the case that, currentPlayer is already empty or null
		 */
		assert this.currentPlayer != null;
		assert !this.currentPlayer.equals(Chip.EMPTY);

		this.currentPlayer = (this.isGameOver()) ? Chip.EMPTY :
			(this.currentPlayer.equals(Chip.BLUE)) ? Chip.RED : Chip.BLUE;
		this.setChanged();
	}

	@Override
	public Chip getChip(int row, int col) throws GameIndexOutOfBoundsException {
		if (row < 0 || row > this.getRows()-1 ||
				col < 0 || col > this.getColumns()-1) {
			System.out.println("Error: getChip is called on a non-existent row or col");
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
			System.out.println("Error: placeChip is called after game is over");
			throw new GameStateException();
		}
		if (row != 0 || col < 0 || col > this.getColumns()-1) {
			System.out.println("Error: placeChip was given row!=0 or non-existent column");
			throw new GameIndexOutOfBoundsException(row, col);
		}
		if ((row = this.getPlaceableRow(col)) < 0) {
			// Also finds bottom-most empty spot for given column and assign it to row
			System.out.println("Error: placeChip was called but board was full");
			throw new GameIndexOutOfBoundsException(row, col);
		};

		Chip p = this.getCurrentPlayer();
		board[row][col] = p;
		if (this.checkWin(p, row, col)) {
			this.winner = p;
			this.gameIsOver = true;
			this.switchPlayer();
			this.setChanged();
			this.notifyObservers("Game Over");
		} else if (this.boardIsFull()) {
			this.winner = Chip.EMPTY;
			this.gameIsOver = true;
			this.switchPlayer();
			this.setChanged();
			this.notifyObservers("Game Over");
		} else {
			this.switchPlayer();
			this.setChanged();
			this.notifyObservers("Next Round");
		}
		
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

	private boolean checkWin(Chip p, int row, int col) {
		// Awesome for-loops -- can we call all logic algorithm? -- that checks if player wins
		// Be sure to check out my diagonal check. It's beautiful.
//		int count;

		//Horizontal check
		for (int c = 0, count = 0; c < this.getColumns(); c++) {
			count = (this.getChip(row, c).equals(p)) ? count+1 : 0;
			if (count >= this.CONNECT) return true;
		}
		
		//Vertical check	
		for (int r = 0, count = 0; r < this.getRows(); r++) {
			count = (this.getChip(r, col).equals(p)) ? count+1 : 0;
			if (count >= this.CONNECT) return true;
		}
		
		
		int R = this.getRows(); // number of rows
		int C = this.getColumns(); // number of columns
		int D = R + C - 1; // total number of diagonals
		int X; // X (row, first index) of starting point of each diagonal
		int Y; // Y (col, second index) of starting point of each diagonal
		int n; // Number of elements in/length of each diagonal

		//Main diagonal check for every diagonal d that has length > CONNECT
		for (int d = this.CONNECT-1; d < D-(this.CONNECT-1); d++) { // first and last few diagonals aren't long enough
			X = Math.max(0, R-(d+1)); // X is zero along the top row, R-(d+1) along the left col
			Y = Math.max(0, (d+1)-R); // Y is (d+1)-R along the top row, zero along the left col
			n = Math.min(Math.min(R, C), Math.min(d+1, D-d)); // length of diagonal
			for (int i = 0, count = 0; i < n; i++) {
				count = (this.getChip(X+i, Y+i).equals(p)) ? count+1 : 0; // reset counter if we see something different
				if (count >= this.CONNECT) return true; // stop as soon as we see the 4th one
			}
		}
		
		// Reverse diagonal check
		for (int d = this.CONNECT-1; d < D-(this.CONNECT-1); d++) { 
			X = Math.max(0, R-(d+1));
			Y = (C-1) - Math.max(0, (d+1)-R);
			n = Math.min(Math.min(R, C), Math.min(d+1, D-d));
			for (int i = 0, count = 0; i < n; i++) {
				count = (this.getChip(X+i, Y-i).equals(p)) ? count+1 : 0;
				if (count >= this.CONNECT) return true;
			}
		}
 
		return false;
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




