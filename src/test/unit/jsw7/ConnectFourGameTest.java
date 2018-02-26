package test.unit.jsw7;

import api.Game;
import api.Chip;
import exc.GameStateException;
import exc.GameIndexOutOfBoundsException;
import impl.game.ConnectFour;

// import api.View;
// import impl.ConsoleView;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ConnectFourGameTest {
    private Game game;

    @Before
    public void setup() {
        game = new ConnectFour();
    }

    private void diagonalWinnerSetup() throws GameStateException {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                game.placeChip(0, j);
            }
        }
    }

    private void createTieGame() throws GameStateException {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                game.placeChip(0, j);
            }
        }

        for (int j = 1; j < game.getColumns(); j++) {
            game.placeChip(0, j);
        }

        for (int j = 1; j >= 0; j--) {
            int column = j * (game.getColumns() - 1);
            game.placeChip(0, column);
        }

        for (int j = 0; j < game.getColumns() - 1; j++) {
            game.placeChip(0, j);
        }

        for (int j = 0; j < game.getColumns(); j++) {
            game.placeChip(0, j);
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
        assertEquals(game.getRows(), 6);
    }

    @Test
    public void testInitSetsColumns() {
        assertEquals(game.getColumns(), 7);
    }

    @Test
    public void testNextPlayerSetAfterPlacedDisk()
        throws GameStateException,
               GameIndexOutOfBoundsException {
        Chip firstPlayer = game.getCurrentPlayer();
        game.placeChip(0, 0);
        Chip secondPlayer = game.getCurrentPlayer();

        assertNotEquals(firstPlayer, secondPlayer);
    }

    @Test(expected = GameIndexOutOfBoundsException.class)
    public void testDisksCannotBePlacedOutOfBounds()
        throws GameStateException,
               GameIndexOutOfBoundsException {
        game.placeChip(0, game.getColumns() + 1);
    }

    @Test(expected = GameIndexOutOfBoundsException.class)
    public void testDisksCannotBePlacedNegatively()
        throws GameStateException,
               GameIndexOutOfBoundsException {
        game.placeChip(0, -1);
    }

    @Test(expected = GameIndexOutOfBoundsException.class)
    public void testColumnPlacementBounded() throws GameStateException {
        for (int i = 0; i < game.getRows() + 1; i++) {
            game.placeChip(0, 0);
        }
    }

    @Test(expected = GameIndexOutOfBoundsException.class)
    public void testNegativeRowNotAllowed() throws GameStateException {
        game.placeChip(-1, 0);
    }

    @Test
    public void testPositiveRowsNotAllowed() throws GameStateException {
        int trials = game.getRows() + 1;
        int exceptions = 0;

        for (int i = 0; i < trials; i++) {
            try {
                game.placeChip(i + 1, 0);
            }
            catch (GameIndexOutOfBoundsException err) {
                exceptions++;
            }
        }

        assertEquals(trials, exceptions);
    }

    @Test
    public void testVerticalWinnerFound() throws GameStateException {
        for (int i = 0; i < 7; i++) {
            game.placeChip(0, i % 2);
        }

        assertTrue(game.isGameOver());
    }

    @Test
    public void testWinningPlayerIsSet() throws GameStateException {
        Chip player = null;

        for (int i = 0; i < 7; i++) {
            player = game.getCurrentPlayer();
            game.placeChip(0, i % 2);
        }

        assertEquals(game.getWinningPlayer(), player);
    }

    @Test
    public void testCurrentPlayerAfterWinner() throws GameStateException {
        for (int i = 0; i < 7; i++) {
            game.placeChip(0, i % 2);
        }

        assertTrue(game.getCurrentPlayer().isEmpty());
    }

    @Test
    public void testHorizontalWinnerFound() throws GameStateException {
        for (int i = 0; i < 4; i++) {
            int stop = (i < 3) ? 2 : 1;
            for (int j = 0; j < stop; j++) {
                game.placeChip(0, i);
            }
        }

        assertTrue(game.isGameOver());
    }

    @Test
    public void testOutofOrderHorizontalWinnerFound()
        throws GameStateException {
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                for (int j = 0; j < 2; j++) {
                    game.placeChip(0, i);
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

    @Test
    public void chipFallsToBottom() throws GameStateException {
        Game game = new ConnectFour();
        int column = 0;

        game.placeChip(0, column);
        Chip chip = game.getChip(game.getRows() - 1, column);

        assertFalse(chip.isEmpty());
    }
}
