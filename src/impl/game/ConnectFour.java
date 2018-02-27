package impl.game;

import api.Chip;
import exc.GameIndexOutOfBoundsException;
import exc.GameStateException;

public class ConnectFour extends ConnectGame {
	
	public ConnectFour() {
		super(6, 7, 4); // rows = 6, cols = 7, connect = 4
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
		this.winner = this.findWinner(); //could be no one
		if (!this.winner.isEmpty() || this.boardIsFull()) {
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
			if (this.getChip(row, col).isEmpty()) {
				return row;
			}
		}
		return -1;
	}
}




