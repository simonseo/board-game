package impl.game;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import api.Chip;
import api.Game;
import exc.GameIndexOutOfBoundsException;
import exc.GameStateException;

public class Complica extends Game {
	private int rows, columns;
	private final int CONNECT = 4; // required to connect 4 in a row 
	private Chip[][] board;
	private Chip currentPlayer;
	private Chip winner;
	private boolean gameIsOver;
	
	
	public Complica() {
		/*
		 * Constructor initiates variables to the first 
		 */
		this.rows = 4;
		this.columns = 4;
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
		this._placeChip(row, col, p);
		this.setChanged();
		this.winner = this.findWinner();
		if (!this.winner.equals(Chip.EMPTY)) {
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

	private void _placeChip(int row, int col, Chip p) {
		if (row < 0 || row > this.getRows()-1 || 
			col < 0 || col > this.getColumns()-1) {
			System.out.println("Error: placeChip was given row!=0 or non-existent column");
			throw new GameIndexOutOfBoundsException(row, col);
		}
		assert p != null;
		this.board[row][col] = p;
	}

	private int getPlaceableRow(int col) {
		/*
		 * Returns the lowest row that is empty for given column
		 * If doesn't exist, shift everything by one and return 0 
		 * assumes that col is within bounds
		 */
		assert col >= 0 && col < this.getColumns();
		for (int row = this.getRows() - 1; row >= 0; row--) {
			if (this.getChip(row, col).equals(Chip.EMPTY)) {
				return row;
			}
		}
		this.shiftColumn(col, 1);
		return 0;
	}

	private void shiftColumn(int col, int offset) {
		/*
		 * Shifts given column by offset amount.
		 * offset can be any int
		 */
		// TODO Test this
		assert col >= 0 && col < this.getColumns();
		Queue<Chip> fifo = new LinkedList<Chip>();
		int d = (offset >= 0) ? 1 : -1;
		int i = (offset >= 0) ? 0 : this.getRows()-1;
		for (; Math.abs(i) < this.getRows(); i += d) {
			fifo.add(this.getChip(i, col));
			Chip c = (Math.abs(i) < Math.abs(offset)) ? Chip.EMPTY : fifo.poll();
			this._placeChip(i, col, c);
		}
	}

	private Chip findWinner() {
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




