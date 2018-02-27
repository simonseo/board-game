package impl.game;


import api.Chip;
import exc.GameIndexOutOfBoundsException;
import exc.GameStateException;

public class TicTacToe extends ConnectGame {
	
	public TicTacToe() {
		super(3, 3, 3); // rows = 3, cols = 3, connect = 3
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
		if (row < 0 || row > this.getRows()-1 || 
			col < 0 || col > this.getColumns()-1) {
			System.out.println("Error: placeChip was given non-existent row or column");
			throw new GameIndexOutOfBoundsException(row, col);
		}
		if (!this.getChip(row, col).isEmpty() ) {
			System.out.println("Error: tried to place chip on a place that is already taken");
			throw new UnsupportedOperationException();
		}

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
	
}




