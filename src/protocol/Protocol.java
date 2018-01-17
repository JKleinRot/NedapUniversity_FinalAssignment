package protocol;


public class Protocol {
	/**
	 * @author Rosalyn.Sleurink
	 * @version 1
	 */
	
	/**
	 * OVERAL WAAR SPATIES STAAN KOMT DUS DELIMITER1 (in de voorbeelden en formats).
	 * OOK MOETEN ALLE COMMANDO'S EINDIGEN MET COMMAND_END.
	 */
	
	public static class Client {
		/**
		 * Het eerste commando wat de client naar de server stuurt. Gaat om versie
		 * van het protocol. De volgorde van de extensions is als volgt: 
		 * chat challenge leaderboard security 2+ simultaneous multiplemoves.
		 * Format: NAME clientnaam VERSION versienummer EXTENSIONS boolean boolean boolean etc
		 * Voorbeeld: NAME piet VERSION 2 EXTENSIONS 0 0 1 1 0 0 0
		 */
		public static final String NAME = "NAME";
		public static final int VERSION = 1;
		public static final String EXTENSIONS = "EXTENSIONS";
		
		/**
		 * Om een move te communiceren. Bord begint linksboven met 0,0.
		 * Format: MOVE rij_kolom of MOVE PASS
		 * Voorbeeld: MOVE 1_3
		 */
		public static final String MOVE = "MOVE";
		public static final String PASS = "PASS";
		
		/**
		 * Als de server een START met aantal spelers heeft gestuurd mag je je voorkeur doorgeven 
		 * voor kleur en grootte van het bord. Dit wordt gevraagd aan de speler die er als eerst 
		 * was.
		 * Format: SETTINGS kleur bordgrootte
		 * Voorbeeld: SETTINGS BLACK 19
		 */
		public static final String SETTINGS = "SETTINGS";
		
		/**
		 * Als je midden in een spel zit en wil stoppen. Wordt niet gestuurd als client abrupt 
		 * afgesloten wordt.
		 * Format: QUIT
		 * Voorbeeld: QUIT
		 */
		public static final String QUIT = "QUIT";
		
		/**
		 * Sturen als je een spel wilt spelen. De eerste keer en als een spel afgelopen is opnieuw.
		 * Als je de Challenge extensie niet ondersteunt, stuur dan RANDOM in plaats van een naam.
		 * Format: REQUESTGAME aantalspelers naamtegenspeler (RANDOM als je geen challenge doet)
		 * Voorbeeld: REQUESTGAME 2 RANDOM of REQUESTGAME 2 piet
		 */
		public static final String REQUESTGAME = "REQUESTGAME";
		public static final String RANDOM = "RANDOM";
		
		
		// -------------- EXTENSIES ------------ //
		
		/**
		 * Als je de uitdaging wil accepteren.
		 * Format: ACCEPTGAME naamuitdager
		 * Voorbeeld: ACCEPTGAME piet
		 */
		public static final String ACCEPTGAME = "ACCEPTGAME";
		
		/**
		 * Als je de uitdaging niet accepteert.
		 * Format: DECLINEGAME naamuitdager
		 * Voorbeeld: DECLINEGAME piet
		 */
		public static final String DECLINEGAME = "DECLINEGAME";
		
		/**
		 * Om op te vragen wie je allemaal kan uitdagen.
		 * Format: LOBBY
		 * Voorbeeld: LOBBY
		 */
		public static final String LOBBY = "LOBBY";
		
		/**
		 * Om een chatbericht te sturen. Als je in een spel zit mogen alleen de spelers het zien. 
		 * Als je in de lobby zit mag iedereen in de lobby het zien.
		 * Format: CHAT bericht
		 * Voorbeeld: CHAT hoi ik ben piet
		 */
		public static final String CHAT = "CHAT";
		
		/**
		 * Om de leaderboard op te vragen. Overige queries moet je afspreken met anderen die ook 
		 * leaderboard willen implementeren.
		 * Format: LEADERBOARD
		 * Voorbeeld: LEADERBOARD
		 */
		public static final String LEADERBOARD = "LEADERBOARD";
	}

	public static class Server {
		/**
		 * Het eerste commando wat de server naar de client stuurt. Gaat om versie
		 * van het protocol. De volgorde van de extensions is als volgt: 
		 * chat challenge leaderboard security 2+ simultaneous multiplemoves.
		 * Format: NAME clientnaam VERSION versienummer EXTENSIONS boolean boolean boolean etc
		 * Voorbeeld: NAME serverpiet VERSION 2 EXTENSIONS 0 0 1 1 0 0 0
		 */
		public static final String NAME = "NAME";
		public static final int VERSION = 1;
		public static final String EXTENSIONS = "EXTENSIONS";
		
		/**
		 * Een spel starten. Dit stuur je naar de eerste speler. 
		 * Format: START aantalspelers (speler 1)
		 * Format: START kleur bordgrootte (naar andere speler(s))
		 * Voorbeeld: START 2 of START BLACK 19
		 */
		public static final String START = "START";
		
		/**
		 * Vertelt aan de spelers welke beurt er gedaan is. Speler1 is de speler die de beurt heeft
		 * gedaan, speler 2 de speler die nu aan de beurt is om een MOVE door te geven. Als dit de
		 * eerste beurt is zijn speler1 en speler2 allebei de speler die nu aan de beurt is, en dan
		 * stuur je FIRST i.p.v. de integers.
		 * Format: TURN speler1 rij_kolom speler2
		 * Voorbeeld: TURN piet 1_3 jan of TURN piet FIRST piet
		 */
		public static final String TURN = "TURN";
		
		/**
		 * Als het spel klaar is om welke reden dan ook. Reden kan zijn FINISHED (normaal einde), 
		 * ABORTED (abrupt einde) of TIMEOUT (geen respons binnen redelijke tijd)
		 * Format: ENDGAME reden winspeler score verliesspeler score
		 * Voorbeeld: ENDGAME FINISHED piet 12 jan 10
		 */
		public static final String ENDGAME = "ENDGAME";
		public static final String FINISHED = "FINISHED";
		public static final String ABORTED = "ABORTED";
		public static final String TIMEOUT = "TIMEOUT";
		
		/**
		 * Errortypes die we gedefinieerd hebben: UNKNOWNCOMMAND, INVALIDMOVE, NAMETAKEN, 
		 * INCOMPATIBLEPROTOCOL, OTHER.
		 * Format: ERROR type bericht
		 * Voorbeeld: ERROR NAMETAKEN de naam piet is al bezet
		 */
		public static final String ERROR = "ERROR";
		public static final String UNKNOWN = "UNKNOWNCOMMAND";
		public static final String INVALID = "INVALIDMOVE";
		public static final String NAMETAKEN = "NAMETAKEN";
		public static final String INCOMPATIBLEPROTOCOL = "INCOMPATIBLEPROTOCOL";
		public static final String OTHER = "OTHER";
		
		// -------------- EXTENSIES ------------ //

		/**
		 * Stuurt aan één client wie hem heeft uitgedaagd.
		 * Format: REQUESTGAME uitdager
		 * Voorbeeld: REQUESTGAME piet
		 */
		public static final String REQUESTGAME = "REQUESTGAME";
		
		/**
		 * Stuurt aan de uitdager dat de uitdaging is geweigerd en door wie.
		 * Format: DECLINED uitgedaagde
		 * Voorbeeld: DECLINED piet
		 */
		public static final String DECLINED = "DECLINED";
		
		/**
		 * Reactie op LOBBY van de client. Stuurt alle spelers die uitgedaagd kunnen worden 
		 * (in de lobby zitten).
		 * Format: LOBBY naam1_naam2_naam3
		 * Voorbeeld: LOBBY piet jan koos
		 */
		public static final String LOBBY = "LOBBY";

		/**
		 * Stuurt chatbericht naar relevante clients (in spel of in lobby).
		 * Format: CHAT naam bericht
		 * Voorbeeld: CHAT piet hallo ik ben piet (Met correcte delimiter ziet dat er dus uit als:
		 * CHAT$piet$hallo ik ben piet)
		 */
		public static final String CHAT = "CHAT";
				
		/**
		 * Reactie op LEADERBOARD van client. Stuurt de beste 10 scores naar één client.
		 * Overige queries moet je afspreken met anderen die ook 
		 * leaderboard willen implementeren.
		 * Format: LEADERBOARD naam1 score1 naam2 score2 naam3 score3 enz
		 * Voorbeeld: LEADERBOARD piet 1834897 jan 2 koos 1
		 */
		public static final String LEADERBOARD = "LEADERBOARD";
	}

	public static class General {
		
		/**
		 * ENCODING kun je ergens bij je printstream/bufferedreader/writer instellen (zie API).
		 */
		public static final String ENCODING = "UTF-8";
		public static final int TIMEOUTSECONDS = 90;
		public static final short DEFAULT_PORT = 5647;
		public static final char DELIMITER1 = '$';
		public static final char DELIMITER2 = '_';
		public static final String COMMAND_END = "\n";
	}

}
