package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
	
	private String playerType = "";
	
	/**
	 * Creates a new Go client actor.
	 */
	public GoClientActorImpl(GoClient goClient) {
		this.goClient = goClient;
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

}
