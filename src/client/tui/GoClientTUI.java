package client.tui;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import client.GoClientActor;
import game.player.ComputerPlayer;
import game.player.HumanPlayer;

/**
 * A TUI for the client connected to a Go server.
 * @author janine.kleinrot
 */
public class GoClientTUI implements Observer, Runnable {
	
	/** The client using the TUI. */
	private GoClientActor goClientActor;
	
	/** A scanner to read input. */
	private Scanner in;
	
	/** The name of the GoClient. */
	private String name;
	
	/** The stone color of the GoClient player. */
	private String stoneColor;
	
	/** The move made by the player. */
	private String move;

	/**
	 * Creates a new TUI for the client.
	 * Standard input is read using the initialized scanner.
	 * @param goClient
	 * 			The client using this TUI.
	 */
	public GoClientTUI(GoClientActor goClientActor) {
		this.goClientActor = goClientActor;
		((Observable) goClientActor).addObserver(this);
		in = new Scanner(System.in);
		name = goClientActor.getName().toUpperCase();
	}
	
	/**
	 * Runs continuously while the thread is running.
	 */
	public void run() {
		start();
	}
	
	/**
	 * Start the TUI.
	 */
	public void start() {
		boolean running = true;
		String input = readString(name + ": Waiting for command...\n");
		while (running) {
			String[] words = input.split(" ");
			if (words.length == 3 && words[0].equals("CONNECT")) {
				goClientActor.connect(words[1], words[2]);
			} else if (words.length == 2 && words[0].equals("NAME")) {
				goClientActor.changeName(words[1]);
			} else if (words.length == 2 && words[0].equals("REQUEST_GAME")) {
				goClientActor.requestGame(words[1]);
			} else if (words.length == 2 && words[0].equals("MOVE_TIME")) {
				if (goClientActor.getPlayer() instanceof ComputerPlayer) {
					goClientActor.getPlayer().setMoveTime(words[1]);
				} else if (goClientActor.getPlayer() instanceof HumanPlayer) {
					System.out.println("ERROR: Move time only avaible for computer player");
					System.out.println(name + ": Waiting on command...");
				} else {
					System.out.println("ERROR: Computer player needed to set move time");
					System.out.println(name + ": Waiting on command...");
				}
			} else if (words.length == 3 && words[0].equals("SETTINGS")) {
				goClientActor.setGameSettings(words[1], words[2]);
			} else if (words.length == 2 && words[0].equals("MOVE")) {
				move = words[1];
				goClientActor.getPlayer().makeMove(words[1]);
			} else if (words.length == 1 && words[0].equals("HINT")) {
				goClientActor.getPlayer().provideHint();
				goClientActor.getPlayer().determineMove();
			} else if (words.length == 1 && words[0].equals("HELP")) {
				System.out.println(String.format("%-120s" + "%-30s", "Action", "Command"));
				System.out.println(String.format("%-120s" + "%-30s", 
						"Connect to Go server with this IP address and port number", 
						"CONNECT <IP address> <port number>"));
				System.out.println(String.format("%-120s" + "%-30s", 
						"Request to play a game yourself or let a computer play a game", 
						"REQUEST_GAME <player type>"));
				System.out.println(String.format("%-120s" + "%-30s", 
						"Adjust the maximum move time of the computer player", 
						"MOVE_TIME <move time in seconds>"));
				System.out.println(String.format("%-120s" + "%-30s", 
						"Choose settings for the requested game", 
						"SETTINGS <stone color> <board size>"));
				System.out.println(String.format("%-120s" + "%-30s", 
						"Make a move by placing a stone at a coordinate of the board or pass "
						+ "(0, 0 is top left intersection)", 
						"MOVE <row_column> or MOVE PASS"));
				System.out.println(String.format("%-120s" + "%-30s", 
						"Ask for a hint", "HINT"));
				System.out.println(String.format("%-120s" + "%-30s", "Set a new name if the name "
						+ "is already taken at the Go server", "NAME <Name>"));
				System.out.println(String.format("%-120s" + "%-30s", 
						"Quit a current game", "QUIT"));
				System.out.println(String.format("%-120s" + "%-30s", 
						"Exit from the Go server", "EXIT"));
				System.out.println(name + ": Waiting for command... ");
			} else if (words.length == 1 && words[0].equals("QUIT")) {
				goClientActor.quitGame();
			} else if (words.length == 1 && words[0].equals("EXIT")) {
				System.out.println("Goodbye");
				goClientActor.exit();
				running = false;
				Thread.interrupted();
			} else {
				System.out.println("Unknown command. Enter 'HELP' for a list of commands");
			}
			input = readString("");
		}
	}
	
	/**
	 * Reads standard input after sending prompt to standard output.
	 * @param prompt
	 * 			The message displayed at standard output.
	 * @return
	 * 			The line read from standard input.
	 */
	private String readString(String prompt) {
		String result = null;
		System.out.print(prompt);
		if (in.hasNextLine()) {
			result = in.nextLine();
		}
		return result;
	}
	
	/**
	 * Prints a messages to the standard output to show the changes made.
	 */
	public void update(Observable observable, Object object) {
		if (object.equals("Connected")) {
			System.out.println(name + ": Connected to Go server");
			System.out.println(name + ": Waiting for command...");
		} else if (object.equals("Name error")) {
			System.out.println(name + ": Name is already taken");
			System.out.println(name + ": Waiting on new name...");
		} else if (object.equals("Name changed")) {
			name = goClientActor.getName().toUpperCase();
			System.out.println(name + ": Name is changed");
			System.out.println(name + ": Waiting on command...");
		} else if (object.equals("Already connected")) {
			System.out.println("ERROR: Already connected to Go server");
			System.out.println(name + ": Waiting for command...");
		} else if (object.equals("Game requested human")) {
			System.out.println(name + ": Game requested as human player");
			System.out.println(name + ": Waiting for opponent...");
		} else if (object.equals("Game requested computer")) {
			System.out.println(name + ": Game requested as computer player");
			System.out.println(name + ": Waiting for opponent...");
		} else if (object.equals("Already requested game human")) {
			System.out.println("ERROR: Already requested game as human player");
			System.out.println(name + ": Waiting for opponent...");
		} else if (object.equals("Already requested game computer")) {
			System.out.println("ERROR: Already requested game as computer player");
			System.out.println(name + ": Waiting for opponent...");
		} else if (object.equals("Request game settings")) {
			System.out.println(name + ": Waiting for game settings command...");
		} else if (object.equals("Illegal stone color")) {
			System.out.println("ERROR: Not a valid stone color");
			System.out.println(name + ": Waiting for game settings command...");
		} else if (object.equals("No game settings requested")) {
			System.out.println("ERROR: No game requested to set game settings for");
			System.out.println(name + ": Waiting for command...");
		} else if (((String) object).contains("Opponent")) {
			System.out.println(object);
		} else if (object.equals("Game settings received BLACK")) {
			((Observable) goClientActor.getPlayer()).addObserver(this);
			stoneColor = " BLACK";
		} else if (object.equals("Game settings received WHITE")) {
			((Observable) goClientActor.getPlayer()).addObserver(this);
			System.out.println(name + ": Game settings received");
			stoneColor = " WHITE";
			System.out.println(name + stoneColor + ": Waiting on opponent...");
		} else if (object.equals("Not connected yet")) {
			System.out.println("ERROR: Not connected to Go server yet");
			System.out.println(name + ": Waiting for command...");
		} else if (object.equals("No opponent found")) {
			System.out.println("ERROR: No game settings requested yet");
			System.out.println(name + ": Waiting for opponent...");
		} else if (object.equals("Invalid player type")) {
			System.out.println("ERROR: Not a valid player type");
			System.out.println(name + ": Waiting for command...");
		} else if (object.equals("Illegal board size")) {
			System.out.println("ERROR: Not a valid board size");
			System.out.println(name + ": Waiting for game settings command...");
		} else if (object.equals("Move requested")) {
			System.out.println(name + stoneColor + ": Waiting on move...");
		} else if (((String) object).contains("Valid move")) {
			String[] words = ((String) object).split(" ");
			goClientActor.sendMove(words[2]);
		} else if (object.equals("Invalid move input")) {
			System.out.println("ERROR: Not a valid move");
			System.out.println(name + stoneColor + ": Waiting on move...");
		} else if (object.equals("Move not on board")) {
			System.out.println("ERROR: Not a valid move "
					+ "(Intersection out of range of board)");
			System.out.println(name + stoneColor + ": Waiting on move...");
		} else if (object.equals("Occupied intersection")) {
			System.out.println("ERROR: Not a valid move "
					+ "(Intersection already occupied with stone)");
			System.out.println(name + stoneColor + ": Waiting on move...");
		} else if (object.equals("Ko rule")) {
			System.out.println("ERROR: Not a valid move (Ko rule violated");
			System.out.println(name + stoneColor + ": Waiting on move...");
		} else if (object.equals("The game is finished")) {
			System.out.println(name + stoneColor + ": The game is finised");
		} else if (((String) object).contains("won")) {
			System.out.println(name + ": " + object);
		} else if (object.equals("Invalid move server")) {
			System.out.println("ERROR: Not a valid move (Checked by Go server)");
			System.out.println(name + stoneColor + ": Waiting on move...");
		} else if (((String) object).contains("draw")) {
			System.out.println(name + ": " + object);
		} else if (object.equals("Move made")) {
			System.out.println(name + stoneColor + ": The move is made");
			System.out.println(name + stoneColor + ": Waiting on opponent...");
		} else if (object.equals("Other move made")) {
			System.out.println(name + stoneColor + ": Opponent made a move");
		} else if (object.equals("Unknown command")) {
			System.out.println(name + ": Unknown command given to Go server");
			System.out.println(name + ": Waiting on command...");
		} else if (object.equals("Invalid move time")) {
			System.out.println("ERROR: Invalid move time argument");
			System.out.println(name + ": Waiting on command...");
		} else if (((String) object).contains("The game is aborted")) {
			System.out.println("ERROR: " + object);
		} else if (((String) object).contains("aborted the game")) {
			System.out.println("ERROR: " + object);
		} else if (((String) object).contains("in time")) {
			System.out.println("ERROR: " + object);
		} 
		
	}
	
}
