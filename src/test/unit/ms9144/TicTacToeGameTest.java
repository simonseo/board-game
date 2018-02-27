package test.unit.ms9144;

import api.Game;
import api.Chip;
import exc.GameStateException;
import exc.GameIndexOutOfBoundsException;
import impl.game.TicTacToe;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TicTacToeGameTest {
    private Game game;

    @Before
    public void setup() {
        game = new TicTacToe();
    }

    private void diagonalWinnerSetup() throws GameStateException {
        for (int i = game.getRows()-1; i > 0; i--) {
            for (int j = 0; j < game.getColumns(); j++) {
                game.placeChip(i, j);
            }
        }
    }

    private void createTieGame() throws GameStateException {
		int moves[][] = {
			{0,0}, {0,1}, {0,2},
			{1,0}, {1,1}, {2,0},
			{2,1}, {2,2}, {1,2}
		};
        for (int i = 0; i < moves.length; i++) {
            game.placeChip(moves[i][0], moves[i][1]);
        }
    }

    @Test
    public void testInitSetsPlayer() {
        Chip chip = game.getCurrentPlayer();
        assertFalse(chip.isEmpty());
    }

    @Test
    public void testInitBlanksBoard() {
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getColumns(); col++) {
                Chip chip = game.getChip(row, col);
                assertTrue(chip.isEmpty());
            }
        }
    }

    @Test
    public void testInitSetsRows() {
        assertEquals(game.getRows(), 3);
    }

    @Test
    public void testInitSetsColumns() {
        assertEquals(game.getColumns(), 3);
    }

    @Test
    public void testNextPlayerSetAfterPlacedDisk() throws GameStateException {
        Chip firstPlayer = game.getCurrentPlayer();
        game.placeChip(0, 0);
        Chip secondPlayer = game.getCurrentPlayer();

        assertNotEquals(firstPlayer, secondPlayer);
    }

    @Test(expected = GameIndexOutOfBoundsException.class)
    public void testDisksCannotBePlacedOutOfBounds()
        throws GameStateException {
        game.placeChip(0, game.getColumns() + 1);
    }

    @Test(expected = GameIndexOutOfBoundsException.class)
    public void testDisksCannotBePlacedNegatively() throws GameStateException {
        game.placeChip(0, -1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRepeatedPlacementBlocked() throws UnsupportedOperationException, GameStateException {
        for (int i = 0; i < game.getRows() + 1; i++) {
            game.placeChip(0, 0);
        }
    }

    @Test(expected = GameIndexOutOfBoundsException.class)
    public void testNegativeRowNotAllowed() throws GameStateException {
        game.placeChip(-1, 0);
    }

    @Test
    public void testOutOfBoundsColsNotAllowed() throws GameStateException {
        int trials = game.getColumns() + 1;
        int exceptions = 0;

        for (int i = 0; i < trials * 2; i++) {
            try {
                game.placeChip(0, i);
            }
            catch (GameIndexOutOfBoundsException err) {
                exceptions++;
            }
        }
        assertEquals(5, exceptions);
    }

    @Test
    public void testVerticalWinnerFound() throws GameStateException {
        for (int i = 0; i < 5; i++) {
            game.placeChip(i/2, i % 2);
        }

        assertTrue(game.isGameOver());
    }

    @Test
    public void testWinningPlayerIsSet() throws GameStateException {
        Chip player = null;

        for (int i = 0; i < 5; i++) {
            player = game.getCurrentPlayer();
            game.placeChip(i/2, i % 2);
        }

        assertEquals(game.getWinningPlayer(), player);
    }

    @Test
    public void testCurrentPlayerAfterWinner() throws GameStateException {
        for (int i = 0; i < 5; i++) {
            game.placeChip(i/2, i % 2);
        }

        assertTrue(game.getCurrentPlayer().isEmpty());
    }

    @Test
    public void testHorizontalWinnerFound() throws GameStateException {
        for (int i = 0; i < 5; i++) {
            game.placeChip(i%2, i/2);
        }
        assertTrue(game.isGameOver());
    }

    @Test
    public void testOutofOrderHorizontalWinnerFound()
        throws GameStateException {
        for (int i = 0; i < 3; i++) {
            if (i != 1) {
                for (int j = 0; j < 2; j++) {
                    game.placeChip(j, i);
                }
            }
        }
        game.placeChip(0, 1);

        assertTrue(game.isGameOver());
    }

    @Test
    public void testLeftRightDiagonalWinnerFound() throws GameStateException {
        diagonalWinnerSetup();
        game.placeChip(0, 0);
        assertTrue(game.isGameOver());
    }

    @Test
    public void testRightLeftDiagonalWinnerFound() throws GameStateException {
        diagonalWinnerSetup();
        game.placeChip(0, game.getColumns() - 1);
        assertTrue(game.isGameOver());
    }

    @Test
    public void testTieGameIdentified() throws GameStateException {
        createTieGame();
        assertTrue(game.isGameOver());
    }

    @Test(expected = GameStateException.class)
    public void testTieHasNoWinner() throws GameStateException {
        createTieGame();
        game.getWinningPlayer();
    }

    @Test
    public void testTieHasNoCurrentPlayer() throws GameStateException {
        createTieGame();
        assertTrue(game.getCurrentPlayer().isEmpty());
    }

}
