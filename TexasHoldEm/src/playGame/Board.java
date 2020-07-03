package playGame;
import Cards.Card;
import java.util.ArrayList;

/**
 * Board class to easily flop cards and show the specific cards
 * @author am_go
 *
 */
public class Board {
	protected ArrayList<Card> allCards;
	protected ArrayList<Card> hiddenCards;
	protected ArrayList<Card> floppedCards;
	
	public Board(ArrayList<Card> incomingCards){ // When First made 3 cards are flopped and 2 cards are hidden
		this.allCards = incomingCards;
		this.floppedCards = incomingCards.get(0).combine(incomingCards.get(1), incomingCards.get(2)); // 3 Cards
		this.hiddenCards = incomingCards.get(3).combine(incomingCards.get(4)); // 2 Cards
	}
	
	public void flop() {
		Card cardToFlop = hiddenCards.get(0);
		hiddenCards.remove(0);                 // Take the Card from hidden and add it to the flopped Cards
		floppedCards.add(cardToFlop);
	}
	
	public ArrayList<Card> getFloppedCards(){
		return this.floppedCards;
	}
	
	public ArrayList<Card> getAllCards(){
		return this.allCards;
	}

}
