/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a
 * Big Two card game. It should override the compareTo() method it inherited from the Card
 * class to reflect the ordering of cards used in a Big Two card game
 *
 * @author Tse Man Kit
 *
 */
public class BigTwoCard extends Card {
	/**
	 * a constructor for building a card with the specified
	 * suit and rank. suit is an integer between 0 and 3, and rank is an integer between 0 and
	 * 12.
	 * @param suit
	 * @param rank
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit,rank);
	}
	/**
	 * a method for comparing the order of this card with the
	 * specified card. Returns a negative integer, zero, or a positive integer as this card is less
	 * than, equal to, or greater than the specified card.
	 */
	public int compareTo(Card card) {
		if (this.rank == 1) {
			if (this.rank==card.rank) {
				if (this.suit<card.suit) {return -1;}
				if (this.suit==card.suit) {return 0;}
			}
			return 1;
		} 
		else if (this.rank ==0) {
			if (card.rank==1) {return -1;}
			else if (card.rank==0) {
				if (this.rank==card.rank) {
					if (this.suit<card.suit) {return -1;}
					if (this.suit==card.suit) {return 0;}
				}
			}
			return 1;
		}
		if (card.rank == 1) {
			if (card.rank==this.rank) {
				if (card.suit<this.suit) {return 1;}
				if (this.suit==card.suit) {return 0;}
			}
			return -1;
		} 
		else if (card.rank ==0) {
			if (this.rank==1) {return 1;}
			else if (this.rank==0) {
				if (this.rank==card.rank) {
					if (card.suit<this.suit) {return 1;}
					if (this.suit==card.suit) {return 0;}
				}
			}
			return -1;
		}//Special ranking for Ace and 2 in big two games
		else if (this.rank<card.rank) {
			return -1;
		}
		else if (this.rank>card.rank) {
			return 1;
		}
		else if (this.suit > card.suit) {
			return 1;
		}
		else if (this.suit < card.suit) {
			return -1;
		}
		else {
			return 0;
		}
	}
	
	
}
