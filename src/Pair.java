/**
 * This is a class for modeling a hand of Pair in a Big Two card game
 * 
 * @author Tse Man Kit
 *
 */
public class Pair extends Hand{
	

	/**
	 * a constructor for building a hand
	 * with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * a method for checking if this is a valid hand of Pair.
	 */
	public boolean isValid() {
		//check the number of the card equals 2
		//check if the ranks of two cards are equal
		if (this.size()==2) {
			if (this.getCard(0).getRank()==this.getCard(1).getRank()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand (Pair).
	 */
	public String getType() {
		return "Pair";
	}
	
	
	
}
