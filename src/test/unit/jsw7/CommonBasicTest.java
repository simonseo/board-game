package test.unit.jsw7;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertTrue;

import api.Game;
import api.Chip;
import impl.game.Complica;
import impl.game.TicTacToe;
import impl.game.ConnectFour;

/**
 * An example of how to use JUnit Parameters to test common elements
 * between games. Characteristics that are unique -- winning
 * conditions, for example -- will probably require specialised tests;
 * however, a lot of testing mileage can be gained, with minimal
 * coding effort, by identifying such "common" requirements.
 **/

@RunWith(Parameterized.class)
public class CommonBasicTest {
    private Game game;

    public CommonBasicTest(Game game) {
        this.game = game;
    }

    @Parameters
    public static Iterable<Game> parameters() {
        return Arrays.asList(new ConnectFour(),
                             new Complica(),
                             new TicTacToe());
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
}
