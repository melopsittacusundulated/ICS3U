import java.io.*; 
import java.util.Scanner;
import java.util.Random;

/*
 * Name: Dorothy Lin
 * Date: 2023-22-12
 * Description: This is a console-based version of the popular card game Crazy 8's. Enjoy! :D
 * TO DO:
 * - UI; you win, you lose, "craxy 8s" title card, etc.
 * - methods
 */
public class Crazy8s {
	static String prevCard = "N0"; //create global variable to store previous card put down. This is the placeholder.
	public static void main(String[] args) throws IOException {
		File playerFile = new File("playerPoints.txt"); //create file to track  player's points for several rounds
		File CPUFile = new File("CPUPoints.txt"); //create file to track CPU's points for several rounds
		PrintWriter playerWriter = new PrintWriter("playerPoints.txt"); //create file writer for player points file
		PrintWriter CPUWriter = new PrintWriter("CPUPoints.txt"); //create file writer for player points file
		
		for (int i = 0; i < 10; i++) { //fill the first 10 lines of the file with 0s to indicate 0 rounds, points
			playerWriter.println("0");
			CPUWriter.println("0");
		}
		
		Scanner in = new Scanner(System.in); //create scanner
		
		String titleCard = "title card here";
		String userInput; //create user input storage variable
		int roundCounter = 1; //create variable to keep track of rounds
		int userPoints; //create variable to hold player's points
		int CPUPoints; //same for CPU

		//welcome user to game
		System.out.println("> Starting game...");
		System.out.println(titleCard); //show 'crazy 8's' title card to player
		System.out.println("> What's your name? (type your name or a favourite word!): ");
		String userName = in.next(); //get next word or name they they type in as user name
		System.out.println("> Welcome to CRAZY 8's, " + userName);
		showRules(); //show rules to user, start game
		
		boolean playAgain = true; // create boolean to facilitate whether ENTIRE game restarts or not
		
		while (playAgain == true) { //runs continuously for each round while user decides to play again.
			String[] drawDeck = new String[52]; //initiate draw deck array
			String[] playerDeck = new String[20]; //initiate player's deck, space for 20 cards (it is highly unlikely they will exceed over 20 cards without being able to put down one.)
			String CPUDeck[] = new String[20]; //initiate CPU's deck
			
			dealCards(drawDeck, playerDeck, CPUDeck); //deal cards from draw deck to players' decks
			
			boolean CPUWin = false; //reset player "winning" booleans to false so that game plays
			boolean playerWin = false;
			
			do {
				CPUsTurn(drawDeck, playerDeck, CPUDeck); //CPU goes first.
				CPUWin = verifyEmpty(CPUDeck); //check if cpu's deck is empty (they have won)
				if (CPUWin == true) { //if the CPU has won..
					System.out.println("> You LOST!\n Better luck next time!"); //print out that the player has lost
				} else { 
					playersTurn(drawDeck, playerDeck, CPUDeck, userName); //if CPU hasn't won, player's turn
					playerWin = verifyEmpty(drawDeck, playerDeck, CPUDeck); //check if player has won
				}
			} while (CPUWin && playerWin == false);
			
			prevCard = "N0"; //reset previous card to default placeholder
			if (playerWin == true) { //if the player won...
				System.out.println("> You WON!\n Great job, " + userName);//tell user they did a great job if they won
			}
			
			do { //create do-while loop to ensure user responds with yes or no
				System.out.println("> Would you like to play again? You will receive points for each round. (y/n): "); //Ask user if they want to play again
				 userInput = in.next(); //store user's input
				if (userInput.equals("y") && userInput.equals("n")) {
					System.out.println("> Please enter 'y' or 'n'");
				}
			} while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n"));
			
			if (userInput.equalsIgnoreCase("y")) { //if the user says yes, create round system
				String player = "p"; //initiate variable to pass onto point calculation method, to indicate whether CPU or player
				userPoints = calculatePoints(playerDeck, player); //calculate points from prev. rounds
				player = "c"; //set player to 'c' for CPU
				CPUPoints = calculatePoints(CPUDeck, player); //calculate points from prev rounds
				System.out.println("> New round starting...");
				roundCounter++; //increment round counter
			} else {
				playAgain = false; //tell program the player doesn't want to play anymore. Ends loop.
			}
		} //end of rounds/game loop
		
		if (roundCounter > 1) { //if more than one round was played, show overall winner
			System.out.println("> You played " + roundCounter + " rounds!"); //tell user how many rounds were played
			if (userPoints > CPUPoints) { //if user has more total points
				System.out.println("> Congratulations! You are the OVERALL winner, and beat the CPU by " + (userPoints - CPUPoints) + " points!");
			} else if (userPoints < CPUPoints) { //if user has less points than CPU
				System.out.println("> Unfortunately, you were not the OVERALL winner… You lost by " + (CPUPoints - userPoints) + " points!");
			} else {
				System.out.println("> You and the CPU were tied in OVERALL rounds, with " + userPoints + " points!");
			}
		}
		
		//thank player for playing, end game
		System.out.println("> Thanks for playing...");
		System.out.println(titleCard);
		System.out.println("> Game closing...\n END");
		
		playerWriter.close(); //close print-writers to save progress
		CPUWriter.close();
		in.close(); //close scanner
	}
	
	public static void showRules() { //this method asks the user if they want to see the rules, and prints them if necessary.
		Scanner in = new Scanner(System.in);
		System.out.println("> Would you like to see the rules? (y/n)");
		String userInput = in.next();
		if (userInput.equals("y")) {
			System.out.println("- - - - - - - - R U L E S- - - - - - - - ");
			System.out.println("Crazy 8’s is a card game in which the objective is for players to discard all of the cards from their hand. ");
			System.out.println("Players take turns discarding or picking up cards through their ability to match the rank or suit of the previous card placed down.");
			System.out.println("1. The user and CPU are each dealt 7 cards from a shuffled 52-card deck. The rest of the cards are put into the draw deck.");
			System.out.println("2. The CPU will place down the first card. From there, if the user has any cards of the same suit, rank, or an 8, they may choose to place them down. They MUST put down a card if they can.");
			System.out.println("3. If they do not have a card they can put down, they must take one from the draw deck.");
			System.out.println("4. They may discard the card they received from the draw deck if possible (if the top card has the same rank, suit, or the card just drawn is an 8). If not, the player’s turn ends.");
			System.out.println("5. The CPU and player will take turns doing this until one of them discards their entire hand; this person is the winner.");
			System.out.println("6. If the player wants to keep playing, they will be rewarded points so that an “overall” winner can be determined after all rounds conclude. The winner of each round is given 1000 points, while the loser’s points are determined by their leftover hand; number cards are worth 25 points, face cards are worth 50, and 8’s are worth 100.");
			System.out.println("7. There are 3 special cards other than the 8's... Play to find out!");
		}
		else {
			System.out.println("> Let's start, then!");
		}
		System.out.println("> Are you ready? (type anything to begin!)");
		userInput = in.next();
		System.out.println("> Game Beginning . . .");
		in.close();
	}
	
	public static void dealCards(String [] drawDeck, String [] player1, String [] player2) throws FileNotFoundException{ //prepares and deals cards to players
		Scanner scanner = new Scanner(new FileReader("cards.txt")); //create reader to get cards from file
		int x = 0; //initiate counter variable
		while (scanner.hasNext()) { //get all 52 cards from file, read them and put into draw deck
			drawDeck[x] = scanner.nextLine(); //put card on each line into file
			x++; //increment counter to change deck index
		}
		
		shuffle(drawDeck); //shuffle draw deck
		
		for (int i = 0; i < 7; i++) { //place 7 cards into each of the players' decks
			player1[i] = drawDeck[i];
			drawDeck[i] = "NONE"; //replace card given to player with empty slot
			player2[i+7] = drawDeck[i+7]; //simultaneously put next 7 cards into CPU's card
			drawDeck[i+7] = "NONE"; //replace given card with empty slot
		}
		scanner.close();
	}
	
	public static void shuffle(String[] deck) { //this method shuffles decks
		int n = deck.length; //get length of deck array 
		Random randomGen = new Random(); //create random gen
		for (int i = 0; i < (n * 5); i++) { //shuffles cards for 5 times the number of cards in array
			int rand1 = randomGen.nextInt(n); //generate a random card index
			int rand2 = randomGen.nextInt(n);
			String temp = deck[rand1]; //swap the cards at each index around
			deck[rand1] = deck[rand2];
			deck[rand2] = temp;
		}
	}
	
	public static void CPUsTurn(String[] drawDeck, String[] playerDeck, String[] CPUDeck) { //this method executes the CPU's turn
		Random randomGen = new Random(); //create new random generator
		System.out.println("It's the CPU's turn to play a card!");
		boolean validCard = false; //initiaite boolean to check if card played is valid
		String turn = "c"; //initiate turn variable to be passed into methods as 'c' for CPU
		String chosenCard; //declare variable to store user's chosen card
		
		do { //create loop that continues until CPU chooses valid card
			drawCheck(drawDeck, CPUDeck, turn); //pass decks into method that checks if card needs to be drawn
			System.out.println("The CPU is thinking...");
			int randNum = randomGen.nextInt(20); //choose a random card index from CPU's 20 cards
			chosenCard = CPUDeck[randNum]; //set chosen card to randomly chosen card
			String usage = "playCard"; //set variable to pass into validCard that tells usage of card (affects handling of card)
			validCard = validateCard(CPUDeck, chosenCard, turn, usage); //reset validCard boolean to check if chosen card can be played
		} while (validCard == false); 
		
		prevCard = chosenCard; //set card at top of played card deck to chosen card
		System.out.println("> The CPU has put down a [ " + chosenCard + " ], or a "); //tell user played card
		translateCard(chosenCard); //translate card to words
		cardCommands(drawDeck, playerDeck, CPUDeck, chosenCard, turn); //execute any commands for special cards
		System.out.println("> The CPU's turn is over.");
	}
	
	public static void playersTurn(String[] drawDeck, String[] playerDeck, String[] CPUDeck, String userName) { //this method executes the player's turn
		Scanner in = new Scanner(System.in); //create scanner for receiving user input
		boolean validCard = false;
		String turn = "p"; //initiate turn variable to be passed into methods as 'p' for player
		System.out.println("> Play a card, " + userName + "!"); //tell user its their turn
		String chosenCard; // declare variable for stored card
		
		do { //create loop to ensure card chosen is valid
			drawCheck(drawDeck, CPUDeck, turn); //check if draws need to happen
			System.out.println("> Choose a card to put down from your deck..."); //prompt user for card selection
			printDeck(playerDeck); //print player's deck
			chosenCard = in.next(); //set chosen card to input
			String usage = "playCard"; //set variable that tells method what the card will be used for
			validCard = validateCard(playerDeck, chosenCard, turn, usage); //check if card chosen is valid
		} while (validCard == false); //continue to ask user for card input until they enter something valid
		
		prevCard = chosenCard; //set card at top of played deck to chosen card
		System.out.println("> You put down a [ " + chosenCard + " }, or a"); 
		translateCard(chosenCard); //translate chosen card to words
		cardCommands(drawDeck, playerDeck, CPUDeck, chosenCard, turn); //do any special card actions if necessary
		System.out.println("> Your turn is over, " + userName + "."); //tell user their turn is over
	}
	
	public static boolean verifyEmpty(String[] deck) { //this method checks if a deck is empty
		boolean isEmpty = true; //set boolean that checks if deck is empty to true
		int n = deck.length; //get length of deck, store in variable for counted loop
		for (int i = 0; i < n; i++) { //for every card
			if (deck[i] != "NONE") { //if any one of the cards is not "NONE" (empty slot)
				isEmpty = false; //then the deck is not empty
				break; //leave loop as soon as there is one slot that isn't empty
			}
		}
		return isEmpty; //return whetehr or not the deck is empty
	}
	
	public static void drawCheck(String[] drawDeck, String[] deck, String turn) { //this method checks and carries out the withdrawal of cards from the draw deck
		boolean deckEmpty = verifyEmpty(drawDeck); //check if draw deck is empty
		int n = deck.length; //store length of deck passed in for loops
		String usage = "checkCard"; //set usage to be passed into validCard method as check-only
		boolean isValid = false; //set whether there is a card that can be played in player's deck or not
		while (isValid == false) { //continue to draw card if the player still doesn't have a valid card
			if (turn.equals("c")) { //if its the cpu's turn
				System.out.println("> CPU's turn has started...");
				for (int i = 0; i < n; i++) {
					isValid = validateCard(deck, deck[i], turn, usage); //check if any of the cards can be played
					if (isValid == true) { //if there is a valid card that can be played, leave loop
						break;
					}
				}
				if (isValid == false) { //if there are no valid cards
					if (deckEmpty == true) {
						System.out.println("> The CPU's turn was skipped; there are no cards in the draw deck.");
					} else { //but if the draw deck is not empty, draw cards
						shuffle(drawDeck); //shuffle draw deck
						System.out.println("> The CPU is drawing a card..."); //tell user the CPU is drawing a card
						String drawnCard; //declare variable for drawn card
						
						for (int i = 0; i < 52; i++) { //iterate through draw deck to find a card that can be drawn
							if (!drawDeck[i].equals("NONE")) { //if there is a card
								drawnCard = drawDeck[i]; //draw the card from the drawdeck
								drawDeck[i] = "NONE"; //set taken card to empty slot
								break; //leave loop
							}
						}
						for (int i = 0; i < n; i++) { //iterate through CPU's deck to find a place to put down the card
							if (!deck[i].equals("NONE")) { //if there is an empty spot
								deck[i] = drawnCard; //set empty spot to drawn card
								break; //leave loop
							}
						}
						System.out.println("> The CPU drew a card."); //tell user the CPU drew a card.
						
					}
					
				}
			} else if (turn.equals("p")) { //but if it is the player's turn (same thing)
				System.out.println("The player's turn has started...");
				for (int i = 0; i < n; i++) { 
					isValid = validateCard(deck, deck[i], turn, usage); //check if any of the cards can be played
					if (isValid == true) {
						break;
					}
				}
				if (isValid == false) {
					if (deckEmpty == true) {
						System.out.println("> Your turn was skipped. There are no cards in the draw deck.");
					} else {
						shuffle(drawDeck); //shuffle draw deck
						System.out.println("> You must draw a card!");
						String drawnCard; //declare variable for drawn card
						
						for (int i = 0; i < 52; i++) { //iterate through draw deck to find a card that can be drawn
							if (!drawDeck[i].equals("NONE")) { //if there is a card
								drawnCard = drawDeck[i]; //draw the card from the drawdeck
								drawDeck[i] = "NONE"; //set taken card to empty slot
								break; //leave loop
							}
						}
						for (int i = 0; i < n; i++) { //iterate through CPU's deck to find a place to put down the card
							if (!deck[i].equals("NONE")) { //if there is an empty spot
								deck[i] = drawnCard; //set empty spot to drawn card
								break; //leave loop
							}
						}
						System.out.println("> You drew a [ " + drawnCard + " ] !"); //tell user the card they drew
						translateCard(drawnCard); //translate card name to words
					}
				}
			}
		}
	}
	
	public static boolean validateCard(String[] deck, String chosenCard, String turn, String usage) { //this method validates whether a card can be played, according to the previous played card
		boolean isValid = false; //initiate value that decides if card is value to false
		boolean cardExists = false; //initiate boolean that ensures that card is in player's deck
		
		//verify if card exists in deck (if user-inputed)
		if (turn.equals("p")) { //if its the player's turn
			for (int i = 0; i < 20; i++) { //iterate through each card in their deck
				if (chosenCard.equals(deck[i])) { //if the card chosen is equal to one present in their deck
					cardExists = true; //the card exists
					break; //leave loop immediately
				}
			}
		} else { //if it's the CPU's turn, the card must exist because a card at a random index is passed in as chosenCard
			cardExists = true;
		}
		
		if (cardExists == true) { //if the card is present
			if (chosenCard.charAt(1) == '8') { //if the card is an 8, it can always be played
				return (isValid = true); //return that it is a valid card
			} else { //otherwise, check if card matches the suit or rank of the previously played card
				char validSuit = prevCard.charAt(0); //store the suit of the previous card played
				char validRank = prevCard.charAt(1); //store the rank of the previous card played
				
				if (validSuit == chosenCard.charAt(0) || validRank == chosenCard.charAt(1)) { //if either the suit or rank match
					if (usage.equals("playCard")) { //if the card is being played by either CPU or player, modify their deck
						for (int i = 0; i < 20; i++) {
							if (chosenCard.equals(deck[i])) {
								deck[i] = "NONE"; //replace the played card in the player's deck with an empty slot
							}
						} 
					} 
					return (isValid = true); //return that card can indeed be played 
				} else { //otherwise, if the card does not match either rank or suit
					if (turn.equals("p")) {
						System.out.println("> Please choose a card of the same rank, suit, or an 8");
					}
					return isValid; //return that the card is not valid
				}
			}
			
		}
	}
	
}