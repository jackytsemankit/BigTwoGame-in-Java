/**
 * This is a class for modeling a hand of Single in a Big Two card game
 * 
 * @author Tse Man Kit
 *
 */
public class Single extends Hand{
	
	/**
	 * a constructor for building a hand
	 * with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}
	/**
	 * a method for checking if this is a valid hand of Single.
	 */
	public boolean isValid() {
		//check if the number of card is 1
		if (this.size()==1) {return true;}
		else {return false;}
	}
	
	/**
	 * a method for returning a string specifying the type of this hand (Single).
	 */
	public String getType() {
		return "Single";
	}
}
