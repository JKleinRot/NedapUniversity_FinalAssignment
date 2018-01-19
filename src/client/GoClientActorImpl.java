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

import protocol.Protocol.Client;
import protocol.Protocol.General;

/**
 * Handles the actions required after input received from the GoClientHandler and GoClientTUI.
 * @author janine.kleinrot
 */
public class GoClientActorImpl extends Observable implements GoClientActor {
	
	private GoClient goClient;
	
	private String playerType;
	
	private String boardSize;
	
	private String stoneColor;
	
	private boolean areGameSettingsRequested;
	
	/**
	 * Creates a new Go client actor.
	 */
	public GoClientActorImpl(GoClient goClient) {
		this.goClient = goClient;
		areGameSettingsRequested = false;
		playerType = "";
		boardSize = "";
		stoneColor = "";
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
	public void showConnectionConfirmed(String[] words) {
		setChanged();
		notifyObservers("Connected");
	}
	
	@Override
	public void alreadyConnected() {
		setChanged();
		notifyObservers("Already connected");
	}
	
	@Override
	public void requestGame(String goPlayerType) {
		if (playerType.isEmpty()) {
			this.playerType = goPlayerType;
			goClient.sendMessage(Client.REQUESTGAME + General.DELIMITER1 + 2 + 
					General.DELIMITER1 + Client.RANDOM + General.COMMAND_END);
			setChanged();
			notifyObservers("Game requested " + playerType);
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
			this.stoneColor = goStoneColor;
			this.boardSize = goBoardSize;
			if (stoneColor.equals("white")) {
				goClient.sendMessage(Client.SETTINGS + General.DELIMITER1 + "white" + 
						General.DELIMITER1 + boardSize + General.COMMAND_END);
				setChanged();
				notifyObservers("Game settings set white");
			} else if (stoneColor.equals("black")) {
				goClient.sendMessage(Client.SETTINGS + General.DELIMITER1 + "black" + 
						General.DELIMITER1 + boardSize + General.COMMAND_END);
				setChanged();
				notifyObservers("Game settings set black");
			} else {
				setChanged();
				notifyObservers("Illegal stone color");
			}
		} else {
			setChanged();
			notifyObservers("No game settings requested");
		}
	}

	@Override
	public void setReceivedGameSettings(String aStoneColor, String aBoardSize) {
		this.stoneColor = aStoneColor;
		this.boardSize = aBoardSize;
		setChanged();
		notifyObservers("Game settings received " + stoneColor);
	}
}
