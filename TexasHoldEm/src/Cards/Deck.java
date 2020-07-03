//////////////// //////////////////////////
//
// Title:   Deck class of 52 cards.
//
// Author:  Aidan Godfrey
// Email:   amgodfrey@wisc.edu
//
//
///////////////////////////////////////////////////////////////////////////////

package Cards;

import java.util.ArrayList;
import java.util.Collections;


/**
 * @author am_go
 *
 */
public class Deck {
	private ArrayList<Card> fullDeck;
	
	/**
	 * Creates the Deck, by creating the suits and cardNumbers to create the 52 combinations, which is now this.cards
	 */
	public Deck() {
		this.fullDeck = this.generateBoard();
	}

	
	
	/**
	 * Private helper method to generate the 52 card deck.
	 * @return 52 card list
	 */
	private ArrayList<Card> generateBoard(){
		//Generate the 13 numbers in a perfect-size array and 4 suits in a different perfect-size array to add
		//onto an arraylist
		final String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
		final int[] cardNumbers = {14,2,3,4,5,6,7,8,9,10,11,12,13};
		ArrayList<Card> cards = new ArrayList<Card>();
		
		//Combine to create the 52 card combinations and add onto an array list of cards which is then created for fulldeck.
		for (Integer number : cardNumbers) {
			for (String suit: suits) {
				Card currCard = new Card(suit, number);	 // Add each combo of cards to the full Deck
				cards.add(currCard);
			}
		} 
		Collections.shuffle(cards);
		return cards;
	}
	
	
	/**
	 * Used to get the full Deck in Play.java in order to deal cards.
	 * @return this.fullDeck 
	 */
	public ArrayList<Card> getFullDeck(){
		return this.fullDeck;
	}

}


