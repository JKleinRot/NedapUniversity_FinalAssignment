package client.tui;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import client.GoClientActor;
import protocol.Protocol.Server;
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
		String input = readString(name + ": Waiting for command... ");
		while (running) {
			String[] words = input.split(" ");
			if (words.length == 3 && words[0].equals("CONNECT")) {
				goClientActor.connect(words[1], words[2]);
			} else if (words.length == 1 && words[0].equals("REQUEST_GAME")) {
				goClientActor.requestGame();
			} else if (words.length == 3 && words[0].equals("SETTINGS")) {
				
			} else if (words.length == 2 && words[0].equals("MOVE")) {
				
			} else if (words.length == 1 && words[0].equals("HELP")) {
				System.out.println(String.format("%-80s" + "%-30s", "Action", "Command"));
				System.out.println(String.format("%-80s" + "%-30s", 
						"Connect to Go server with this IP address and port number", 
						"CONNECT <IP address> <port number>"));
				System.out.println(String.format("%-80s" + "%-30s", 
						"Request to play a game", 
						"REQUEST_GAME"));
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
			System.out.println(name + ": Connected to Go server");
			System.out.println(name + ": Waiting for command... ");
		} else if (object.equals("Already connected")) {
			System.out.println(name + ": Already connected to Go server");
			System.out.println(name + ": Waiting for command... ");
		} else if (object.equals("Game requested")) {
			System.out.println(name + ": Game requested");
			System.out.println(name + ": Waiting for opponent...");
		}
		
	}
	
}
