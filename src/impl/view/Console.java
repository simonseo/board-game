package impl.view;

import java.util.Observable;

import api.Chip;
import api.Game;
import api.View;
import impl.game.ConnectFour;

public class Console extends View {
	private Game game;
	private enum marker {
		EMPTY {
			public String toString() {
				return " ";
			}
		},
		RED {
			public String toString() {
				return "O";
			}
		},
		BLUE {
			public String toString() {
				return "X";
			}
		},
		CORNER {
			public String toString() {
				return "+";
			}
		},
		HORIZONTAL {
			public String toString() {
				return "-";
			}
		},
		VERTICAL {
			public String toString() {
				return "|";
			}
		};
		public static marker getMarker(Chip p) {
			return marker.valueOf(p.name());
		}
	}

	public Console(Game game) {
		this.game = game;
		this.game.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		/*
		 * Executed every time observable calls notifyObservers(response)
		 * The user should interact with the game through the view via the command line. 
		 * The view observes the game, updating what it shows to the user based its observations (notifications).
		 * The view maintains an aggregate relationship with the game.
		 * The view must accurately update the visualization of the board and give players turns. 
		 * The system test takes care of announcing the end result of the game; 
		 * which should run as expected, annoucing ties or wins. 
		 * The view and the model should interact entirely via the Java Observer infrastructure. 
		 * Specifically, this means no loops should be involved.
		 */
		this.render(this.game);
		
		if (arg == null) {
			// Assumably start of game
			((ConnectFour) this.game).start();
		} else if (arg.toString().equals("Next Round")) {
			// Assumably middle of game
			System.out.println(arg.toString());
		} else if (arg.toString().equals("Game Over")) {
			// Assumably end of game
			System.out.println(arg.toString());
		} else {
			System.out.println("Error: Unexpected arg in Console.update(): " + arg.toString());
		}
	}

	@Override
	public void render(Game game) {
		// Draw the board on the screen in a manner that resembles a standing board. 
		// It should be clear which player occupies a given cell.
		/*
		 * print the board
		 *  0 1 2
		 * +-+-+-+
		 * |O| |X|
		 * +-+-+-+
		 * |X|O|O|
		 * +-+-+-+
		 */
		int r = game.getRows();
		int c = game.getColumns();
		for (int j = 0; j < c; j++) {
			System.out.print(" "+j);
		}
		System.out.println();
		for (int i = 0; i < r; i++) {
			this.printHorizontalRule(c);
			for (int j = 0; j < c; j++) {
				System.out.print(marker.VERTICAL);
				System.out.print(marker.getMarker(game.getChip(i, j)));
			}
			System.out.println(marker.VERTICAL);
		}
		this.printHorizontalRule(c);
	}
	private void printHorizontalRule(int c) {
		for (int i = 0; i < c; i++) {
			System.out.print(marker.CORNER);
			System.out.print(marker.HORIZONTAL);
		}
		System.out.println(marker.CORNER);	
	}
}


