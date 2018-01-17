package client.tui;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import client.GoClient;

/**
 * A TUI for the client connected to a Go server.
 * @author janine.kleinrot
 */
public class GoClientTUI implements Observer {
	
	/** The client using the TUI. */
	private GoClient goClient;
	
	/** A scanner to read input. */
	private Scanner in;

	/**
	 * Creates a new TUI for the client.
	 * Standard input is read using the initialized scanner.
	 * @param goClient
	 * 			The client using this TUI.
	 */
	public GoClientTUI(GoClient goClient) {
		this.goClient = goClient;
		goClient.addObserver(this);
		in = new Scanner(System.in);
	}
	
	/**
	 * Start the TUI.
	 */
	public void start() {
		boolean running = true;
		while (running) {
			String input = readString("Enter your command: ");
			String[] words = input.split(" ");
			if (words.length == 3 && words[0].equals("CONNECT")) {
				goClient.connect(words[1], words[2]);
			} else if (words.length == 2 && words[0].equals("REQUEST_GAME")) {
				
			} else if (words.length == 3 && words[0].equals("SETTINGS")) {
				
			} else if (words.length == 2 && words[0].equals("MOVE")) {
				
			} else if (words.length == 1 && words[0].equals("HELP")) {
				System.out.println(String.format("%-80s" + "%-30s", "Action", "Command"));
				System.out.println(String.format("%-80s" + "%-30s", 
						"Connect to Go server with this IP address and port number", 
						"CONNECT <IP address> <port number>"));
				System.out.println(String.format("%-80s" + "%-30s", 
						"Request to play a game with this amount of players", 
						"REQUEST_GAME <number of players>"));
				System.out.println(String.format("%-80s" + "%-30s", 
						"Choose settings for the requested game", 
						"SETTINGS <stone color> <board size>"));
				System.out.println(String.format("%-80s" + "%-30s", 
						"Make a move by placing a stone at a coordinate of the board or pass", 
						"MOVE <row_column> or MOVE PASS"));
			} else if (words.length == 1 && words[0].equals("EXIT")) {
				running = false;
				System.out.println("Goodbye");
			} else {
				System.out.println("Unknown command. Enter 'HELP' for a list of commands");
			}
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
		System.out.println(prompt);
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
			System.out.println("Connected to Go server");
		}
		
	}
	
}
