package playGame;
import java.util.ArrayList;
import java.util.HashMap;
import Cards.Card;


public class Player {
	int playerNumber;
	public Hand playerHand;
	int highCard;
	double balance;
	boolean allIn;
	boolean folded;
	public HashMap<String, Integer> ratingSystem;
	
	
	Player(int i){
		this.playerNumber = i;
		this.balance = 500; // Current Balance Startoff
		this.folded =false;
		
	}
	
	public void addHand(ArrayList<Card> incomingCards) { // Add Card when dealed
		this.playerHand = new Hand(incomingCards);
	}
	
	public String checkBet(double bidIntoPot) { 
		
		//Start with a basis Check to see if player is already All in
		
		if(this.allIn == true) {
			return "ALL IN";	
		}

		else if(this.balance - bidIntoPot > 0) {
			this.balance = this.balance - bidIntoPot; // If he has more money than the bet its good
			return "Okay";
		}
		
		else if(this.balance - bidIntoPot ==0 ){        // If the bet is all the money, he is all in
			this.balance = this.balance - bidIntoPot;
			this.allIn = true;
			return "ALL IN";
		}
		
		else {
			return "Retry"; // Insufficent Funds
		}
		
	}
	
	public String bet(double bidIntoPot) {        // Return the given String to check what should be done
		String resp = this.checkBet(bidIntoPot);
		return resp;
	}
	
	
	public HashMap<String, Integer> evaluateHand(ArrayList<Card> potCards) {
		HashMap<String, Integer> ratings = playerHand.evaluateHand(potCards);
		ratingSystem = ratings;
		return ratings;
		
	} 
	
	
	
}
