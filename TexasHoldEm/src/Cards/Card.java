//////////////// //////////////////////////
//
// Title:   The card Class is created to create a unique card out of 52.
//
// Author:  Aidan Godfrey
// Email:   amgodfrey@wisc.edu
//
//
///////////////////////////////////////////////////////////////////////////////

package Cards;
import java.util.ArrayList;

/**
 * This Card class is to create a distinct card.
 * @author am_go
 *
 */
public class Card{
	private String suitOfCard; //Suit
	private int number; // Used for number comparisons and toString() of cards 1-10.
	
	
	/**
	 * Card constructor receives the suit and number of card to be accessed later.
	 * @param String suit
	 * @param int numberOfCard
	 */
	public Card(String suit, int numberOfCard){
		this.suitOfCard = suit;
		this.number = numberOfCard;
	}

	
	/**
	 * @return String representation of the (Jack of Hearts)
	 */
	@Override
	public String toString() {  
		
		// Local variable to represent the # 11 as a Jack, as people refer to it.
		
		String totalRepr; // Final String to return. Determined by Number of the Card
		
		/*If the Card is a Jack, Queen, King, or Ace, then change the number to name
		* in reprNumberString to concat in repr.
		* Otherwise, repr is just the number concat with the other string.
		*/ 
		if(this.getNumber() >= 11 && this.getNumber() <= 14) {
			String reprNumberString = null;
			if (this.number == 14) {
				reprNumberString = "Ace"; // 14 = Ace
			}
			else if(this.number == 11) {  // 11 = Jack
				reprNumberString = "Jack";      
			}
			else if (this.number == 12) { // 12 = Queen
				reprNumberString = "Queen";
			}
			else if (this.number == 13) { // 13 = King
				reprNumberString = "King";   
			}

			totalRepr = reprNumberString + " of "+ this.getSuit();
		}
		else{ // The Number Appears as it would, i.e. "7 of Hearts"
			totalRepr = this.getNumber() + " of " + this.getSuit();
		}
		return totalRepr;
	}
	
	/**
	 * This method overloads combine to combine two cards or more as well.
	 * 
	 * @param otherCards (to combine Cards seperated by commas)
	 * @return ArrayList<Card> of All of the card combined together
	 */
	public ArrayList<Card> combine(Card... otherCards){
		// New ArrayList to combine all cards, first add the own card.
		ArrayList<Card> combinedCards = new ArrayList<Card>(); 
		combinedCards.add(this);
		
		// Add all the other Cards to the ArrayList
		for(Card cardToAdd: otherCards) {
			combinedCards.add(cardToAdd);
		}
		return combinedCards;
	}
	

	
	
	/**
	 * Method is used to get the number of the card
	 * @return this.number
	 */
	public Integer getNumber() {
		return this.number;
	}
	
	/**
	 * Method is used to get the suit of the card.
	 * @return String suit 
	 */
	public String getSuit() {
		return this.suitOfCard;
	}
}
