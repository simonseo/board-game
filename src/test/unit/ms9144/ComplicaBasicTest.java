package test.unit.ms9144;

import api.Game;
import impl.game.Complica;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ComplicaBasicTest {
    @Test
    public void testDefaultConstructor() {
        Game game = new Complica();
        assertNotNull(game);
    }
}
