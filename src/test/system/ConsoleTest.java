package test.system;

import api.View;
import api.Game;
import api.Chip;
import api.Controller;
import exc.GameStateException;
import impl.view.Console;
import impl.game.Complica;
import impl.game.TicTacToe;
import impl.game.ConnectFour;
import impl.controller.GameController;

import java.lang.IllegalArgumentException;

public class ConsoleTest {
    public static void main(String[] args) {
        Game game;

        switch (args[0]) {
        case "connectfour":
            game = new ConnectFour();
            break;
        case "complica":
            game = new Complica();
            break;
        case "tictactoe":
            game = new TicTacToe();
            break;
        default:
            throw new IllegalArgumentException(args[0]);
        }

        Controller controller = new GameController(game);
        View view = new Console(game, controller);

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
