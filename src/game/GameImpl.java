package game;

import client.handler.GoClientHandler;

/**
 * A game of Go.
 * @author janine.kleinrot
 */
public class GameImpl implements Game {

	public GameImpl(GoClientHandler firstGoClientHandler, GoClientHandler secondGoClientHandler)  {
		
	}

	@Override
	public void run() {
		System.out.println("GAME started");
		
	}
}
