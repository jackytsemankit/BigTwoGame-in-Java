/**
 * This is a class for modeling a hand of Straight in a Big Two card game
 * 
 * @author Tse Man Kit
 *
 */
public class Straight extends Hand{
	
	/**
	 * a constructor for building a hand
	 * with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * a method for checking if this is a valid hand of Straight.
	 */
	public boolean isValid() {
		//check the number of the card equals 5
		//check if the ranks of five cards are consecutive
		if (this.size()==5) {
			this.sort();
			if (this.getCard(0).getSuit()== this.getCard(1).getSuit() && this.getCard(1).getSuit()== this.getCard(2).getSuit() && this.getCard(2).getSuit()== this.getCard(3).getSuit() && this.getCard(3).getSuit()== this.getCard(4).getSuit()) {
				return false;
			}
			if (this.getCard(0).getRank()==10 && this.getCard(1).getRank()== 11 && this.getCard(2).getRank()== 12 && this.getCard(3).getRank()== 0 && this.getCard(4).getRank()== 1) {
				return true;
			}
			if (this.getCard(0).getRank()== 9 && this.getCard(1).getRank()== 10 && this.getCard(2).getRank()== 11 && this.getCard(3).getRank()== 12 && this.getCard(4).getRank()== 0) {
				return true;
			}
			if (this.getCard(0).getRank()+1 == this.getCard(1).getRank() && this.getCard(1).getRank()+1 == this.getCard(2).getRank() && this.getCard(2).getRank()+1 == this.getCard(3).getRank() && this.getCard(3).getRank()+1 == this.getCard(4).getRank()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand (Straight).
	 */
	public String getType() {
		return "Straight";
	}

}
