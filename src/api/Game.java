package api;

import exc.GameStateException;

import java.util.Observable;

/**
 * All Game implementations should expose a default constructor that
 * sets the board size according the respective game rules.
 **/
public abstract class Game extends Observable {
    /**
     * Get the number of rows.
     **/
    public abstract int getRows();

    /**
     * Get the number of columns.
     **/
    public abstract int getColumns();

    /**
     * Get the chip at position (row,col).
     *
     * @throws GameIndexOutOfBoundsException if placement request is
     *         outside of the bounds of the game board.
     **/
    public abstract Chip getChip(int row, int column);

    /**
     * @throws GameStateException if the game is over.
     * @throws GameIndexOutOfBoundsException if the requested row or
     *         column is out-of-bounds according the rules/semantics
     *         of the concrete implementation of the game.
     **/
    public abstract void placeChip(int row, int col) throws GameStateException;

    /**
     * @throws GameStateException if no winner has been established.
     **/
    public abstract Chip getWinningPlayer() throws GameStateException;

    /**
     * Get the current player (Chip). If the game is over, the current
     * player should be "empty".
     **/

    public abstract Chip getCurrentPlayer();

    /**
     * Whether the game is over.
     **/
    public abstract boolean isGameOver();
}
