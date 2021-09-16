/**
 * This is a class for modeling a hand of Quad in a Big Two card game
 * 
 * @author Tse Man Kit
 *
 */
public class Quad extends Hand{
	
	/**
	 * a constructor for building a hand
	 * with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * a method for checking if this is a valid hand of Quad.
	 */
	public boolean isValid() {
		//check the number of the card equals 5
		//check if the hand is a quad, i.e. a quadruplet
		this.sort();
		if (this.size()==5) {
			if (this.getCard(0).getRank()== this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank()== this.getCard(3).getRank() && this.getCard(3).getRank()!= this.getCard(4).getRank()) {
				return true;
			}
			if (this.getCard(0).getRank()!= this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank()== this.getCard(3).getRank() && this.getCard(3).getRank()== this.getCard(4).getRank()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand (Quad).
	 */
	public String getType() {
		return "Quad";
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * it is overridden so that the card in
	 * the quadruplet with the highest suit in a quad is referred to as the top card of this quad
	 */
	public Card getTopCard() {

		this.sort();
		if (this.getCard(0).getRank()== this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank()== this.getCard(3).getRank() && this.getCard(3).getRank()!= this.getCard(4).getRank()) {
			return this.getCard(3);
		}
		//if (this.getCard(0).getRank()== this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank()== this.getCard(3).getRank() && this.getCard(3).getRank()!= this.getCard(4).getRank()) {
		//	return this.getCard(4);
		//}
		
		return this.getCard(4);
		}
}