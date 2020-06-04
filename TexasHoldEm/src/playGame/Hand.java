package playGame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

import Cards.Card;

public class Hand {
	ArrayList<Card> handCards;
	
	public Hand(ArrayList<Card> cardsForHand){
		this.handCards = cardsForHand;
		
	}
	
	
	public HashMap<String, Integer> evaluateHand(ArrayList<Card> potCards) {
		HashMap<String, Integer> ratingSystem = new HashMap<String, Integer>(); // Rating System
		
		ArrayList<Card> combinedCard = new ArrayList<Card>();
		combinedCard.addAll(potCards);
		combinedCard.addAll(handCards);   // Combine all hands
		
		int highValue = evalHighCard(combinedCard);
		ratingSystem.put("HighCard", highValue); // CHeck both pot and personal cards
		
		int highValuePersonal = evalHighCard(handCards);
		ratingSystem.put("PersonalHighCard", highValuePersonal); // Check his personal Hand
		
		HashMap<String, Integer> specialValue = evalSpecial(combinedCard);
		 //Merge the hasmaps
		
		ratingSystem.putAll(specialValue);
		
		
		return ratingSystem;
		
	} 
	
	public int evalHighCard(ArrayList<Card> cards) {
		int highCard = 0;
		for (Card currCard: cards) {
			int currNumber = currCard.number;
			if (currNumber == 0) {  // If the card is an Ace treat it as high.
				highCard = 14;
			}
			else if(currNumber > highCard) {
				highCard = currNumber;
			}
		}

		return highCard;
	}
	
	public HashMap<String, Integer> evalSpecial(ArrayList<Card> combinedCard) {
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
		
		HashMap<String, Integer> bestCombinations = new HashMap<String, Integer>();
		
		// Check for pairs
		
		HashMap<Integer, Integer> pairs = new HashMap<Integer, Integer>();
		for(Card card: combinedCard) {
			Integer pairsCurrValue = pairs.get(card.number);
			if(pairsCurrValue == null) {   // Find the pairs
				pairsCurrValue = 0;
			}
			pairs.put(card.number, pairsCurrValue + 1);
		}
		System.out.println(pairs);
		
		if(pairs.containsValue(4)) { // 4 of a kind, if four of a kind, a best combo of pairs not possible
			for(int i =15; i >0; i--) {
				if(pairs.containsKey(i)&& pairs.get(i)==4) {
					bestCombinations.put("FourOfAKind", i);
				}
			}
		}
		if (pairs.containsValue(3)) {     
			for(int i =15; i >0; i--) {
				if(pairs.containsKey(i) && pairs.get(i)==3) {
					bestCombinations.put("ThreeOfAKind", i);
				}
			}
			
		}
		else if (pairs.containsValue(2)) {
			List<Integer> cardNumbersPaired = new ArrayList<Integer>();
			for(int i =15; i >0; i--) {
				if(pairs.containsKey(i) && pairs.get(i)==2) {
					cardNumbersPaired.add(i);
					
				}
			}  
			
			//Problem arises with multiple pairs, if there is two pairs, I must represent them both
			// In Addition I need to represent this
			if(cardNumbersPaired.size() > 3) { //Get the two highest
				for(int i =0; i <15; i ++) {
					if(cardNumbersPaired.size() >2 & pairs.containsKey(i)) {
						cardNumbersPaired.remove(i);
					}
				}
			}
			
			Collections.sort(cardNumbersPaired, Collections.reverseOrder() );
			if(cardNumbersPaired.size() == 2) {
				bestCombinations.put("Pair1",cardNumbersPaired.get(0));
				bestCombinations.put("Pair2",cardNumbersPaired.get(1));
			}
			
			if(cardNumbersPaired.size() == 1) {
				bestCombinations.put("Pair", cardNumbersPaired.get(0));
			}
						
			
		}
		

		return bestCombinations;
		
	}
		
	

}
