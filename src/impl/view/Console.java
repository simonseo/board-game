package impl.view;

import java.util.Observable;
import java.util.Scanner;

import api.Game;
import api.View;
import impl.game.ConnectFour;

public class Console extends View {
	
	private Game game;

	public Console(Game game) {
		this.game = game;
		this.game.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		/*
		 * Executed when observable calls notifyObservers(response)
		 * The user should interact with the game through the view via the command line. 
		 * The view observes the game, updating what it shows to the user based its observations (notifications).
		 * The view maintains an aggregate relationship with the game.
		 * The view must accurately update the visualization of the board and give players turns. 
		 * The system test takes care of announcing the end result of the game; 
		 * which should run as expected, annoucing ties or wins. 
		 * The view and the model should interact entirely via the Java Observer infrastructure. 
		 * Specifically, this means no loops should be involved.
		 */
		
//		scan.nextLine();
		this.render(this.game);
		((ConnectFour) this.game).run();
	}

	@Override
	public void render(Game game) {
		// Draw the board on the screen in a manner that resembles a standing board. 
		// It should be clear which player occupies a given cell.
		/*
		 * print stuff
		 * +-+-+-+
		 * |O| |X|
		 * +-+-+-+
		 * |X|O|O|
		 * +-+-+-+
		 */
	}
	
}