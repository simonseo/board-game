package test.unit.jsw7;

import api.Game;
import impl.game.ConnectFour;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ConnectFourBasicTest {
    @Test
    public void testDefaultConstructor() {
        Game game = new ConnectFour();
        assertNotNull(game);
    }
}
