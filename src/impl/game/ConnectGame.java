package impl.game;

import java.util.Random;

import api.Chip;
import api.Game;
import exc.GameIndexOutOfBoundsException;
import exc.GameStateException;

public abstract class ConnectGame extends Game {
	private final int ROWS, COLUMNS, CONNECT;
	private Chip currentPlayer;
	protected boolean gameIsOver;
	protected Chip winner;
	private Chip[][] board;

	public ConnectGame(int rows, int columns, int connect) {
		this.currentPlayer = new Chip[]{Chip.BLUE, Chip.RED}[new Random().nextInt(2)];
		this.winner = null;
		this.gameIsOver = false;
		this.ROWS = rows;
		this.COLUMNS = columns;
		this.CONNECT = connect;
		this.board = new Chip[this.ROWS][this.COLUMNS];
		for (int i = 0; i < this.ROWS; i++) {
			for (int j = 0; j < this.COLUMNS; j++) {
				board[i][j] = Chip.EMPTY;
			}
		}
		this.setChanged();
	}

	@Override
	public int getRows() {
		return this.ROWS;
	}

	@Override
	public int getColumns() {
		return this.COLUMNS;
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
	
	protected void switchPlayer() {
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
		return this.board[row][col];
	}


	@Override
	public abstract void placeChip(int row, int col) throws GameStateException;
	
	protected void _placeChip(int row, int col, Chip p) {
		if (row < 0 || row > this.getRows()-1 || 
			col < 0 || col > this.getColumns()-1) {
			System.out.println("Error: placeChip was given row!=0 or non-existent column");
			throw new GameIndexOutOfBoundsException(row, col);
		}
		assert p != null;
		this.board[row][col] = p;
		this.setChanged();
	}


	public Chip findWinner() {
		// Finds the chip that has the most inarows
		Chip winner = Chip.EMPTY;
		int maxInarows = 0;

		// Find chip that has maximum number of inarows.
		for (Chip p : Chip.values()) {
			if (!p.equals(Chip.EMPTY)) {
				int in_a_row_count = this.countInarows(p);
				if (in_a_row_count > maxInarows) {
					maxInarows = in_a_row_count;
					winner = p;
				}
			}
		}
		
		// if there are any other chip that has the same (or more) number of inarows, nobody is a winner.
		for (Chip p : Chip.values()) {
			if (!p.equals(Chip.EMPTY) && !p.equals(winner)) {
				if (this.countInarows(p) >= maxInarows) {
					return Chip.EMPTY;
				}
			}
		}
		return winner;
	}

	private int countInarows(Chip p) {
		int in_a_row_count = 0;
		//Horizontal check
		for (int r = 0; r < this.getRows(); r++) {
			for (int c = 0, count = 0; c < this.getColumns(); c++) {
				count = (this.getChip(r, c).equals(p)) ? count+1 : 0;
				if (count >= this.CONNECT) in_a_row_count += 1;
			}
		}
		
		//Vertical check	
		for (int c = 0; c < this.getColumns(); c++) {
			for (int r = 0, count = 0; r < this.getRows(); r++) {
				count = (this.getChip(r, c).equals(p)) ? count+1 : 0;
				if (count >= this.CONNECT) in_a_row_count += 1;
			}
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
				if (count >= this.CONNECT) in_a_row_count += 1; // stop as soon as we see the 4th one
			}
		}
		// Reverse diagonal check
		for (int d = this.CONNECT-1; d < D-(this.CONNECT-1); d++) { 
			X = Math.max(0, R-(d+1));
			Y = (C-1) - Math.max(0, (d+1)-R);
			n = Math.min(Math.min(R, C), Math.min(d+1, D-d));
			for (int i = 0, count = 0; i < n; i++) {
				count = (this.getChip(X+i, Y-i).equals(p)) ? count+1 : 0;
				if (count >= this.CONNECT) in_a_row_count += 1;
			}
		}
		return in_a_row_count;
	}
	
	public boolean boardIsFull() {
		/*
		 * Checks if board is full (true) or has any empty space (false)
		 */
		for (int col = 0; col < this.getColumns(); col++) {
			for (int row = 0; row < this.getRows(); row++) {
				if (this.getChip(row, col).equals(Chip.EMPTY)) return false;
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
