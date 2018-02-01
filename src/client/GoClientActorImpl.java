package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import game.board.gui.GoGUIIntegrator;
import game.board.stone.StoneColor;
import game.player.ComputerPlayer;
import game.player.HumanPlayer;
import game.player.Player;
import protocol.Protocol.Client;
import protocol.Protocol.General;
import protocol.Protocol.Server;

/**
 * Handle the actions required after input received from the GoClientHandler and GoClientTUI.
 * @author janine.kleinrot
 */
public class GoClientActorImpl extends Observable implements GoClientActor {
	
	/** The GoClient. */
	private GoClient goClient;
	
	/** The player type. */
	private String playerType;
	
	/** The board size. */
	private String boardSize;
	
	/** The stone color represented in a string. */
	private String stoneColorString;
	
	/** Whether game settings are required yet. */
	private boolean areGameSettingsRequested;
	
	/** The player. */
	private Player player;
	
	/** The name of the opponent. */
	private String opponentName;
	
	/** The number of games played. */
	private int gamesPlayed;
	
	/** The GoGUI. */
	private GoGUIIntegrator goGUI;
	
	/** Whether the GoClient is connected. */
	private boolean isConnected;
	
	/**
	 * Create a new Go client actor.
	 */
	public GoClientActorImpl(GoClient goClient) {
		this.goClient = goClient;
		areGameSettingsRequested = false;
		playerType = "";
		boardSize = "";
		stoneColorString = "";
		gamesPlayed = 0;
		isConnected = false;
	}
	
	@Override
	public void connect(String ipAddress, String port) {
		try {
			if (goClient.getSocket() == null) {
				goClient.setSocket(new Socket(InetAddress.getByName(ipAddress), 
						Integer.parseInt(port)));
				goClient.setReader(new BufferedReader(new InputStreamReader(
						goClient.getSocket().getInputStream())));
				goClient.setWriter(new BufferedWriter(new OutputStreamWriter(
						goClient.getSocket().getOutputStream())));
				goClient.sendMessage(Client.NAME + General.DELIMITER1 + goClient.getName() + 
						General.DELIMITER1 + Client.VERSION + General.DELIMITER1 +  
						Client.VERSIONNO + General.DELIMITER1 + Client.EXTENSIONS + 
						General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
						General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + General.DELIMITER1 + 0 + 
						General.DELIMITER1 + 0 + General.COMMAND_END);
				goClient.setIsConnected();
				isConnected = true;
			} else {
				alreadyConnected();
			}
		} catch (ConnectException e) {
			System.out.println("ERROR: No Go server at this combination of "
					+ "IP address and port number");
		} catch (NumberFormatException e) {
			System.out.println("ERROR: Not a valid port number");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("ERROR: Not a valid host");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ERROR: Could not connect to Go server");
			e.printStackTrace();
		}
	}
	
	@Override
	public String getName() {
		return goClient.getName();
	}

	@Override
	public void changeName(String name) {
		goClient.setSocket(null);
		goClient.changeName(name);
		setChanged();
		notifyObservers("Name changed");
	}
	
	@Override
	public void showConnectionConfirmed(String[] words) {
		setChanged();
		notifyObservers("Connected");
	}
	
	@Override
	public void handleNameError() {
		setChanged();
		notifyObservers("Name error");
	}
	
	@Override
	public void alreadyConnected() {
		setChanged();
		notifyObservers("Already connected");
	}
	
	@Override
	public void requestGame(String goPlayerType) {
		if (playerType.isEmpty() && goClient.getSocket() != null && 
				(goPlayerType.equals("human") || goPlayerType.equals("computer"))) {
			this.playerType = goPlayerType;
			goClient.sendMessage(Client.REQUESTGAME + General.DELIMITER1 + 2 +
					General.DELIMITER1 + Client.RANDOM + General.COMMAND_END);
			setChanged();
			notifyObservers("Game requested " + playerType);
		} else if (playerType.isEmpty() && goClient.getSocket() != null && 
				(!goPlayerType.equals("human") || !goPlayerType.equals("computer"))) {
			setChanged();
			notifyObservers("Invalid player type");
		} else if (goClient.getSocket() == null) {
			setChanged();
			notifyObservers("Not connected yet");
		} else {
			setChanged();
			notifyObservers("Already requested game " + playerType);
		}
	}
	
	@Override
	public void getGameSettings() {
		areGameSettingsRequested = true;
		setChanged();
		notifyObservers("Request game settings");
	}

	@Override
	public void setGameSettings(String goStoneColor, String goBoardSize) {
		if (areGameSettingsRequested) {
			this.stoneColorString = goStoneColor;
			this.boardSize = goBoardSize;
			if (stoneColorString.equals("white")) {
				try {
					Integer.parseInt(boardSize);
					goClient.sendMessage(Client.SETTINGS + General.DELIMITER1 + General.WHITE + 
							General.DELIMITER1 + boardSize + General.COMMAND_END);
					setChanged();
					notifyObservers("Game settings set white");
				} catch (NumberFormatException e) {
					setChanged();
					notifyObservers("Illegal board size");
				}
			} else if (stoneColorString.equals("black")) {
				try {
					Integer.parseInt(boardSize);
					goClient.sendMessage(Client.SETTINGS + General.DELIMITER1 + General.BLACK + 
							General.DELIMITER1 + boardSize + General.COMMAND_END);
					setChanged();
					notifyObservers("Game settings set black");
				} catch (NumberFormatException e) {
					setChanged();
					notifyObservers("Illegal board size");
				}
			} else {
				setChanged();
				notifyObservers("Illegal stone color");
			}
		} else if (!playerType.isEmpty() && !areGameSettingsRequested) {
			setChanged();
			notifyObservers("No opponent found");
		} else {
			setChanged();
			notifyObservers("No game settings requested");
		}
	}

	@Override
	public void setReceivedGameSettings(String aStoneColor, String aBoardSize, String playerName, 
			String otherPlayerName) {
		this.stoneColorString = aStoneColor;
		this.boardSize = aBoardSize;
		if (stoneColorString.equals(General.WHITE)) {
			if (playerType.equals("human")) {
				player = new HumanPlayer(goClient.getName(), StoneColor.WHITE);
			} else {
				player = new ComputerPlayer(goClient.getName(), StoneColor.WHITE);
			}
			if (gamesPlayed == 0) {
				player.setBoard(boardSize);
				player.setGoGUI(boardSize);
				goGUI = player.getBoard().getGoGUI();
			} else {
				player.adjustBoard(boardSize, goGUI);
			}
		} else {
			if (playerType.equals("human")) {
				player = new HumanPlayer(goClient.getName(), StoneColor.BLACK);
			} else {
				player = new ComputerPlayer(goClient.getName(), StoneColor.BLACK);
			}
			if (gamesPlayed == 0) {
				player.setBoard(boardSize);
				player.setGoGUI(boardSize);
				goGUI = player.getBoard().getGoGUI();
			} else {
				player.adjustBoard(boardSize, goGUI);
			}
		}
		if (playerName.equals(goClient.getName())) {
			opponentName = otherPlayerName;
		} else {
			opponentName = playerName;
		}
		setChanged();
		notifyObservers("Opponent " + opponentName + " is found");
		setChanged();
		notifyObservers("Game settings received " + stoneColorString);
	}
	
	@Override
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public void sendMove(String move) {
		goClient.sendMessage(Client.MOVE + General.DELIMITER1 + move + General.COMMAND_END);
	}

	@Override
	public void handleInvalidMove() {
		setChanged();
		notifyObservers("Invalid move server");
	}
	
	@Override
	public void handleEndOfGame(String reason, String winningPlayer, String winningScore, 
			String losingPlayer, String losingScore) {
		if (reason.equals(Server.FINISHED)) {
			setChanged();
			notifyObservers("The game is finished");
			if (!winningScore.equals(losingScore)) {
				setChanged();
				notifyObservers(winningPlayer.toUpperCase() + " has won with " + winningScore + " points and " + 
						losingPlayer.toUpperCase() + " has gained " + losingScore + " points");
			} else {
				setChanged();
				notifyObservers("The game ended in a draw with " + winningScore + 
						" points each for " + winningPlayer.toUpperCase() + " and " + losingPlayer.toUpperCase());
			}
		} else if (reason.equals(Server.ABORTED)) {
			setChanged();
			notifyObservers("The game is aborted by " + losingPlayer);
			setChanged();
			notifyObservers(winningPlayer.toUpperCase() + " has won with " + winningScore + " points and " + 
					losingPlayer.toUpperCase() + " has gained " + losingScore + " points");
		} else if (reason.equals(Server.TIMEOUT)) {
			setChanged();
			notifyObservers("No move was made in time by " + losingPlayer);
			setChanged();
			notifyObservers(winningPlayer.toUpperCase() + " has won with " + winningScore + " points and " + 
					losingPlayer.toUpperCase() + " has gained " + losingScore + " points");
		}
		playerType = "";
		player.getBoard().clear();
		gamesPlayed++;
	}
	
	@Override
	public void handleUnknownCommand() {
		setChanged();
		notifyObservers("Unknown command");
	}

	@Override
	public void exit() {
		if (isConnected) {
			goClient.sendMessage(Client.EXIT);
		}
		goClient.exit();
	}
	
	@Override
	public void quitGame() {
		if (isConnected) {
			goClient.sendMessage(Client.QUIT + General.COMMAND_END);
		}
	}
	
	@Override
	public GoClient getGoClient() {
		return goClient;
	}
	
	@Override
	public void handleEndOfConnection() {
		playerType = "";
		areGameSettingsRequested = false;
		isConnected = false;
		gamesPlayed = 0;
		opponentName = "";
		player = null;
		setChanged();
		notifyObservers("Connection lost");
	}
	
}
