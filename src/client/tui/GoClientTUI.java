package client.tui;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import client.GoClientActor;

/**
 * A TUI for the client connected to a Go server.
 * @author janine.kleinrot
 */
public class GoClientTUI implements Observer, Runnable {
	
	/** The client using the TUI. */
	private GoClientActor goClientActor;
	
	/** A scanner to read input. */
	private Scanner in;
	
	private String name;
	
	private String stoneColor;
	
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
			} else if (words.length == 2 && words[0].equals("REQUEST_GAME")) {
				goClientActor.requestGame(words[1]);
			} else if (words.length == 3 && words[0].equals("SETTINGS")) {
				goClientActor.setGameSettings(words[1], words[2]);
			} else if (words.length == 2 && words[0].equals("MOVE")) {
				move = words[1];
				goClientActor.getPlayer().makeMove(words[1]);
			} else if (words.length == 1 && words[0].equals("HELP")) {
				System.out.println(String.format("%-80s" + "%-30s", "Action", "Command"));
				System.out.println(String.format("%-80s" + "%-30s", 
						"Connect to Go server with this IP address and port number", 
						"CONNECT <IP address> <port number>"));
				System.out.println(String.format("%-80s" + "%-30s", 
						"Request to play a game yourself or let a computer play a game", 
						"REQUEST_GAME <player type>"));
				System.out.println(String.format("%-80s" + "%-30s", 
						"Choose settings for the requested game", 
						"SETTINGS <stone color> <board size>"));
				System.out.println(String.format("%-80s" + "%-30s", 
						"Make a move by placing a stone at a coordinate of the board or pass", 
						"MOVE <row_column> or MOVE PASS"));
				System.out.println(name + ": Waiting for command... ");
			} else if (words.length == 1 && words[0].equals("EXIT")) {
				running = false;
				System.out.println("Goodbye");
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
			System.out.println("ERROR: Already requested games as human player");
			System.out.println(name + ": Waiting for opponent...");
		} else if (object.equals("Already requested game computer")) {
			System.out.println("ERROR: Already requested games as computer player");
			System.out.println(name + ": Waiting for opponent...");
		} else if (object.equals("Request game settings")) {
			System.out.println(name + ": Opponent found");
			System.out.println(name + ": Waiting for game settings command...");
		} else if (object.equals("Game settings set black")) {
			((Observable) goClientActor.getPlayer()).addObserver(this);
			System.out.println(name + ": Game settings set");
			stoneColor = " BLACK";
			System.out.println(name + stoneColor + ": Waiting on move...");
		} else if (object.equals("Game settings set white")) {
			((Observable) goClientActor.getPlayer()).addObserver(this);
			System.out.println(name + ": Game settings set");
			stoneColor = " WHITE";
			System.out.println(name + stoneColor + ": Waiting on opponent...");
		} else if (object.equals("Illegal stone color")) {
			System.out.println("ERROR: Not a valid stone color");
			System.out.println(name + ": Waiting for game settings command...");
		} else if (object.equals("No game settings requested")) {
			System.out.println("ERROR: No game requested to set game settings for");
			System.out.println(name + ": Waiting for command...");
		} else if (object.equals("Game settings received BLACK")) {
			((Observable) goClientActor.getPlayer()).addObserver(this);
			System.out.println(name + ": Opponent found");
			System.out.println(name + ": Game settings received");
			stoneColor = " BLACK";
		} else if (object.equals("Game settings received WHITE")) {
			((Observable) goClientActor.getPlayer()).addObserver(this);
			System.out.println(name + ": Opponent found");
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
		} else if (object.equals("Valid move")) {
			goClientActor.sendMove(move);
		} else if (object.equals("Invalid move input")) {
			System.out.println(name + stoneColor + ": Not a valid move");
			System.out.println(name + stoneColor + ": Waiting on move...");
		} else if (object.equals("Move not on board")) {
			System.out.println(name + stoneColor + ": Not a valid move "
					+ "(Intersection out of range of board)");
			System.out.println(name + stoneColor + ": Waiting on move...");
		} else if (object.equals("Occupied intersection")) {
			System.out.println(name + stoneColor + ": Not a valid move "
					+ "(Intersection already occupied with stone)");
			System.out.println(name + stoneColor + ": Waiting on move...");
		} else if (object.equals("Ko rule")) {
			System.out.println(name + stoneColor + ": Not a valid move (Ko rule violated");
			System.out.println(name + stoneColor + ": Waiting on move...");
		}
		
	}
	
}
