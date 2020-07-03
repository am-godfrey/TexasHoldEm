package playGame;
import java.util.ArrayList;
import java.util.HashMap;
import Cards.Card;


public class Player {
	private int playerNumber; 
	private Hand playerHand; //Cards dealt to this player
	private int balance; 
	private boolean allIn;
	private boolean folded;
	private String playerName;
	private HashMap<String, Integer> rating; //Rating is a HashMap to set a specific value to the order of precedence
	private int totalBalanceIn;  // Per round the amount of money in the pot
	private static final int DEFAULT_BALANCE = 500;
	


	/**
	 * Overloaded player to set a balance of 500 unless otherwise specified.
	 * @param playerNumber to assign to a specific player
	 */
	public Player(int playerNumber){
		this(playerNumber, DEFAULT_BALANCE);
	}
	
	/**
	 * Second Constructor if the User wants a balance different than 500.
	 * @param playerNumber
	 * @param balance
	 */
	public Player(int playerNumber, int balance){
		this.playerNumber = playerNumber;
		this.balance = balance;
		this.folded = false;
	}
	

	
	/**
	 * Adds the specific cards to the player
	 * @param incomingCards
	 */
	public void addHand(ArrayList<Card> incomingCards) { 
		this.playerHand = new Hand(incomingCards);
	}
	
	/**
	 * Returns the hand of cards
	 * @return Hand of cards
	 */
	public Hand getHand() {
		return this.playerHand;
	}
	
	
	/**
	 * Sets the rating for the specific round
	 * Must be called prior to comparing, 
	 * Uses the pot cards as well so the deck must be dealt.
	 * 
	 * @param potCards
	 */
	public void setRating(ArrayList<Card> potCards) {
		playerHand.setRatingSystem(potCards);
		HashMap<String, Integer> rating = playerHand.getRatingSystem();
		this.rating = rating;
	} 
	
	/**
	 * Getter of the rating of their hand with the pot cards
	 * @return HashMap of the ratings.
	 */
	public HashMap<String, Integer> getRating(){
		return this.rating;
	}

	/**
	 * Compares a players hand and rating to one another.
	 * @param otherPlayer
	 * @return integer, 1 (this is better), -1 (other is better), 0 if equivalent
	 */
	public int comparePlayer(Player otherPlayer) {
		HashMap<String, Integer> thisRating = this.playerHand.getRatingSystem();
		HashMap<String, Integer> otherRating = otherPlayer.playerHand.getRatingSystem();
		int ratingValueThis = assignRatingNumber(thisRating);
		int ratingValueOther = assignRatingNumber(otherRating);
		
		if(ratingValueThis > ratingValueOther) { // If this hand is better, return 1
			return 1;
		}
		else if(ratingValueOther > ratingValueThis) { // If the other hand is better, return -1
			return -1;
		}
		else{ // If they are equivalent we go down to tiebreakers
			
			int tieBreakComparisonValue = 0; //This comparison value determines the winner for the tiebreaker
			
			if(thisRating.containsKey("RoyalFlush")) { //No tiebreakers for a royal flush.
				return 0;
			}
			else if(thisRating.containsKey("StraightFlush")) { //
				tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "StraightFlush");

			}
			else if(thisRating.containsKey("FourOfAKind")) {
				tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "FourOfAKind");
			}
			else if(thisRating.containsKey("FullHouse")) {
				tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "ThreeOfAKind");			
			}
			else if(thisRating.containsKey("Flush")){
				tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "PersonalHighCard");
				if( tieBreakComparisonValue == 0) {
					tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "SecondHighCard");
				}
			}
			else if(thisRating.containsKey("Straight")) {
				tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "Straight");
			}
			else if(thisRating.containsKey("ThreeOfAKind")) { //Tiebreaks must be dealt multiple times
				tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "ThreeOfAKind");
				if(tieBreakComparisonValue == 0) { // Another tie occurs
					tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "PersonalHighCard");
					if(tieBreakComparisonValue == 0) {
						tieBreakComparisonValue = tieBreakSpecific(thisRating,otherRating, "SecondHighCard");
					}
				}
			} // Pair 1 is the higher pair, tie break this first then the other.
			else if(thisRating.containsKey("Pair1")) {
				tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "Pair1");
				if(tieBreakComparisonValue == 0) {
					tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "Pair2");
					if(tieBreakComparisonValue == 0) {
						tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "PersonalHighCard");
						if(tieBreakComparisonValue == 0) {
							tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "SecondHighCard");

						}
					}
				}
			}
			
			else if(thisRating.containsKey("Pair")) {
				tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "Pair");
				if(tieBreakComparisonValue == 0) {
					tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "PersonalHighCard");
					if(tieBreakComparisonValue == 0) {
							tieBreakComparisonValue = tieBreakSpecific(thisRating, otherRating, "SecondHighCard");
					}
				}
			}
			return tieBreakComparisonValue;
		}

	}
	
	/**
	 * Tiebreakers
	 * This compares the tiebreakers for a specific comparison like "Straight Flush"
	 * The Integer, which is taken from the ratings for both players is used for tiebreakrs.
	 * 
	 * @param thisRating
	 * @param otherRating
	 * @param comparison
	 * @return 1 if this player has a better hand, -1 if the other player has a better hand, 0 if tied.
	 */
	private static int tieBreakSpecific(HashMap<String, Integer> thisRating , HashMap<String, Integer> otherRating, String comparison) {
		if(thisRating.get(comparison) > otherRating.get(comparison)) {
			return 1; //Gets the specific comparison, and the Integer in the hash, compares.
		}
		else if(otherRating.get(comparison) > thisRating.get(comparison)) {
			return -1;
		}
		return 0;
	}
	
	/**
	 * Assigns the number to the specific String Rating, easier to compare strings with a rating for the string
	 * RatingValue is a default of 1.
	 * 
	 * @param ratings
	 * @return
	 */
	private int assignRatingNumber(HashMap<String, Integer> ratings) {
		int ratingValue = 1;
		if(ratings.containsKey("RoyalFlush")) {
			System.out.println("Royal Flush " + this.getPlayerName());
			ratingValue = 10;
		}
		else if(ratings.containsKey("StraightFlush")) {
			System.out.println("Straight Flush " + this.getPlayerName());

			ratingValue = 9;
		}
		else if(ratings.containsKey("FourOfAKind")) {
			System.out.println("FourOfAKind " + this.getPlayerName());

			ratingValue = 8;
		}
		else if(ratings.containsKey("FullHouse")) {
			System.out.println("Full House " + this.getPlayerName());
			ratingValue = 7;
		}
		else if(ratings.containsKey("Flush")) {
			System.out.println("Flush " + this.getPlayerName());
			ratingValue = 6;
		}
		else if(ratings.containsKey("Straight")) {
			System.out.println("Straight " + this.getPlayerName());
			ratingValue = 5;
		}
		else if(ratings.containsKey("ThreeOfAKind")) {
			System.out.println("ThreeOfAKind " + this.getPlayerName());
			ratingValue = 4;
		}
		else if(ratings.containsKey("Pair1") && ratings.containsKey("Pair2")) {
			System.out.println("TwoPair " + this.getPlayerName());
			ratingValue = 3;
		}
		else if(ratings.containsKey("Pair")) {
			System.out.println("Pair " + this.getPlayerName());
			ratingValue = 2;
		}
		
		return ratingValue;
	}
	
	/**
	 * @param bid (the additonal amount they want put into the pot)
	 * @throws Exception to enforce that their balance stays positive or 0.
	 */
	public void bet(int bid) throws Exception{
		int newBalance = balance - bid;
		if(newBalance < 0) {
			throw new Exception("The amount you want to bet is outside your means, you may need to go all in!");
		}
		else if(newBalance ==0) {
			this.goAllIn();
		}
		else {
			this.totalBalanceIn = this.totalBalanceIn + bid;
			this.balance = newBalance;
		}
	}
	
	/**
	 * Allows the player to clear their balance and sets their field allIn to true so they dont bet again.
	 */
	public void goAllIn() {
		this.setAllIn(true);
		this.totalBalanceIn = this.totalBalanceIn + this.balance;
		this.balance = 0;
	}
	
	/**
	 * Adds the winnings from the pot into their balance
	 * @param winnings (total amount from pot)
	 */
	public void acceptWinnings(int winnings) {
		this.balance = winnings + this.balance;
	}
	
	/**
	 * After every round, their balance inside the pot must be zero.
	 */
	public void clearTotalBalanceIn() {
		this.totalBalanceIn = 0;
	}
	
	/**
	 * Represents the player to the console
	 */
	@Override
	public String toString() {
		return "Player " + this.playerNumber + " (" + this.playerName + ")";
	}
	
	
	public int getPlayerNumber() {
		return this.playerNumber;
	}
	
	public void setPlayerName(String name) {
		this.playerName = name;
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	public int getTotalBalanceIn() {
		return this.totalBalanceIn;
	}
	
	public void setTotalBalanceIn(int add) {
		this.totalBalanceIn = this.totalBalanceIn + add;
	}

	public int getBalance() {
		return this.balance;
	}
	
	public boolean getFolded() {
		return this.folded;
	}
	
	public void setFolded(boolean truth) {
		this.folded = truth; // Normally false, unless more than one games are played.
	}
	
	public boolean isAllIn() {
		return this.allIn;
	}
	
	public void setAllIn(boolean truth) {
		this.allIn = truth;
	}
}
