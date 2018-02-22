package impl.game;

import api.Chip;
import api.Game;
import exc.GameIndexOutOfBoundsException;
import exc.GameStateException;

public class ConnectFour extends Game {
	private int rows, columns;
	private Chip[][] board;
	private Chip currentPlayer;
	private Chip winner;
	
	
	public ConnectFour() {
		this.rows = 5;
		this.columns = 6;
		this.currentPlayer = Chip.BLUE;
		this.winner = null;
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
		 * 2. place
		 * 3. check win
		 * 4. 
		 * 
		 */
		assert !this.isGameOver();
		
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
		if (row < 0 || row > this.getRows()-1 ||
				col < 0 || col > this.getRows()-1) {
			throw new GameIndexOutOfBoundsException(row, col);
		}
		// TODO Find placeable row 
		board[row][col] = this.getCurrentPlayer();
		this.switchPlayer();
	}
	


	private void switchPlayer() {
		assert this.currentPlayer != null;
		this.currentPlayer = (this.isGameOver()) ? Chip.EMPTY :
			(this.currentPlayer.equals(Chip.BLUE)) ? Chip.RED : Chip.BLUE;
	}

	@Override
	public Chip getWinningPlayer() throws GameStateException {
		if (this.isGameOver() || this.winner == null || this.winner.equals(Chip.EMPTY)) {
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
		// TODO figure out game flow
		// The game is over if it is a tie or a winner has been determined.
		return false;
	}
	
}