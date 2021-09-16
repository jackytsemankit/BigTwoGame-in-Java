/**
 * This is a class for modeling a hand of Triple in a Big Two card game
 * 
 * @author Tse Man Kit
 *
 */
public class Triple extends Hand {
	/**
	 * a constructor for building a hand
	 * with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * a method for checking if this is a valid hand of Triple.
	 */
	public boolean isValid() {
		//check the number of the card equals 3
		//check if the ranks of three cards are equal
		if (this.size()==3) {
			
			if (this.getCard(0).getRank()==this.getCard(1).getRank() && this.getCard(1).getRank()== this.getCard(2).getRank()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand (Triple).
	 */
	public String getType() {
		return "Triple";
	}

}
