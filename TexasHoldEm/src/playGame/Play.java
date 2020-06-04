package playGame;
import java.util.ArrayList;
import Cards.Card;
import Cards.Deck;


public class Play {
	public ArrayList<Player> allPlayers;
	public Deck playingDeck;
	public Board boardCards;
	
	
	public Play() {
		this.allPlayers = new ArrayList<Player>();
		this.playingDeck = new Deck();
	}
	public static void main(String[] args) {
		Play game = new Play();
		game.playingDeck.shuffleCards();    // Shuffle the Deck
		game.addPlayers();
		game.dealCards();
		System.out.println(game.boardCards.allCards);
		game.evaluateHands(game.boardCards.allCards);
	}
	
	public void addPlayers() {
		for(int i = 0 ; i <3; i ++) {
			Player player = new Player(i); // Make the Initial Players
			allPlayers.add(player);
		}	
	}
	
	public void dealCards() {
		
		for(Player playerAtTable: allPlayers) {
			ArrayList<Card> cardsToAdd = new ArrayList<Card>(); // Deal Two Cards to each Player
		
			for(int addCard = 0; addCard < 2 ;  addCard++) {
				cardsToAdd.add(playingDeck.fullDeck.get(0));   // Deal 2 cards
				playingDeck.fullDeck.remove(0);  // Always get the first card since its Shuffled
			}
			playerAtTable.addHand(cardsToAdd);
			System.out.println(playerAtTable.playerHand.handCards);
		}
		
		ArrayList<Card> potCards = new ArrayList<Card>();
		for(int potCard = 0; potCard < 5; potCard++) {
			potCards.add(playingDeck.fullDeck.get(0));
			playingDeck.fullDeck.remove(0);
		}
		boardCards = new Board(potCards);
		
	}
	
	public void evaluateHands(ArrayList<Card> cardsFromPot){

		for(Player currPlayer: allPlayers) {
			currPlayer.evaluateHand(cardsFromPot);
			System.out.println(currPlayer.ratingSystem);
		}
		
		
		//return ratingSystem;
	}
}
