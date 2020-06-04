package Cards;

import java.util.ArrayList;

public class Card{
	String suitOfCard;
	public int number;
	String reprNumber;
	int trueNumber;
	
	
	public Card(String suit, int numberOfCard){
		this.suitOfCard = suit;
		this.number = numberOfCard;
	}

	
	public String toString() {  // Represent the Card for the User to View
		if (this.number == 14) {
			reprNumber = "Ace";
		}
		else if(this.number == 11) {  //  Check for a Face card or a number Card
			reprNumber = "Jack";      // represent it either way.
		}
		else if (this.number == 12) {
			reprNumber = "Queen";
		}
		else if (this.number == 13) {
			reprNumber = "King";   
		}
		else {
			int trueNumber = this.number;  // Stays that number
			String repr = trueNumber + " of " + this.suitOfCard;
			return repr; 
		}
		String repr = reprNumber + " of " + this.suitOfCard;
		return repr;
		
	}
	
	public ArrayList<Card> combine(Card... otherCards){
		ArrayList<Card> combinedCards = new ArrayList<Card>(); // Combine all cards together, 1 - how many you need
		combinedCards.add(this);
		for(Card cardToAdd: otherCards) {
			combinedCards.add(cardToAdd);
		}
		return combinedCards;
		
	}
	
	

}