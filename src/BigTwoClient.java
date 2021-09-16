import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class BigTwoClient implements CardGame , NetworkGame {
	
	/**
	 * a deck of cards.
	 */
	private Deck deck = new BigTwoDeck();
	/**
	 * a list of players.
	 */
	private ArrayList<CardGamePlayer> playerList;
	/**
	 * a list of hands played on the table.
	 */
	private ArrayList<Hand> handsOnTable;
	/**
	 * an integer specifying the playerID (i.e., index) of the local player.
	 */
	private int playerID;
	/**
	 * a string specifying the name of the local player.
	 */
	private String playerName =null;
	/**
	 * a string specifying the IP address of the game server.
	 */
	private String serverIP=null;
	/**
	 * an integer specifying the TCP port of the game server.
	 */
	private int serverPort;
	/**
	 * a socket connection to the game server.
	 */
	private Socket sock;
	/**
	 * an ObjectOutputStream for sending messages to the server.
	 */
	private ObjectOutputStream oos;
	/**
	 * an ObjectInputStream for receiving messages from the server.
	 */
	private ObjectInputStream ois;
	/**
	 * an integer specifying the index of the player for the current turn.
	 */
	private int currentIdx;
	/**
	 * a Big Two table which builds the GUI for the game and handles
	 * all user actions.
	 */
	private BigTwoTable table;
	
	/**
	 * an integer specifying the index of the player that played the last valid hand.
	 */
	private int idx=5;
	
	/**
	 * boolean values for determining if the frame needs to be repainted 
	 */
	private boolean refresh=false,repaint=true;
	
	
	/**
	 * a constructor for creating a Big Two client.
	 */
	public BigTwoClient() {
		
		playerName=JOptionPane.showInputDialog(playerName);
		playerList = new ArrayList<CardGamePlayer>();
		for(int i=0;i<4;i++) {
			CardGamePlayer player =new CardGamePlayer();
			playerList.add(player);
		}
		table = new BigTwoTable(this);
		handsOnTable=new ArrayList<Hand>();
		makeConnection();
		table.disable();
	}

	/**
	 * a method for getting the number of players.
	 */
	@Override
	public int getNumOfPlayers() {
		return playerList.size();
	}

	/**
	 * a method for getting the deck of cards being used.
	 */
	@Override
	public Deck getDeck() {
		return deck;
	}

	/**
	 * a method for getting the list of players.
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}

	/**
	 * a method for getting the list of hands played on the table.
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}
	
	/**
	 * a method for getting the index of the player for the current turn.
	 */
	@Override
	public int getCurrentIdx() {
		return currentIdx;
	}
	
	/**
	 * a method for starting/restarting the game with a given shuffled deck of cards.
	 */
	@Override
	public void start(Deck deck) {
		idx=5;
		
		table.enable();
		for(int i=0;i<this.getNumOfPlayers();i++) {
			playerList.get(i).removeAllCards();
		}
		
		handsOnTable.clear();
		for(int i=0;i<4;i++) {//distribute cards to 4 players, each 13
			for(int j=0;j<13;j++) {
				playerList.get(i).addCard(deck.getCard(i*13+j));
			}
		}
		
		for (int i=0;i<4;i++) { //sort from small to larger, according to big-two logic
			playerList.get(i).sortCardsInHand();
		}
		Card threeOfDiamonds=new Card(0,2);
		for(int i=0;i<4;i++) {
			if(playerList.get(i).getCardsInHand().contains(threeOfDiamonds)) {
				currentIdx=i;
			}
		}
		table.setActivePlayer(playerID);
		table.printMsg(" "+playerList.get(currentIdx).getName()+"'s turn:\n");
		
	}

	/**
	 * Make move played by the player and call checkMove()
	 * @param playerID: the playerID of the player who makes the move
	 * @param cardIdx: the list of the indices of the cards selected by the player
	 */
	public void makeMove(int playerID,int[] cardIdx) {
		CardGameMessage message = new CardGameMessage(6,playerID,cardIdx);
		sendMessage(message);
	}
	/**
	 * a method for checking a move made by a player.
	 *  This method should be called from the makeMove() method from the
	 *  CardGame interface. *
	 */
	@Override
	public void checkMove(int playerID,int[] cardIdx) {
		
		CardList list=playerList.get(currentIdx).play(cardIdx);
		
		Card card=new Card(0,2);
		
		
		if(list!=null) {
			Hand hand=composeHand(playerList.get(currentIdx),list);
			if(hand!=null) {
				
				if(handsOnTable.size()==0){
					if(hand.contains(card)){
						handsOnTable.add(hand);
						playerList.get(currentIdx).removeCards(hand);
						repaint=true;
						table.printMsg(" {"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("\n");
					}
					else{
						table.printMsg(" {"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg(" <==Not a legal move!!!\n");
						table.playSound("error.wav");
						if(currentIdx>0) {
							currentIdx=currentIdx-1;
						}
						else {
							currentIdx=3;
						}
						repaint=false;
					} 
				}
				else {
					if(refresh) {
						handsOnTable.add(hand);
						playerList.get(currentIdx).removeCards(hand);
						refresh=false;
						idx=5;
						repaint=true;
						table.printMsg(" {"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("\n");
					}
					else if(hand.size()!=handsOnTable.get(handsOnTable.size()-1).size()) {
						table.printMsg(" {"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg(" <==Not a legal move!!!\n");
						table.playSound("error.wav");
						if(currentIdx>0) {
							currentIdx=currentIdx-1;
						}
						else {
							currentIdx=3;
						}
						repaint=false;
							
					}
					else if(hand.beats(handsOnTable.get(handsOnTable.size()-1))) {
						handsOnTable.add(hand);
						playerList.get(currentIdx).removeCards(hand);
						idx=5;
						repaint=true;
						table.printMsg(" {"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("\n");
					}
					else {
						table.printMsg(" {"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg(" <==Not a legal move!!!\n");
						table.playSound("error.wav");
						if(currentIdx>0) {
							currentIdx=currentIdx-1;
						}
						else {
							currentIdx=3;
						}
						repaint=false;
					}
				}
				
			}
			else if(hand==null&&handsOnTable.size()==0) {
				print(list,true,false);
				table.printMsg(" <==Not a legal move!!!\n");
				table.playSound("error.wav");
				if(currentIdx>0) {
					currentIdx=currentIdx-1;
				}
				else {
					currentIdx=3;
				}
				repaint=false;
			}
			else {
				print(list,true,false);
				table.printMsg("<==Not a legal move!!!\n");
				table.playSound("error.wav");
				if(currentIdx>0) {
					currentIdx=currentIdx-1;
				}
				else {
					currentIdx=3;
				}
				repaint=false;
			}
			currentIdx = (currentIdx +1) %4;
			
			
		}	
		
		
		else {
			if(handsOnTable.size()==0) {
				table.printMsg(" <==Not a legal move!!!\n");
				table.playSound("error.wav");
				if(currentIdx>0) {
					currentIdx=currentIdx-1;
				}
				else {
					currentIdx=3;
				}
				repaint=false;
			}
			
			else if(idx==5) {
				repaint=true;
				if(currentIdx!=0) {
					idx=currentIdx-1;
				}
				else {
					idx=3;
				}
				table.printMsg("{pass}\n");
			}
			else if(idx==currentIdx){
				table.printMsg(" <==Not a legal move!!!\n");
				table.playSound("error.wav");
				if(currentIdx>0) {
					currentIdx=currentIdx-1;
				}
				else {
					currentIdx=3;
				}
				repaint=false;
				refresh=true;
			}
			
			else{
				table.printMsg("{pass}\n");
				repaint=true;
			}
			if(idx==(currentIdx+1)%4){
				//repaint=true;
				refresh=true;
				
			}
			currentIdx = (currentIdx+1) %4;
				
		}
	
		//game ends
		if(endOfGame()) {
			String endGameMsg="\nGame ends\n";
			table.playSound("win.wav");
			for(int i=0;i<4;i++) {
				if(playerList.get(i).getNumOfCards()!=0) {
					endGameMsg +=playerList.get(i)+" wins the game.\n";
				}
				else {
					endGameMsg +=playerList.get(i)+" has "+playerList.get(i).getNumOfCards()+" cards\n";
				}
			}
			table.disable();
			int optionChosen = JOptionPane.showOptionDialog(null, endGameMsg, "Game has ended", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

			if(optionChosen == JOptionPane.OK_OPTION)
			{
				CardGameMessage ready = new CardGameMessage(CardGameMessage.READY,-1,null);
				sendMessage(ready);
				
			}
			if(currentIdx>0) {
				currentIdx=currentIdx-1;
			}
			else {
				currentIdx=3;
			}
		}
		//table.setActivePlayer(currentIdx);
		//repaint
		if(repaint&&!endOfGame()) {
			table.printMsg(" "+playerList.get(currentIdx).getName()+"'s turn:\n");
		}
		
	}
	
	/**
	 * a method for checking if the game ends.
	 */
	@Override
	public boolean endOfGame() {
		for(int i=0;i<getNumOfPlayers();i++) {
			if(playerList.get(i).getNumOfCards()==0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * a method to facilitate the printing of messages
	 * @param hand
	 * @param printFront
	 * @param printIndex
	 */
	private void print(CardList hand,boolean printFront, boolean printIndex) {
		ArrayList<Card> cards= new ArrayList<Card>();
		
		for(int i=0;i<hand.size();i++) {
			cards.add(hand.getCard(i));
		}
		for (int i = 0; i < cards.size(); i++) {
			String string = "";
			if (printIndex) {
				string = i + " ";
			}
			if (printFront) {
				string = string + "[" + cards.get(i) + "]";
			} 
			else {
				string = string + "[  ]";
			}
			if (i % 13 != 0) {
				string = " " + string;
			}
			table.printMsg(string);
			if (i % 13 == 12 || i == cards.size() - 1) {
				table.printMsg("");
			}
		}
	
	}
	
	/**
	 * a method for creating an instance of BigTwoClient.
	 * @param args
	 */
	public static void main(String[] args) {
		BigTwoClient bigTwo = new BigTwoClient();
	}
	/**
	 * a method for returning a valid hand from the specified 
	 * list of cards of the player. Returns null is no
	 * valid hand can be composed from the specified list of cards.
	 * @param player: The active player that plays the hand
	 * @param cards: The hand played by player
	 * @return: An object of the type of hand played
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) { // OK
		
		Single single = new Single(player, cards);
		if (single.isValid()) {
			return single;
		}
		
		Pair pair = new Pair(player, cards);
		if (pair.isValid()) {
			return pair;
		}
	
		Triple triple = new Triple(player, cards);
		if (triple.isValid()) {
			return triple;
		}
		
		StraightFlush straightflush = new StraightFlush(player, cards);
		if (straightflush.isValid()) {
			return straightflush;
		}
		
		Straight straight = new Straight(player, cards);
		if (straight.isValid()) {
			return straight;
		}
		
		Flush flush = new Flush(player, cards);
		if (flush.isValid()) {
			return flush;
		}
		
		FullHouse fullhouse = new FullHouse(player, cards);
		if (fullhouse.isValid()) {
			return fullhouse;
		}
		
		Quad quad = new Quad(player, cards);
		if (quad.isValid()) {
			return quad;
		}
		
		return null;				
	
	}
	
	/**
	 * a method for getting the playerID (i.e., index) of the local player.
	 */
	@Override
	public int getPlayerID() {
		return playerID;
	}
	
	/**
	 * a method for setting the playerID (i.e., index) of the local player.
	 */
	@Override
	public void setPlayerID(int playerID) {
		this.playerID=playerID;
	}
	
	/**
	 * a method for getting the name of the local player.
	 */
	@Override
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * a method for setting the name of the local player.
	 */
	@Override
	public void setPlayerName(String playerName) {
		this.playerName=playerName;
		
	}
	
	/**
	 * a method for getting the IP address of the game server.
	 */
	@Override
	public String getServerIP() {
		return serverIP;
	}

	/**
	 * a method for setting the IP address of the game server.
	 */
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP=serverIP;
	}
	
	/**
	 * a method for getting the TCP port of the game server.
	 */
	@Override
	public int getServerPort() {
		return serverPort;
	}
	
	/**
	 * a method for setting the TCP port of the game server.
	 */
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort=serverPort;
	}
	
	/**
	 * a method for making a socket connection with the game server.
	 */
	@Override
	public void makeConnection() {
		
		try {
			
			sock = new Socket("127.0.0.1",2396);
			oos= new ObjectOutputStream(sock.getOutputStream());
			
			
			Thread t = new Thread(new ServerHandler());
			t.start();
			
			System.out.println("Network established");
			table.disableConnect();			
			
			CardGameMessage join = new CardGameMessage(CardGameMessage.JOIN,-1,playerName);
			sendMessage(join);
			CardGameMessage ready = new CardGameMessage(CardGameMessage.READY,-1,null);
			sendMessage(ready);
			
						
			
			
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			table.printMsg(" Error in connecting to the server!\n");
			table.enableConnect();
			}
		
	}
	/**
	 * a method for parsing the messages
	 * received from the game server. This method should be called from the thread
	 * responsible for receiving messages from the game server. Based on the message type,
	 * different actions will be carried out (please refer to the general behavior of the client
	 * described in the previous section).
	 */
	@Override
	public void parseMessage(GameMessage message) {
		
		if (message.getType()==CardGameMessage.PLAYER_LIST) {
			table.disable();
			playerID=message.getPlayerID();
			String names[]=(String[])message.getData();
			for (int i=0;i<4;i++) {
				playerList.get(i).setName(names[i]);
			}
		}
		
		if (message.getType()==CardGameMessage.JOIN) {
			playerList.get(message.getPlayerID()).setName((String)message.getData());
			table.repaint();
			table.enableConnect();
		}
		
		if (message.getType()==CardGameMessage.FULL) {
			table.printMsg(" Server is full");
			try {
				oos.flush();
				sock.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Error in closing the client socket at "
						+ sock.getRemoteSocketAddress());
			}
		}
		
		if (message.getType()==CardGameMessage.QUIT) {
			table.printMsg(" "+playerList.get(message.getPlayerID()).getName()+" (\"/"+message.getData()+")\" left the game\n" );
			playerList.get(message.getPlayerID()).setName(null);
			if(!endOfGame()) {
				table.disable();
				for (int i=0;i<playerList.size();i++) {
					playerList.get(i).removeAllCards();
				}
				handsOnTable.clear();
				table.clearMsgArea();
				sendMessage(new CardGameMessage(4,-1,null));
			}
			
			table.repaint();
		}
		
		if (message.getType()==CardGameMessage.READY) {
			table.printMsg(" "+playerList.get(message.getPlayerID()).getName()+": Ready!\n");
			table.repaint();
		}
		
		if(message.getType()==CardGameMessage.START) {
			start((BigTwoDeck)message.getData());
			table.repaint();
		}
		
		if(message.getType()==CardGameMessage.MOVE) {
			checkMove(message.getPlayerID(),(int[])message.getData());
			table.repaint();
		}
		
		if(message.getType()==CardGameMessage.MSG) {
			
			table.addTochatMsgArea(((String)message.getData()+"\n"));
			
		}
	}
	/**
	 * a method for sending the specified
	 * message to the game server. This method should be called whenever the client wants to
	 * communicate with the game server or other clients.
	 */
	@Override
	public synchronized void sendMessage(GameMessage message) {
		try {			
			oos.writeObject(message);	
			}
		catch (Exception ex) {
			ex.printStackTrace();
			}
		
	}

	/**
	 * an inner class that implements the Runnable interface
	 * @author Tse Man Kit
	 *
	 */
	private class ServerHandler implements Runnable {

		public  ServerHandler() {
			try {
				ois = new ObjectInputStream(sock.getInputStream());	
				}
			
			catch (Exception ex) {
				System.out.println("exception at server handler");
				ex.printStackTrace();
				table.enableConnect();
				}
		}
		public void run() {
			GameMessage message;
			try {
				
				message =(CardGameMessage)ois.readObject();
				while (message!=null) {
					parseMessage(message);
					message =(CardGameMessage)ois.readObject();
					table.disableConnect();
				 	}
				}
			catch (Exception ex) {
				ex.printStackTrace();
				table.printMsg(" Error in communicating with the server!\n");
				table.enableConnect();
				}
			
		}
		
	}

	/**
	 * a methoud for quiting the game, which is called when the "Quit" button is pressed
	 */
	public void quit() {
		try {
			oos.flush();
			sock.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error in closing the client socket at "
					+ sock.getRemoteSocketAddress());
		}
		
	}
	
	
}
