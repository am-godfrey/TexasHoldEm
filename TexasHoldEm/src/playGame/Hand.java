package playGame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Map;


import Cards.Card;

public class Hand {
	private ArrayList<Card> handCards;
	public HashMap<String, Integer> ratingSystem;
	
	
	/**
	 * Adds the cards to this hand
	 * @param cardsForHand
	 */
	public Hand(ArrayList<Card> cardsForHand){
		this.handCards = cardsForHand;
	}
	

	public void setRatingSystem(ArrayList<Card> potCards) {
		HashMap<String, Integer> ratingSystem = new HashMap<String, Integer>(); // Rating System

		//First, we want to evaluate the card number in his hands.
		// Created a method in case we wanted to play a variation like 5 draw poker.
		int highValuePersonal = this.evalHighCard(this.handCards);
		ratingSystem.put("PersonalHighCard", highValuePersonal); 
		
		int secondHighValuePersonal = this.evalSecondHighCard(this.handCards);
		ratingSystem.put("SecondHighCard", secondHighValuePersonal); 
		
		

		//Now, Combine all of the cards
		ArrayList<Card> combinedCard = new ArrayList<Card>();
		combinedCard.addAll(potCards);
		combinedCard.addAll(this.handCards);   
		
		//Evaluate each of the special hands with all the cards.
		HashMap<String, Integer> specialValues = this.constructSpecialRatings(combinedCard); //Private Helper, see below

		//Merge the hashmaps from 
		ratingSystem.putAll(specialValues);
		
		this.ratingSystem = ratingSystem; // Allow Access.
	} 
	
	
	public HashMap<String, Integer> getRatingSystem(){
		return this.ratingSystem;
	}
	
	/**
	 * Evaluate the highest Card
	 * @param cards
	 * @return number of the highest card
	 */
	private int evalHighCard(ArrayList<Card> cards) {
		int highCard = 0;
		for (Card currCard: cards) {
			int currNumber = currCard.getNumber();
			if(currNumber > highCard) {
				highCard = currNumber;
			}
		}

		return highCard;
	}
	
	/**
	 * Find the second highest card
	 * @param cards
	 * @return number of the second highest card
	 */
	private int evalSecondHighCard(ArrayList<Card> cards) {
		boolean firstCardSeen = true;
		int highCard = 0;
		int secondHighCard = 0;
		for (Card currCard: cards) {
			int currNumber = currCard.getNumber();
			if(firstCardSeen == true) { //For the first card, both values are the same.
				highCard = currNumber;
				firstCardSeen = false;
				continue;
			}

			if(currNumber > highCard) { // If there is a even higher card, the old high card is the second highest
				secondHighCard = highCard;
				highCard = currNumber;
			}
			else if(currNumber == highCard) { // If the current card is the same as high, then second highest is both.
				secondHighCard = highCard;
			}
			else { // currNumber < highCard, Then the high card is greater than the current card
				//If the currNumber is between the second highest and high card,
				if(currNumber > secondHighCard) { // May be a second runner up
					secondHighCard = currNumber;
				}
			}
		}

		return secondHighCard;
	}
	
	public HashMap<String, Integer> constructSpecialRatings(ArrayList<Card> combinedCard) {
		/* To evaluate the Speciality I addressed each special hand such as a straight with a number
		 * The better hands had a larger speciality number.
		 * 
		 * NoSpecial = 0 , OnePair = 1, TwoPair = 2, ThreeOfAKind = 3,
		 * Straight = 4,   Flush = 5,  FullHouse = 6, FourOfAKind = 7,
		 * StraightFlush = 8, RoyalFlush = 9.
		 * 
		 * Returns the best possible combination
		 * First integer is the card combo above, and the second integer is the high card for that specific combo
		 */ 
		
		HashMap<String, Integer> bestCombinations = new HashMap<String, Integer>(); //Used for printing out <Type, High Card>
		
		// Check for pairs
		
		HashMap<Integer, Integer> cardNumberCount = new HashMap<Integer, Integer>();
		for(Card card: combinedCard) {
			Integer pairsCurrValue = cardNumberCount.get(card.getNumber());
			if(pairsCurrValue == null) {   // Find the pairs
				pairsCurrValue = 0;
			}
			cardNumberCount.put(card.getNumber(), pairsCurrValue + 1);
		}
		
		//Pairs is a HashMap with <Acatual Card Number, Number of Occurnces>
		//System.out.println(pairs);
		
		
		// If HashMap <card #, 4> exists, then there is a four of a kind
		if(cardNumberCount.containsValue(4)) { // 4 of a kind, if four of a kind, a best combo of pairs not possible
			for(int i =15; i >0; i--) {
				if(cardNumberCount.containsKey(i)&& cardNumberCount.get(i)==4) {
					bestCombinations.put("FourOfAKind", i);
				}
			}
		}
		// If HashMap <card #, 3> exists, then there is a three of a kind
		if (cardNumberCount.containsValue(3)) {     
			for(int i =15; i >0; i--) {
				if(cardNumberCount.containsKey(i) && cardNumberCount.get(i)==3) {
					bestCombinations.put("ThreeOfAKind", i);
				}
			}
		}
		// If HashMap <card#, 2> exists then there is a pair.
		if (cardNumberCount.containsValue(2)) {
			// New list introduced for seeing which card numbers were paired, just a list of them.
			List<Integer> cardNumbersPaired = new ArrayList<Integer>();
			for(int i =15; i >0; i--) {
				if(cardNumberCount.containsKey(i) && cardNumberCount.get(i)==2) {
					cardNumbersPaired.add(i);
					
				}
			}  
			
			//Problem arises with multiple pairs, if there is two pairs, I must represent them both
			// In Addition I need to represent this
			Collections.sort(cardNumbersPaired );
			if(cardNumbersPaired.size() >2 ) { //Get the two highest
				for(int i =0; i <15; i ++) {
					if(cardNumbersPaired.size() >2 && cardNumbersPaired.contains(i)) { //Remove until there is two left, a
						cardNumbersPaired.remove(0);                        // Start indexing from 0, in order to rid the bad pairs first.
					}
				}
			}
			
			// If there is multiple card numbers paird, then include them both
			if(cardNumbersPaired.size() == 2) { //Repr Best Two Pairs
				System.out.println("Pair 1: "+ cardNumbersPaired.get(1));
				System.out.println("Pair 2: " +  cardNumbersPaired.get(0));
				bestCombinations.put("Pair1",cardNumbersPaired.get(1));
				bestCombinations.put("Pair2",cardNumbersPaired.get(0));
			}
			
			if(cardNumbersPaired.size() == 1) {
				bestCombinations.put("Pair", cardNumbersPaired.get(0));
			}
						
			
		}
		
		if(bestCombinations.containsKey("ThreeOfAKind") && (bestCombinations.containsKey("Pair")|| bestCombinations.containsKey("Pair1"))) {
			bestCombinations.put("FullHouse", 2);
		}
		

		// Specialities
		
		// Flush Processing.
		//countOfSuits counts the number of cards per suit
		HashMap<String, Integer> countOfSuits = new HashMap<String, Integer>();
		boolean flushExists= false;
		
		for(Card card: combinedCard) {
			if(countOfSuits.containsKey(card.getSuit())) { // Make a hashmap to count occurences of cards.
				Integer exsistingValue = countOfSuits.get(card.getSuit());  // <Suit, # of Cards>
				countOfSuits.put(card.getSuit(), exsistingValue + 1);
			}
			else{
				countOfSuits.put(card.getSuit(),1);
			}
		}

		
		for (Map.Entry<String, Integer> entry : countOfSuits.entrySet()) { // Iterate over map.entry, each entry,
		    Integer numberOfSuitOccur = entry.getValue(); 			// If more than 4, then there is a flush present for that suit
		    if(numberOfSuitOccur > 4) {
		    	bestCombinations.put("Flush", numberOfSuitOccur);
		    	flushExists = true;
		    }
		}
		
		
		
		
		
		
		combinedCard.sort((card1,card2) -> card2.getNumber().compareTo(card1.getNumber())); // Descending Sort on card Number
		
		
		
		// Helper Function to evalute if a straight exists
		int lowStraightCard = this.checkStraight(combinedCard);


		boolean straightExists = false;
		
		if(lowStraightCard != 0) {
			straightExists = true; // If a straight exists, then we can check for a straight\royal flush.
			bestCombinations.put("Straight", lowStraightCard);
		}
		

		

		
		//Checking for Straight Flush \ royal Flush
		//Now, we have to ensure the straight and flush coincides.
		if(straightExists && flushExists) {
			String targetSuit= null;
			for (Map.Entry<String, Integer> entry : countOfSuits.entrySet()) { // Iterate over map.entry, each entry,
			    Integer numberOfOccur = entry.getValue(); 			// If more than 4, then there is a flush present
			    String suit = entry.getKey();
			    if(numberOfOccur > 4) {
			    	targetSuit = suit;
			    	break; //Extract the suit that had suit
			    }
			}
			
			// Add each of this specific suit to the 
			ArrayList<Card> flushedCards = new ArrayList<Card>();
			for(Card card: combinedCard) {
				if(card.getSuit().equals(targetSuit)) {
					flushedCards.add(card);
				}
			}
			
			//
			int lowestCardCheck = this.checkStraight(flushedCards);
			int noStraightPresentValue = 0;
			if(lowestCardCheck != noStraightPresentValue) {
				if(lowestCardCheck == 10) {
					bestCombinations.put("RoyalFlush", lowestCardCheck); // 10 runs on the bottom of a Royal Flush
				}
				else {
					bestCombinations.put("StraightFlush", lowestCardCheck);
				}
			}
		}
		
		return bestCombinations;
	}
		
	
	/**
	 * Checks for a straight in a given amount of cards
	 * @param combinedCards
	 * @return int of the lowest card in the straight, if there is none present it returns 0
	 */
	private int checkStraight(ArrayList<Card> combinedCards) {
		int currentConsec = 0;
		int previousValue = 16; //Dummy value to allow aces to set consecutive to 1.
		for(Card card: combinedCards) {
			if(previousValue == card.getNumber()) {
				continue; // Repeat of a card allows it to continue
			}
			// If it is one less, then it is a straight of two minimum
			else if(card.getNumber() == previousValue - 1) {
					currentConsec = currentConsec + 1;
			}
			//If it is more than a one jump, the straight is broken.
			else if(card.getNumber() < previousValue -1) {
				currentConsec = 1;
			}
			// If there is ever 5 consecutive cards, then it is a straight,
			// Since we started high, this is the best straight possible.
			if(currentConsec ==5) {
				return card.getNumber();
			}
		
			
			previousValue = card.getNumber();
		}
		return 0;
		
		
	}
	
	/**
	 * @return ArrayList of Cards
	 */
	public ArrayList<Card> getHandCards(){
		return this.handCards;
	}
	
	/**
	 * @return string of ArrayList of Cards.
	 */
	@Override
	public String toString() {
		return this.handCards.toString();
	}
	

		

}
