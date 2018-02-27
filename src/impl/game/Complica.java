package impl.game;

import java.util.LinkedList;
import java.util.Queue;

import api.Chip;
import exc.GameIndexOutOfBoundsException;
import exc.GameStateException;

public class Complica extends ConnectGame {
	
	public Complica() {
		super(7, 4, 4); // rows = 7, cols = 4, connect = 4
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
		
		row = this.getPlaceableRow(col);
		Chip p = this.getCurrentPlayer();
		this._placeChip(row, col, p);
		this.winner = this.findWinner(); //could be no one
		if (!this.winner.isEmpty()) {
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
		 * If doesn't exist, shift everything by one and return 0 
		 * assumes that col is within bounds
		 */
		assert col >= 0 && col < this.getColumns();
		for (int row = this.getRows() - 1; row >= 0; row--) {
			if (this.getChip(row, col).isEmpty()) {
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
	
}




