package test.system;

import api.View;
import api.Game;
import api.Chip;
import exc.GameStateException;
import impl.view.Console;
import impl.game.ConnectFour;

public class ConnectFourConsoleTest {
    public static void main(String[] args) {
        Game game = new ConnectFour();
        View view = new Console(game);

        view.update(game);
        try {
            Chip winner = game.getWinningPlayer();
            System.out.println(winner + " wins!");
        }
        catch (GameStateException e) {
            System.out.println("It was a tie!");
        }
    }
}
