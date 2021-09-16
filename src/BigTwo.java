import java.util.ArrayList;




/**
 * This class implements the CardGameTable interface. It is used to model a Big Two 
 * card game.
 * 
 * @author Tse Man Kit
 */
public class BigTwo implements CardGame {
	
	/**
	 * a deck of cards.
	 */
	private Deck deck=new BigTwoDeck();
	/**
	 * a list of players.
	 */
	private ArrayList<CardGamePlayer> playerList;
	/**
	 * a list of hands played on the table.
	 */
	private ArrayList<Hand> handsOnTable;
	/**
	 * an integer specifying the index of the current player.
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
	 * a constructor for creating a Big Two card game. You should (i) create 4
	 * players and add them to the list of players; and (ii) create a Big Two table which builds
	 * the GUI for the game and handles user actions.
	 */
	public BigTwo() {
		playerList = new ArrayList<CardGamePlayer>();
		for(int i=0;i<4;i++) {
			CardGamePlayer player =new CardGamePlayer();
			playerList.add(player);
		}
		table = new BigTwoTable(this);
		handsOnTable=new ArrayList<Hand>();
		
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
	 * @return: the deck of cards being used.
	 */
	public Deck getDeck() {
		return this.deck;
	}
	/**
	 * a method for getting the list of players.
	 * @return: the list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return playerList;
	}
	/**
	 * a method for getting the list of hands played on the table.
	 * @return: list of hands played on the table.
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return handsOnTable;
	}
	
	/**
	 * a method for getting the index of the current player.
	 * @return: the index of the current player
	 */
	public int getCurrentIdx() {
		return currentIdx;
	}
	
	/**
	 * Method to return index of the player who played the last valid hand
	 * @return: index of the player who played the last valid hand
	 */
	public int getidx() {
		return idx;
	}
	
	
	
	
	/**
	 * a method for starting/restarting the game with a given
	 * shuffled deck of cards. You should (i) remove all the cards from the players as well as
	 * from the table; (ii) distribute the cards to the players; (iii) identify the player who holds
	 * the 3 of Diamonds; and (iv) set both the currentIdx of the BigTwo instance and the
	 * activePlayer of the BigTwoTable instance to the index of the player who holds the 3
	 * of Diamonds.
	 */
	@Override
	public void start(Deck deck) {
		idx=5;
		
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
		Card card=new Card(0,2);
		for(int i=0;i<4;i++) {
			if(playerList.get(i).getCardsInHand().contains(card)) {
				table.setActivePlayer(i);
				currentIdx=i;
			}
		}
		table.printMsg(" "+playerList.get(currentIdx).getName()+"'s turn:\n");
	}
	
	
	/**
	 * Make move played by the player and call checkMove()
	 * @param playerID: the playerID of the player who makes the move
	 * @param cardIdx: the list of the indices of the cards selected by the player
	 */
	public void makeMove(int playerID,int[] cardIdx) {
		checkMove(playerID, cardIdx);
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
						table.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("\n");
					}
					else{
						table.printMsg("{"+hand.getType()+"} ");
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
				}
				else {
					if(refresh) {
						handsOnTable.add(hand);
						playerList.get(currentIdx).removeCards(hand);
						refresh=false;
						idx=5;
						repaint=true;
						table.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("\n");
					}
					else if(hand.size()!=handsOnTable.get(handsOnTable.size()-1).size()) {
						table.printMsg("{"+hand.getType()+"} ");
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
					else if(hand.beats(handsOnTable.get(handsOnTable.size()-1))) {
						handsOnTable.add(hand);
						playerList.get(currentIdx).removeCards(hand);
						idx=5;
						repaint=true;
						table.printMsg("{"+hand.getType()+"} ");
						print(list,true,false);
						table.printMsg("\n");
					}
					else {
						table.printMsg("{"+hand.getType()+"} ");
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
				}
				
			}
			else if(hand==null&&handsOnTable.size()==0) {
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
			table.printMsg("Game ends");
			table.playSound("win.wav");
			for(int i=0;i<4;i++) {
				if(playerList.get(i).getNumOfCards()!=0) {
					table.printMsg(playerList.get(i).getName()+" has "+playerList.get(i).getNumOfCards()+" cards in hand.\n");
				}
				else {
					table.printMsg(playerList.get(i).getName()+" wins the game.\n");
				}
			}
			table.disable();
			if(currentIdx>0) {
				currentIdx=currentIdx-1;
			}
			else {
				currentIdx=3;
			}
		}
		table.setActivePlayer(currentIdx);
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
	 * a mathod for formatting the messages that get printed on the msgArea
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
	 * a method for starting a Big Two card game. It should (i)
	 * create a Big Two card game; (ii) create and shuffle a deck of cards; and (iii) start the
	 * game with the deck of cards.
	 * @param args
	 */
	public static void main(String[] args) {
		BigTwo bt = new BigTwo();
		BigTwoDeck d = new BigTwoDeck();
	    d.shuffle();
		bt.start(d);
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
	
}
