package playGame;
import java.util.ArrayList;
import java.util.InputMismatchException;
import Cards.Card;
import Cards.Deck;
import java.util.Scanner;


/**
 * Creates the game, with the main method primarly being used.
 * @author am_go
 *
 */
public class Play {
	private ArrayList<Player> allPlayers;
	private Deck playingDeck;
	private Board boardCards;
	private ArrayList<Player> winners;
	private int maxPlayerBet;
	private int potTotalCount;
	private int numberOfPlayersIn; //Players not folded.
	
	public Play() {
		this.allPlayers = new ArrayList<Player>();
	}
	public static void main(String[] args) {
		//Create A Whole New Game.
		Play game = new Play();
		
		// Displaying information about this game
		Scanner scnr = new Scanner(System.in);

		game.intro(scnr); // Used to determine the setting of the game.
		game.beginPlaying(scnr); // Begin the first full game

		scnr.close();
	}

	private void intro(Scanner scnr) {
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("Welcome to the Game of Poker! Here we Play by the Normal Rules and only aces are high");
		System.out.println("You can play from 2-10 Players (No CPUS)");
		
		//Take the number of Players
		this.numberOfPlayersIn = this.takeNumberPlayers(scnr); //These will stay constant throughtout all games
		// Add that amount of player
		this.addPlayers(this.numberOfPlayersIn);
		// Collect the names
		this.collectNames(scnr);
	}
	
	private void beginPlaying(Scanner scnr) {
		while(true) {
			this.playGame(scnr);
			while(true) {
				try {
					System.out.println("------------------------------------------------------------------------------");
					System.out.println("Would you like to continue   :");
					String input = scnr.nextLine();
					String inputUpper = input.toUpperCase();
					if(inputUpper.equals("Y")){
						this.reset();
						System.out.println("You have chosen to Play Again, Best of luck!");
						this.wait(3); //Wait a total of three seconds
						this.printSpace();
						break;
					}
					else if(inputUpper.equals("N")) {
						System.out.println("GoodBye!");
						
						return;
					}
					else {
						throw new InputMismatchException("Y or N");
					}
				}
				catch(InputMismatchException e) {
					System.out.println("Input a [y] for yes, and [n] for no.");
				}
	
			}
		}
	}
	
	private void collectNames(Scanner scnr) {
		for(Player player: allPlayers) {
			System.out.println("Hello Player " + player.getPlayerNumber());
			System.out.println("What would you like to be called? : ");
			String input = scnr.nextLine();
			player.setPlayerName(input);
		}
	}
	
	/**
	 * Adds a shuffled deck to the game
	 */

	private int takeNumberPlayers(Scanner scnr) {
		int numberOfPlayers;
		while(true) {
			System.out.println("How many Players would you like? : ");
			try{
				numberOfPlayers = scnr.nextInt();
				if(numberOfPlayers < 2 || numberOfPlayers>10) {
					throw new Exception("Input a Number between 2-10");
				}
				break;
			}
			catch(java.util.InputMismatchException e) {
				System.out.println("Insert a number only! Try Again");
				scnr.next();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return numberOfPlayers;
		
	}
	
	/**
	 * Adds the player to an ArrayList to preform round operations
	 * @param numberOfPlayers
	 */
	private void addPlayers(int numberOfPlayers) {
		for(int i = 1 ; i <numberOfPlayers + 1; i ++) {
			Player player = new Player(i); // Make the Initial Players
			allPlayers.add(player);
		}	
	}
	
	
	private void addDeck() {
		Deck playingDeck = new Deck();
		this.playingDeck = playingDeck;
	}
	
	

	/**
	 * Overall game method, 3 rounds per a game of poker, therefore, used a class to display space
	 * @param scnr user input passed to each specific round
	 */
	private void playGame(Scanner scnr) {
		this.addDeck();    // Add A deck
		this.dealCards();  // Deal the given cards from the deck
		
		//game.ante(); // No Ante; Optional component to add
		
		this.playSpecificRound(scnr);  //Play round 1
		
		
		
		if(!onlyOneRemaining()) {  //Play round two if not everyone is folded
			this.printSpecificSpace(5);
			System.out.println("TIME TO FLOP A CARD");
			this.flopCard();
			System.out.println("Round 2!"); 
			this.printSpecificSpace(5);
			this.playSpecificRound(scnr);
		}
		if(!onlyOneRemaining()) {
			this.printSpecificSpace(5);
			System.out.println("TIME TO FLOP A CARD");
			this.flopCard();
			this.printSpecificSpace(1);
			System.out.println("Round 3!");
			this.printSpecificSpace(5);
			this.playSpecificRound(scnr);
		}
		
		this.ending();
		
		this.printBalances();
	}
	

	

	/**
	 * A Specific round that people bet after seeing a card has flopped.
	 * @param scnr to deteremine a specific users action like Raise, Bet, Match, Fold, and Check
	 */
	private void playSpecificRound(Scanner scnr){
		int totalMatched= 0; // the total number of players must be the players left
		
		while(true) {
			for(Player currPlayer: allPlayers) {
				if(currPlayer.getFolded()) {
					continue; // Go to the next Player
				}
				//Printing for each person to allow a switch
				this.printHorBar();
				System.out.println("Please Change to " + currPlayer.toString());
				this.printHorBar();
				this.printSpecificSpace(1);
				this.wait(5); // Give Time to change player
				this.printSpace();		
				
				
				if(totalMatched == this.numberOfPlayersIn) {
					this.printSpecificSpace(5);
					System.out.println("END OF ROUND");
					
					return;
				}

				if(currPlayer.isAllIn()) {
					this.printHorBar();
					System.out.println("You are already all in!");
					System.out.println("Best of luck");
					continue;
				}
				

				this.printSpecificSpace(2);
				this.printHorBar();
				this.printPlayerInfo(currPlayer);
				this.printSpecificSpace(1);
				this.wait(3); // Wait for People To Switch
				
				//Now print the Display for the User to identify what to do.
				System.out.println("Cards:");
				System.out.println("        The cards in your hand are: " + currPlayer.getHand().getHandCards().toString());
				this.printBoard();
				System.out.println("Coins:");
				System.out.println("        Your current bet is at: " + currPlayer.getTotalBalanceIn());
				System.out.println("        The table bet is: " + this.maxPlayerBet);  
				System.out.println("        The pot total is: "+ this.potTotalCount);
				System.out.println("        Your total Balance is: " + currPlayer.getBalance());
				System.out.println("Moves:");
				System.out.println("        Press [r] to raise, Press [a] for all in, Press [m] to match the bet, Press [c] to check, and Press [f] to fold");
				System.out.println("Your Move:");
				while(true) {
					try {
						if(totalMatched >= numberOfPlayersIn) {
							return;
						}
						
						String input = scnr.nextLine();
						String inputUpper = input.toLowerCase();
						
						if(inputUpper.equals("r")) {
							System.out.println("The total amount bid by an individual is: " + maxPlayerBet);
							System.out.println("The total amount that you personally have bet is: " + currPlayer.getTotalBalanceIn());
							System.out.println("Note that your current balance is " + currPlayer.getBalance() + "!");
							System.out.println("How much would you like to raise the bid by from your current bid? :");
							
							int bet = scnr.nextInt(); //Take the bet and make sure it is an int and valid number.
							
							this.playerRaise(currPlayer, bet);
							
							totalMatched = 1;
							System.out.println("The current table bet (and by you) is: "+ maxPlayerBet);
							break;
						}
						else if(inputUpper.equals("c")){
							int betDifference = maxPlayerBet - currPlayer.getTotalBalanceIn();
							if(betDifference > 0) {
								throw new Exception("You cannot check, you need to match for: " + betDifference + " more coins or fold.");
							}
							
							System.out.println("You have elected to check");
							totalMatched = totalMatched + 1;
							break;
						}
						else if(inputUpper.equals("a")) {
							int newCurrPlayerTotalIn = currPlayer.getBalance() + currPlayer.getTotalBalanceIn();
							
							if(newCurrPlayerTotalIn < this.maxPlayerBet) {
								totalMatched = totalMatched +1;
							}
							else if(newCurrPlayerTotalIn == this.maxPlayerBet) {
								totalMatched = totalMatched +1;
							}
							else { // newCurrPlayerTotalIn > maxPlayerBet
								totalMatched = 1;
								this.maxPlayerBet = newCurrPlayerTotalIn;
							}
							System.out.println("You are going all in! Best of Luck");
							potTotalCount = potTotalCount + currPlayer.getBalance(); //Working on adding an all in.
							
							currPlayer.goAllIn();

							
							break;
						}
						else if(inputUpper.equals("m")) {
							//Find the difference to their current bet and the max bid at the moment.
							System.out.println("You are trying to match with a total before in of " + currPlayer.getTotalBalanceIn());
							
							int remainingToBid = maxPlayerBet - currPlayer.getTotalBalanceIn();
							currPlayer.bet(remainingToBid); //Must Match the amount
							
							totalMatched = totalMatched +1;
							this.potTotalCount = this.potTotalCount + remainingToBid;
							System.out.println("You are at a total of " + currPlayer.getTotalBalanceIn() + " in.");
							break; //Change Player
							
						}
						else if(inputUpper.equals("f")) {
							this.numberOfPlayersIn = this.numberOfPlayersIn - 1;
							currPlayer.setFolded(true);
							break;
						}

					}
					catch(InputMismatchException e) {
						System.out.println("Insert a number only! Try Again");
					}
					catch(Exception e) {
						System.out.println(e.getMessage());
					}

				}
					
			}
		}
	}
	

	private void playerRaise(Player player, int bet) throws Exception{
		if(bet <0 || bet > player.getBalance()) {
			throw new Exception("Must Be A valid number, Try Again.");
		}
		int totalBetCount = bet + player.getTotalBalanceIn();
		if(totalBetCount < this.maxPlayerBet) {
			throw new Exception("You must raise above the table Bet");
		}

		player.bet(bet); //The player makes the bet and the amount is subtracted from his balance
		
		this.potTotalCount = this.potTotalCount + bet; // The pot grows by the bet if it is valid
		this.maxPlayerBet = totalBetCount; //The amount over each round is incremented for the total in the pot per person.
	}
	
	private boolean onlyOneRemaining() {
		if(numberOfPlayersIn ==1) {
			this.printSpecificSpace(1);
			System.out.println("Everyone else has folded so there is only one winner now!");
			return true;
		}
		return false;
	}
	
	private void flopCard() {
		this.boardCards.flop();
	}
	


	
	private void dealCards() {
		
		//Deal the cards to the players
		for(Player playerAtTable: allPlayers) {
			ArrayList<Card> cardsToAdd = new ArrayList<Card>(); // Deal Two Cards to each Player
			for(int addCard = 0; addCard < 2 ;  addCard++) {
				cardsToAdd.add(playingDeck.getFullDeck().get(0));   // Deal 2 cards
				playingDeck.getFullDeck().remove(0);  // Always get the first card since its Shuffled
			}
			playerAtTable.addHand(cardsToAdd);
		}
		
		// Add a board
		ArrayList<Card> potCards = new ArrayList<Card>();
		for(int potCard = 0; potCard < 5; potCard++) {
			potCards.add(playingDeck.getFullDeck().get(0));
			playingDeck.getFullDeck().remove(0);
		}
		boardCards = new Board(potCards);
		this.printSpecificSpace(1);
		System.out.println("The cards have been dealt and the game can begins!");
		this.printSpecificSpace(1);
		this.wait(2);

		
	}
	
	private void evaluateHands(ArrayList<Card> cardsFromPot){

		for(Player currPlayer: allPlayers) {
			//System.out.println("Player " + currPlayer.getPlayerNumber() + " :" +currPlayer.getHand().getHandCards());
			currPlayer.setRating(cardsFromPot);
			//System.out.println("Rating " + currPlayer.getPlayerNumber() + ":" +currPlayer.getRating());
			
		}

	}
	//Return the index of each winner.
	private void setWinners() {
		ArrayList<Player> winners = new ArrayList<Player>();
		Player currWinner = null;
		for(Player currPlayer: this.allPlayers) {
			System.out.println(currPlayer.getFolded() + " " + currPlayer.getPlayerName());
			if(currPlayer.getFolded()) {
				continue;
			}
			if(currWinner == null) {
				currWinner = currPlayer; // The First person is always a winner.
				winners.add(currPlayer); //Test CASE! COMPARE HIMSELF TO HIMSELF
			}
			else {
				int compValue = currWinner.comparePlayer(currPlayer);
				if(compValue == -1) {
					currWinner = currPlayer; // If the Current winner is worse, we have a new winner
					winners.clear();
					winners.add(currPlayer);
				}
				else if(compValue == 0) { //They are equal and will split the pot.
					winners.add(currPlayer);
				}
				//Otherwise, the currWinner is actually better so continue.
			}
		}
		this.winners = winners;
	}
	
	private ArrayList<Player> getWinners(){
		return this.winners;
	}

	
	private void ending() {
		this.printSpace();
		System.out.println("                                       All the bids are made and this round is over");
		System.out.println("                                                Let us show our hands!");
		System.out.println("Board:");
		System.out.println("                                " + this.boardCards.getAllCards());
		
		for(Player remainingPlayer: this.allPlayers) {
			if(remainingPlayer.getFolded()) {
				continue;
			}
			System.out.println(remainingPlayer.getPlayerName() + " had these cards:");
			System.out.println("                                   " +remainingPlayer.getHand().getHandCards().toString());
		}
		this.printHorBar();
		this.evaluateHands(this.boardCards.getAllCards());
		this.setWinners(); //Setting winners when they win by a fold is something to do!
		this.dispersePot();
		
		if(this.winners.size() >1) {
			this.wait(2);
			System.out.println("The winners are ......");
			this.wait(2);
			System.out.println();
			for(Player winner: this.winners) {
				System.out.println(winner.getPlayerName());
			}
		}
		else {
			this.wait(2);
			System.out.println("The winner is ......");
			this.wait(2);
			System.out.println(this.getWinners().get(0).getPlayerName()); // Print Multiple Winners THOUGH
		}
		
		System.out.println("Nice Round!");
		this.printSpace();

	}
	
	private void dispersePot() {
		ArrayList<Player> winners = this.getWinners();
		int amountWinners = winners.size();
		int amountEachWon = Math.floorDiv(potTotalCount, amountWinners);
		for(int i = 0; i < amountWinners ; i++) {
			winners.get(i).acceptWinnings(amountEachWon);
		}
	}
	

	
	private void reset() {
		//If their Balance is zero, remove the player
		ArrayList<Integer> playersToRemove = new ArrayList<Integer>();
		for(int i = 0; i < allPlayers.size(); i ++) {
			if(allPlayers.get(i).getBalance() <= 0) {
				playersToRemove.add(i);
			}
			else {
				allPlayers.get(i).setFolded(false);
				allPlayers.get(i).setAllIn(false);
				allPlayers.get(i).clearTotalBalanceIn();
			}
		}
		
		if(playersToRemove.size() >0) {
			for(Integer idxRemove: playersToRemove) {
				int playerToRemove = idxRemove; //Unboxing to ensure no imcompatbiltiies between the overloaded method.
				allPlayers.remove(playerToRemove);
			}
		}
		
		this.maxPlayerBet = 0;
		this.potTotalCount = 0;
		this.numberOfPlayersIn = allPlayers.size();
		
	}
	
	
	
	
	
	//Auxiliary Functions for aesthetic Purposes
	
	private void wait(int seconds) {
		try{
			int x = seconds; // Wait for x seconds
		    Thread.sleep(1000 * x);  
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
	}
	
	//Print Function to facilitate the console.
	
	private void printBalances() {
		this.printSpace();
		System.out.println("                                    Balances");
		for(Player player: allPlayers) {
			System.out.println(player.getPlayerName() + " is at " + player.getBalance());
		}
	}
	
	private void printBoard() {
		System.out.println("        The Cards on the Board are: " +boardCards.getFloppedCards() + "[Hidden, Hidden]");
		int remaining = 5 - boardCards.getFloppedCards().size();
		System.out.println("        There are still " + remaining + " rounds");
	}
	
	private void printPlayerInfo(Player currPlayer) {
		System.out.println("Hello " + currPlayer.getPlayerName());
	}
	
	private void printSpace() {
		final int DEFAULT_SPACES = 10;
		this.printSpecificSpace(DEFAULT_SPACES);
	}
	
	private void printSpecificSpace(int totalSpaces) {
		for(int i = 0; i < totalSpaces; i++) {
			System.out.println();
		}
	}
	
	private void printHorBar() {
		System.out.println("---------------------------------------------------------------------------------");
	}

}

