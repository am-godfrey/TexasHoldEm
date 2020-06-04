package Cards;
import java.util.*;

public class Deck {
	public ArrayList<Card> fullDeck;
	
	public Deck() {
		ArrayList<String> suits =  new ArrayList<String>(Arrays.asList("Hearts", "Diamonds", "Clubs", "Spades")) ; // Deck has 4 suit and 13 different Cards
		ArrayList<Integer> cardNumbers =  new ArrayList<Integer>( Arrays.asList(14,2,3,4,5,6,7,8,9,10,11,12,13)) ;
		this.fullDeck = new ArrayList<Card>();
		for (Integer number : cardNumbers) {
			for (String suit: suits) {
				Card currCard = new Card(suit, number);	 // Add each combo of cards to the full Deck
				this.fullDeck.add(currCard);
			}
		} 
	}
	
	public void shuffleCards(){
		Collections.shuffle(this.fullDeck); // Make Sure it is shuffled in Play.java
	}
	
	public static void main(String args[]) { 
	}

}

