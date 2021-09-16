/**
 * 
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. It
 * has a private instance variable for storing the player who plays this hand. It also has methods
 * for getting the player of this hand, checking if it is a valid hand, getting the type of this hand,
 * getting the top card of this hand, and checking if it beats a specified hand
 * 
 * @author Tse Man Kit
 *
 */
public abstract class Hand extends CardList {
	
	/**
	 * a constructor for building a hand
	 * with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player=player;
		for(int i=0;i<cards.size();i++) {
			this.addCard(cards.getCard(i));
		}
		
	}
	/**
	 * the player who plays this hand.
	 */
	private CardGamePlayer player;
	
	/**
	 * a method for retrieving the player of this hand.
	 * @return the player of this hand
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * @return the top card of this hand
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(size()-1);
	}
	
	/**
	 * a method for checking if this hand beats a specified hand.
	 * @param hand
	 * @return a boolean value which indicates if this hand beats a specified hand
	 */
	public boolean beats (Hand hand) {
		if (hand.isEmpty()) {return true;}
		if (hand.size()==5) {
			if (hand.getType()=="StraightFlush") {
				if (this.getType()=="StraightFlush") {
					if (this.getTopCard().compareTo(hand.getTopCard()) >0) {
						return true;
					}
				}
				return false;
			}//both SF
			else if (hand.getType()=="Quad") {
				if (this.getType()=="StraightFlush") {return true;}
				if (this.getType()=="Quad") {
					if (this.getTopCard().compareTo(hand.getTopCard()) >0) {
						return true;
					}
				}
				return false;
			}//Quad vs Quad/SF
			else if (hand.getType()=="FullHouse") {
				if (this.getType()=="StraightFlush"||this.getType()=="Quad") {return true;}
				if (this.getType()=="FullHouse") {
					if (this.getTopCard().compareTo(hand.getTopCard()) >0) {
						return true;
					}
				}
				return false;
			}//Fullhouse
			else if (hand.getType()=="Flush") {
				if (this.getType()=="StraightFlush"||this.getType()=="Quad"||this.getType()=="FullHouse") {return true;}
				if (this.getType()=="Flush") {
					if (this.getTopCard().compareTo(hand.getTopCard()) >0) {
						return true;
					}
				}
				return false;
			}//flush
			else if (hand.getType()=="Straight") {
				if (this.getType()=="StraightFlush"||this.getType()=="Quad"||this.getType()=="FullHouse"||this.getType()=="Flush") {return true;}
				if (this.getType()=="Straight") {
					if (this.getTopCard().compareTo(hand.getTopCard()) >0) {
						return true;
					}
				}
				return false;
			}
		}//end of 5-card case
		else if (this.getType()!=hand.getType()) {return false;}
		else if (this.getTopCard().compareTo(hand.getTopCard()) >0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * this method is abstract and is meant to be overridden
	 * @return a boolean value indicating whether this hand is valid
	 */
	public abstract boolean isValid();
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * this method is abstract and is meant to be overridden
	 * @return a sting specifying the type of this hand
	 */
	public abstract String getType();

}
