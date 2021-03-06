package impl.controller;

import java.util.InputMismatchException;
import java.util.Scanner;

import api.Game;
import exc.GameIndexOutOfBoundsException;
import exc.GameStateException;
import impl.game.Complica;
import impl.game.ConnectFour;
import impl.game.TicTacToe;

public class Controller {
	private Scanner scan;
	private Game game;
	private final boolean requireRow;
	private final boolean requireColumn;
	
	public Controller(Game game) {
		this.scan = new Scanner(System.in);
		this.game = game;
		this.requireRow = game instanceof TicTacToe;
		this.requireColumn = game instanceof TicTacToe || game instanceof ConnectFour || game instanceof Complica;
	}
	
	public void round() {
		// receive user input of their desire column and place chip in board
		boolean pass = false;
		while (!pass) {
			try {
				int row = 0, col = 0;
				if (this.requireRow) {
					System.out.println("Choose a row: ");
					row = scan.nextInt();
				}
				if (this.requireColumn) {
					System.out.println("Choose a column: ");
					col = scan.nextInt();
				}
				game.placeChip(row, col);
				pass = true;
			} catch (InputMismatchException e) {
				System.out.println("Error: Wrong type of input");
				this.scan.skip("[^0-9]");
				e.printStackTrace();
			} catch (GameIndexOutOfBoundsException e) {
				System.out.println("Error: That place doesn't exist. Try another one");
			} catch (GameStateException e) {
				System.out.println("Error: Check that the game is running");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}
}
