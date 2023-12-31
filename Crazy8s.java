import java.io.*;
import java.util.Scanner;
import java.util.Random;

/*
 * Name: Dorothy Lin
 * Date: 2023-22-12
 * Description: This is a console-based version of the popular card game Crazy 8's. Enjoy! :D
 */
public class Crazy8s {
	static String prevCard = "  "; //create global variable to store previous card put down. This is the placeholder.
	static int roundCounter = 0; //create variable to keep track of rounds
	static boolean firstTurn; //declare boolean for first turn to be used across several classes
	static boolean cantDrawUser = false; //create boolean to track whether player cant draw when it needs to
	static boolean cantDrawCPU = false; //create boolean to track whether CPU can't draw when it needs to; skips if necessary
	
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		
		String titleCard = "   ______  _______          _       ________  ____  ____     ____   _   ______   \n" +
" .' ___  ||_   __ \\        / \\     |  __   _||_  _||_  _|  .' __ '.| |.' ____ \\  \n" +
"/ .'   \\_|  | |__) |      / _ \\    |_/  / /    \\ \\  / /    | (__) |\\_|| (___ \\_| \n" +
"| |         |  __ /      / ___ \\      .'.' _    \\ \\/ /     .`____'.    _.____`.  \n" +
"\\ `.___.'\\ _| |  \\ \\_  _/ /   \\ \\_  _/ /__/ |   _|  |_    | (____) |  | \\____) | \n" +
" `.____ .'|____| |___||____| |____||________|  |______|   `.______.'   \\______.' \n"; //text from fancytextpro.com
		
		String winCard = "                            ____      ____   ___   ____  _____  _  \n" +
"                           |_  _|    |_  _|.'   `.|_  \\|_   _| | | \n" +
"  _   __   .--.   __   _   \\ \\ /\\ / / /  .-.\\ |  \\ | |   | | \n" +
" [ \\ [  ]/ .'`\\ \\[  | | |     \\ \\/  \\/ /  | |   | | | |\\ \\| |  | | \n" +
"  \\ '/ / | \\__. | | \\_/ |,     \\  /\\  /   \\  `-'  /_| |_\\   |_ |_| \n" +
"[\\_:  /   '.__.'  '.__.'_/      \\/  \\/     `.___.'|_____|\\____|(_) \n" +
" \\__.'                                                             "; //text from fancytextpro.com
String loseCard = "                                                                                                                 \n" +
" ___  ___  ___   .--.    ___ .-. .-.      .-..      ___  ___  ___   .--.    ___ .-. .-.      .-..               \n" +
"(   )(   )(   ) /    \\  (   )   '   \\    /    \\    (   )(   )(   ) /    \\  (   )   '   \\    /    \\              \n" +
" | |  | |  | | |  .-. ;  |  .-.  .-. ;  ' .-,  ;    | |  | |  | | |  .-. ;  |  .-.  .-. ;  ' .-,  ;             \n" +
" | |  | |  | | | |  | |  | |  | |  | |  | |  . |    | |  | |  | | | |  | |  | |  | |  | |  | |  . |             \n" +
" | |  | |  | | | |  | |  | |  | |  | |  | |  | |    | |  | |  | | | |  | |  | |  | |  | |  | |  | |             \n" +
" | |  | |  | | | |  | |  | |  | |  | |  | |  | |    | |  | |  | | | |  | |  | |  | |  | |  | |  | |             \n" +
" | |  ; '  | | | '  | |  | |  | |  | |  | |  ' |    | |  ; '  | | | '  | |  | |  | |  | |  | |  ' |  .-.   .-.  \n" +
" ' `-'   `-' ' '  `-' /  | |  | |  | |  | `-'  '    ' `-'   `-' ' '  `-' /  | |  | |  | |  | `-'  ' (   ) (   ) \n" +
"  '.__.'.__.'   `.__.'  (___)(___)(___) | \\__.'      '.__.'.__.'   `.__.'  (___)(___)(___) | \\__.'   `-'   `-' \n" +
"                                        | |                                                | |                   \n" +
"                                       (___)                                              (___)                  "; //text from fancytextpro.com, 'sweet'

		//clear point files in case game has been run more than once
		String playerPointsFile = "C:\\Users\\dorothy\\Documents\\VSCode_for_Java\\\\ICS3U\\playerPoints.txt";
		String CPUPointsFile = "C:\\Users\\dorothy\\Documents\\VSCode_for_Java\\ICS3U\\Crazy8s\\CPUPoints.txt";
		clearFile(playerPointsFile);
		clearFile(CPUPointsFile);

		int userPoints = 0; //create variable to hold player's points
		int CPUPoints = 0; //same for CPU
		
		//welcome user to game
		System.out.println("> Starting game...");
		System.out.println(titleCard); //show 'crazy 8's' title card to player
		System.out.println("> What's your name? (type your name or a favourite word!): ");
		String userName = in.next(); //get next word or name they they type in as user name
		System.out.println("> Welcome to CRAZY 8's, " + userName + "!");
		showRules(in); //show rules to user, start game, pass in scanner

		boolean playAgain = true; // create boolean to facilitate whether ENTIRE game restarts or not

		while (playAgain == true) { //runs continuously for each round while user decides to play again.
			String[] drawDeck = new String[52]; //initiate draw deck array
			String[] playerDeck = new String[20]; //initiate player's deck, space for 20 cards (it is highly unlikely they will exceed over 20 cards without being able to put down one.)
			String CPUDeck[] = new String[20]; //initiate CPU's deck
			String userInput; //create user input storage variable

			dealCards(drawDeck, playerDeck, CPUDeck); //deal cards from draw deck to players' decks
			
			boolean CPUWin = false; //reset player "winning" booleans to false so that game plays
			boolean playerWin = false;
			firstTurn = true; //set flag for CPU to put down any card on first turn
			
			do { //start loop for each round
				CPUsTurn(drawDeck, playerDeck, CPUDeck, in); //CPU goes first.
				CPUWin = verifyEmpty(CPUDeck); //check if cpu's deck is empty (they have won)
				if (CPUWin == true) { //if the CPU has won..
					System.out.println(loseCard + "\n> The CPU placed down all their cards before you... you lost!"); //print loser card
					System.out.println("> Better luck next time!"); //print out that the player has lost
				} else { 
					playersTurn(drawDeck, playerDeck, CPUDeck, userName, in); //if CPU hasn't won, player's turn
					playerWin = verifyEmpty(playerDeck); //check if player has won
				}
			} while (!(CPUWin || playerWin)); //continue loop while neither have won
			
			prevCard = "N0"; //reset previous card to default placeholder
			if (playerWin == true) { //if the player won...
				System.out.println(winCard); //print winner card
				System.out.println("> Great job, " + userName + "! You placed down all your cards before the CPU!");//tell user they did a great job if they won
			}
			
			do { //create do-while loop to ensure user responds with yes or no
				System.out.println("> Would you like to play again? You will receive points for each round. (y/n): "); //Ask user if they want to play again
				 userInput = in.next(); //store user's input
				if (userInput.equals("y") && userInput.equals("n")) {
					System.out.println("> Please enter 'y' or 'n'");
				}
			} while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n"));
			
			if (userInput.equalsIgnoreCase("y")) { //if the user says yes, create round system
				
				System.out.println("> New round starting...");
				roundCounter++; //increment round counter
			} else {
				playAgain = false; //tell program the player doesn't want to play anymore. Ends loop.
			}
			String player = "p"; //initiate variable to pass onto point calculation method, to indicate whether CPU or player
			userPoints = calculatePoints(playerDeck, player); //calculate points from prev. rounds
			player = "c"; //set player to 'c' for CPU
			CPUPoints = calculatePoints(CPUDeck, player); //calculate points from prev rounds
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
		System.out.println("> Game closing...\nEND");
		in.close(); //close scanner
	}
	
	public static void showRules(Scanner in) { //this method asks the user if they want to see the rules, and prints them if necessary.
		String userInput = ""; //initiate user input to do do-while loop for error catching
		do {
			System.out.println("> Would you like to see the rules? (y/n): ");
			userInput = in.next();
		} while (!userInput.equals("y") && !userInput.equals("n")); //continue to ask until user inputs either yes or no
		
		if (userInput.equals("y")) {
			System.out.println("                       - - - - - - - - R U L E S- - - - - - - -                        \n");
			System.out.println("Crazy 8's is a card game in which the objective is for players to discard all of the cards from their hand. ");
			System.out.println("Players take turns discarding or picking up cards through their ability to match the rank or suit of the previous card placed down.\n");
			System.out.println("1. The user and CPU are each dealt 7 cards from a shuffled 52-card deck. The rest of the cards are put into the draw deck.");
			System.out.println("2. The CPU will place down the first card. From there, if the user has any cards of the same suit, rank, or an 8, they may choose to place them down. They MUST put down a card if they can.");
			System.out.println("3. If they do not have a card they can put down, they must take one from the draw deck.");
			System.out.println("4. They may discard the card they received from the draw deck if possible (if the top card has the same rank, suit, or the card just drawn is an 8). If not, the player’s turn ends.");
			System.out.println("5. The CPU and player will take turns doing this until one of them discards their entire hand; this person is the winner.");
			System.out.println("6. If the player wants to keep playing, they will be rewarded points so that an “overall” winner can be determined after all rounds conclude. The winner of each round is given 500 points, while the loser’s points are determined by their leftover hand; number cards are worth 25 points, face cards are worth 50, and 8’s are worth 100.");
			System.out.println("7. There are 3 special cards other than the 8's... Play to find out!");
			System.out.println("> Please note that 0s in number cards (Ex. S0, ten of spades) means the 'ten' card.");
		}
		else {
			System.out.println("> Let's start, then!");
		}
		System.out.println("> Are you ready? (type anything to begin!)");
		userInput = in.next();
		System.out.println("> Game Beginning . . .\n");
		in.nextLine(); //clear newline character
	}
	
	public static void dealCards(String [] drawDeck, String [] player1, String [] player2) throws IOException{ //prepares and deals cards to players
		String cardsFile = ("C:\\Users\\dorothy\\Documents\\VSCode_for_Java\\ICS3U\\Crazy8s\\cards.txt"); //file path for cards "H:\\ICS3\\ICS3U\\src\\FinalProject\\cards.txt"
		Scanner scanner = new Scanner(new File(cardsFile)); //create reader to get cards from file
		int x = 0; //initiate counter variable
		while (scanner.hasNext()) { //get all 52 cards from file, read them and put into draw deck
			drawDeck[x] = scanner.nextLine(); //put card on each line into file
			x++; //increment counter to change deck index
		}
		
		shuffle(drawDeck); //shuffle draw deck
		
		for (int i = 0; i < 7; i++) { //place 7 cards into each of the players' decks
			player1[i] = drawDeck[i];
			drawDeck[i] = "NONE"; //replace card given to player with empty slot
			player2[i] = drawDeck[i+7]; //simultaneously put next 7 cards into CPU's card
			drawDeck[i+7] = "NONE"; //replace given card with empty slot
		}
		
		for (int i = 19; i >= 7; i--) { //replace empty slots with "NONE"
			player1[i] = "NONE";
			player2[i] = "NONE";
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
	
	public static void CPUsTurn(String[] drawDeck, String[] playerDeck, String[] CPUDeck, Scanner in) { //this method executes the CPU's turn
		Random randomGen = new Random(); //create new random generator
		System.out.println("It's the CPU's turn to play a card!");
		System.out.println("The CPU is thinking...\n");
		boolean validCard = false; //initiaite boolean to check if card played is valid
		String turn = "c"; //initiate turn variable to be passed into methods as 'c' for CPU
		String chosenCard = ""; //initiate variable to store user's chosen card

		do { //create loop that continues until CPU chooses valid card
			drawCheck(drawDeck, CPUDeck, turn); //pass decks into method that checks if card needs to be drawn
			if (cantDrawCPU == true) { //if the CPU needs to draw a card but the deck is empty, skip turn
				validCard = true; //breaks out of validation loop
			} else {
				int randNum = randomGen.nextInt(20); //choose a random card index from CPU's 20 cards
				chosenCard = CPUDeck[randNum]; //set chosen card to randomly chosen card
				if (firstTurn == true) { //allow CPU to put down any card for first turn
					if (!chosenCard.equals("NONE")) { //allow any card that isnt empty
						validCard = true;
					} 
				} else {
					String usage = "playCard"; //set variable to pass into validCard that tells usage of card (affects handling of card)
					validCard = validateCard(CPUDeck, chosenCard, turn, usage); //reset validCard boolean to check if chosen card can be played
				}
			}
			
		} while (validCard == false); 
		
		firstTurn = false; //set first turn flag to false once CPU has gone
		if (cantDrawCPU == false) {//if the cpu was able to have a turn, set prevcard and print messages
			prevCard = chosenCard; //set card at top of played card deck to chosen card
			discard(CPUDeck, chosenCard); //discards played card
			System.out.println("> The CPU has put down a [ " + chosenCard + " ], or a "); //tell user played card
			translateCard(chosenCard); //translate card to words
			cardCommands(drawDeck, playerDeck, CPUDeck, chosenCard, turn, in); //execute any commands for special cards
		}
		System.out.println("> The CPU's turn is over.");
		cantDrawCPU = false; //reset ability to draw for next turn

	}
	
	public static void playersTurn(String[] drawDeck, String[] playerDeck, String[] CPUDeck, String userName, Scanner in) { //this method executes the player's turn
		boolean validCard = false;
		String turn = "p"; //initiate turn variable to be passed into methods as 'p' for player
		System.out.println("\n> Your turn has started! \n> Play a card, " + userName + "!"); //tell user its their turn
		String chosenCard = ""; // initiate variable for stored card

		do { //create loop to ensure card chosen is valid
			drawCheck(drawDeck, playerDeck, turn); //check if draws need to happen, execute if needed
			if (cantDrawUser == true) { //if user can't draw but needs to skip turn
				validCard = true; //leave validation loop
			} else {
				printDeck(playerDeck); //print player's deck
				System.out.println("> Remember; 8's can be put down at any time! You will be forced to play your 8 if it is the only valid card you can play.");
				System.out.println("> Choose a card to put down from your deck. The top card is [ " + prevCard + " ] : "); //prompt user for card selection
				chosenCard = in.nextLine();
				String usage = "playCard"; //set variable that tells method what the card will be used for
				validCard = validateCard(playerDeck, chosenCard, turn, usage); //check if card chosen is valid
			}
		} while (validCard == false); //continue to ask user for card input until they enter something valid
		
		if (cantDrawUser == false) { //set card and messages if player has gone
			prevCard = chosenCard; //set card at top of played deck to chosen card
			discard(playerDeck, chosenCard); //discards played card
			System.out.println("> You put down a [ " + chosenCard + " }, or a"); 
			translateCard(chosenCard); //translate chosen card to words
			cardCommands(drawDeck, playerDeck, CPUDeck, chosenCard, turn, in); //do any special card actions if necessary
		}

		System.out.println("> Your turn is over, " + userName + "."); //tell user their turn is over
		cantDrawUser = false; //reset ability to not draw cards when needed
	}
	
	public static boolean verifyEmpty(String[] deck) { //this method checks if a deck is empty
		boolean isEmpty = true; //initiate boolean that checks if deck is empty to true
		int n = deck.length; //get length of deck, store in variable for counted loop
		for (int i = 0; i < n; i++) { //for every card
			if (!deck[i].equals("NONE")) { //if any one of the cards is not "NONE" (empty slot)
				return (isEmpty = false); //then the deck is not empty
			}
		}
		return isEmpty; //return whetehr or not the deck is empty
	}
	
	public static void discard(String[] deck, String chosenCard) { //this method discards a played card
		for (int i = 0; i < 20; i++) {
			if (chosenCard.equals(deck[i])) {
				deck[i] = "NONE";
			}
		}
	}
	public static void drawCheck(String[] drawDeck, String[] deck, String turn) { //this method checks and carries out the withdrawal of cards from the draw deck
		boolean deckEmpty = verifyEmpty(drawDeck); //check if draw deck is empty
		if (firstTurn == false) { //if it isn't the first turn, check if any cards need to be picked up. otherwise, ignore b/c CPU will put down first card, no cards need to be drawn
			int n = deck.length; //store length of deck passed in for loops
			String usage = "checkCard"; //set usage to be passed into validCard method as check-only
			boolean isValid = false; //set whether there is a card that can be played in player's deck or not
			while (isValid == false) { //continue to draw card if the player still doesn't have a valid card
				if (turn.equals("c")) { //if its the cpu's turn
					for (int i = 0; i < n; i++) {
						isValid = validateCard(deck, deck[i], turn, usage); //check if any of the cards can be played
						if (isValid == true) { //if there is a valid card that can be played, leave loop
							break;
						}
					}
					if (isValid == false) { //if there are no valid cards
						if (deckEmpty == true) {
							System.out.println("> The CPU's turn was skipped; there are no cards in the draw deck.");
							cantDrawCPU = true; //set the inability of the CPU to draw cards if needed as true
							break; //leave loop to avoid infinitely printing message
						} else { //but if the draw deck is not empty, draw cards
							shuffle(drawDeck); //shuffle draw deck
							System.out.println("> The CPU is drawing a card...\n"); //tell user the CPU is drawing a card
							String drawnCard = ""; //declare variable for drawn card
							
							for (int i = 0; i < 52; i++) { //iterate through draw deck to find a card that can be drawn
								if (!drawDeck[i].equals("NONE")) { //if there is a card
									drawnCard = drawDeck[i]; //draw the card from the drawdeck
									drawDeck[i] = "NONE"; //set taken card to empty slot
									break; //leave loop
								}
							}
							for (int i = 0; i < n; i++) { //iterate through CPU's deck to find a place to put down the card
								if (deck[i].equals("NONE")) { //if there is an empty spot
									deck[i] = drawnCard; //set empty spot to drawn card
									break; //leave loop
								}
							}
							System.out.println("> The CPU drew a card."); //tell user the CPU drew a card.
							deckEmpty = verifyEmpty(drawDeck); //reverify if draw deck is empty
						}
						
					}
				} else if (turn.equals("p")) { //but if it is the player's turn (same thing)
					for (int i = 0; i < n; i++) { 
							isValid = validateCard(deck, deck[i], turn, usage); //check if any of the cards can be played
							if (isValid == true) {
								break;
							}
						}
					if (isValid == false) {
						if (deckEmpty == true) {
							System.out.println("> Your turn was skipped. There are no cards in the draw deck.");
							cantDrawUser = true; //set the inability of the player to draw cards if needed as true
							break; //leave immediately to avoid looping message
						} else {
							shuffle(drawDeck); //shuffle draw deck
							System.out.println("> You must draw a card!");
							String drawnCard = ""; //declare variable for drawn card
							
							for (int i = 0; i < 52; i++) { //iterate through draw deck to find a card that can be drawn
								if (!drawDeck[i].equals("NONE")) { //if there is a card
									drawnCard = drawDeck[i]; //draw the card from the drawdeck
									drawDeck[i] = "NONE"; //set taken card to empty slot
									break; //leave loop
								}
							}
							for (int i = 0; i < n; i++) { //iterate through CPU's deck to find a place to put down the card
								if (deck[i].equals("NONE")) { //if there is an empty spot
									deck[i] = drawnCard; //replace empty spot with drawn card
									break; //leave loop
								}
							}
							deckEmpty = verifyEmpty(drawDeck); //reverify if draw deck is empty
							System.out.println("> You drew a [ " + drawnCard + " ] , or a "); //tell user the card they drew
							translateCard(drawnCard); //translate card name to words
						}
					}
				}
			}
		} 
	}
	
	public static boolean validateCard(String[] deck, String chosenCard, String turn, String usage) { //this method validates whether a card can be played, according to the previous played card
		boolean isValid = false; //initiate value that decides if card is value to false
		boolean cardExists = false; //initiate boolean that ensures that card is in player's deck
		
		if (chosenCard.equals("NONE")) { //cpu may hand in empty card slot, ensure it does not pass validation
			return isValid; //return that the card is not valid
		}
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
			if (usage.equals("checkCard") || usage.equals("playCard")) { //if the card is either be played or checked if it can be played, check here:
				if (chosenCard.charAt(1) == '8') { //if the card is an 8, it can always be played
					if (usage.equals("playCard")) { //if the card is being played by either CPU or player, modify their deck
							for (int i = 0; i < 20; i++) {
								if (chosenCard.equals(deck[i])) {
									deck[i] = "NONE"; //replace the played card in the player's deck with an empty slot
								}
							} 
					}
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
						if (turn.equals("p") && !usage.equals("checkCard")) { //prints message for a card that can't be played only if player's turn and the card isn't just being checked
							System.out.println("\n> Please choose a card of the same rank, suit, or an 8"); //tell user to choose a card that matches prevcard
						}
						return isValid; //return that the card is not valid
					}
				} 
			} else { //if the card passed in is just  being checked if it exists (not rank or suit), return true
				return (isValid = true);
			}
			
		} 
		
		if (!usage.equals("checkExists")) { //print message if card chosen by user or cpu doesn't exist. this will only happen with user input
			System.out.println("\n> Please choose a valid card from your deck...\n"); //tells player to choose a valid card; no conditional needed for player b/c this will only happen for player input
			return isValid; //return that card is not valid
		} else {
			return isValid;
		}
	}
	
	public static void cardCommands(String[] drawDeck, String[] playerDeck, String[] CPUDeck, String chosenCard, String turn, Scanner in) { //this method executes any special cards' commands
		boolean emptyDeck = verifyEmpty(drawDeck); //check if draw deck is empty before any commands are played
		
		if (chosenCard.equals("HQ")) { //if a queen of cards is played, the other player must draw a card
			if (emptyDeck == true) { //if the draw deck is empty
				System.out.println("> <3 The QUEEN of HEARTS is not effective; there are no cards in the draw deck! <3");		
			} else { //otherwise, if the deck is not empty, continue to draw card
				
				if (turn.equals("p")) { //if it is the player's turn, give special message
					System.out.println("> <3 Your QUEEN of HEARTS forced the CPU to pick up a card! <3");
				} else { //send different message if CPU's turn
					System.out.println("> <3 The CPU’s QUEEN of HEARTS forced you to pick up a card! <3");
				}
				String drawnCard = ""; //initiate variable for drawn card
				for (int i = 0; i < 52; i++) { //go through every card in deck until there is one that isn't empty, take that card.
					if (!drawDeck[i].equals("NONE")) {
						drawnCard = drawDeck[i]; //draw card
						drawDeck[i] = "NONE"; //replace drawn card with empty slot
						break; //leave loop immediately
					}
				}
				for (int i = 0; i < 20; i++) {
					if (turn.equals("p")) { //if it is the player's turn, put the drawn card into the CPU's deck
						if (CPUDeck[i].equals("NONE")) {
							CPUDeck[i] = drawnCard;
							break; //leave loop immediately after replacing
						}
					} else { //if CPU's turn, put card into player's deck
						if (playerDeck[i].equals("NONE")) {
							playerDeck[i] = drawnCard;
							break;
						}
					}

				} 
				if (turn.equals("c")) { //if CPU's turn (made player pick up), then tell user the card they picked up
					System.out.println("> You picked up a [ " + drawnCard + " ] , or a ");
					translateCard(drawnCard);
				}
				
			}
			
		} else if (chosenCard.equals("S7")) { // if the card is a 7 of spades, it will shuffle the draw deck
			if (emptyDeck == true) { //if the draw deck is empty, say that it cannot be shuffled
				System.out.println("> % The SEVEN of SPADES is ineffective; there are no cards in the draw deck! % ");
			} else {
				if (turn.equals("p")) { //if player played the seven of spades, show message
					System.out.println("> % Your SEVEN of SPADES shuffled the draw deck! % ");
				} else { //do same for CPU
					System.out.println("> % The CPU's SEVEN of SPADES shuffled the draw deck! %");
				}
				shuffle(drawDeck); //shuffle draw deck
			}
		} else if (chosenCard.equals("CJ")) { //if the played card is a jack of clubs, the opponent must get a card from the player's hand
			if (turn.equals("p")) { //if it is the player's turn..
				System.out.println("> 0-0 Your JACK of CLUBS will make the CPU pick up one of your cards! 0-0");
				boolean validCard = false; //initiate boolean to check if chosen card is valid
				String givenCard = ""; //initiate givenCard variable so it can be handled outside loop
				while (validCard == false) {
					System.out.println("> Choose one of your cards to give to the CPU.");
					printDeck(playerDeck); //prints player's deck so they can see their cards
					givenCard = in.next();
					String usage = "checkExists"; //set usage to be passed into validcard method. This only checks 
					validCard = validateCard(playerDeck, givenCard, turn, usage);
				}
				for (int i = 0; i < 20; i++) { //iterate through each of player's cards to remove the chosen card
					if (playerDeck[i].equals(givenCard)) {
						playerDeck[i] = "NONE"; //replace chosen card with empty slot
					}
				}
				for (int i = 0; i < 20; i++) { //iterate through CPU's deck to replace first empty slot with given card
					if (CPUDeck[i].equals("NONE")) {
						CPUDeck[i] = givenCard;
						break; //leave loop immediately
					}
				}
				System.out.println("> You gave the CPU your [ " + givenCard + " ], or a ");
				translateCard(givenCard); //translate card given to words

			} else { //if the CPU's places down the card, it will choose a random card for the player to take.
				Random randomGen = new Random(); //create random generator
				System.out.println("> % The CPU's JACK of CLUBS will make you pick up one of their cards! % ");
				boolean validCard = false;
				String givenCard = "";
				while (validCard == false) {
					int randomNum = randomGen.nextInt(20); //generate card index num from 0-19
					givenCard = CPUDeck[randomNum];
					if (!givenCard.equals("NONE")) { //if the card at the index isn't empty, consider it to be a valid card
						validCard = true;
					}
				}
				//iterate through CPU's deck to replace card, iterate through player's card 
				for (int i = 0; i < 20; i++) {
					if (CPUDeck[i].equals(givenCard)) {
						CPUDeck[i] = "NONE";
					}
				}
				for (int i = 0; i < 20; i++) {
					if (playerDeck[i].equals("NONE")) {
						playerDeck[i] = givenCard;
						break;
					}
				}
				System.out.println("The CPU gave you a [ " + givenCard + " ], or a");
				translateCard(givenCard);
			}
		}
	} 

	public static void translateCard(String chosenCard) { //this method prints the word version of cards
		String typeCard = ""; //stores number or face value of card
		String suit = ""; //stores suit of card

		//assign suit names according to card's first character
			if (chosenCard.charAt(0) == 'C') {
				suit = "CLUBS 0-0";
			} else if (chosenCard.charAt(0) == 'D') {
				suit = "DIAMONDS <>";
			} else if (chosenCard.charAt(0) == 'S') {
				suit = "SPADES %";
			} else { //if the first character is 'H'
				suit = "HEARTS <3";
			}
			//assign rank names based on second character of card
			if (chosenCard.charAt(1) == '1') {
				typeCard = "ACE";
			} else if (chosenCard.charAt(1) == '2') {
				typeCard = "TWO";
			} else if (chosenCard.charAt(1) == '3') {
				typeCard = "THREE";
			} else if (chosenCard.charAt(1) == '4') {
				typeCard = "FOUR";
			} else if (chosenCard.charAt(1) == '5') {
				typeCard = "FIVE";
			} else if (chosenCard.charAt(1) == '6') {
				typeCard = "SIX";
			} else if (chosenCard.charAt(1) == '7') {
				typeCard = "SEVEN";
			} else if (chosenCard.charAt(1) == '8') {
				typeCard = "EIGHT";
			} else if (chosenCard.charAt(1) == '9') {
				typeCard = "NINE";
			} else if (chosenCard.charAt(1) == '0') {  //the '0' will represent tens in this game.
				typeCard = "TEN";
			} else if (chosenCard.charAt(1) == 'J') {
				typeCard = "# JACK #";
			} else if (chosenCard.charAt(1) == 'Q') {
				typeCard = "@ QUEEN @";
			} else {
				typeCard = "$ KING $";
			}

			//construct and print card name
			String cardName = typeCard + " of " + suit;
			System.out.println(cardName + "\n");
	}
	
	public static void printDeck(String[] playerDeck) { //this method sorts and prints the player's hand for viewing
		int numberCardCount = 0; //initialize counter for number cards
		int faceCardCount = 0; //initialize counter for face cards
		int nullCardCount = 0; //initialize counter for empty card slots
		//count the number of each card to create respective arrays
		for (int i = 0; i < 20; i++) {
			if (!playerDeck[i].equals("NONE")) { //if the card is not empty, check whether it is a face or number card, increment the counter
				if (playerDeck[i].charAt(1) == 'J' || playerDeck[i].charAt(1) == 'Q' || playerDeck[i].charAt(1) == 'K') {
					faceCardCount++;
				} else {
					numberCardCount++;
				} 
			} else {
				nullCardCount++;
			}
		} //create arrays to sort the cards
		String[] numberCards = new String[numberCardCount];
		String[] faceCards = new String[faceCardCount];
		//create indexes that will be used to fill up these arrays
		int numberIndex = 0;
		int faceIndex = 0;
		
		for (int i = 0; i < 20; i++) { //add cards to each respective array
			if (!playerDeck[i].equals("NONE")) {
				if (playerDeck[i].charAt(1) == 'J' ||playerDeck[i].charAt(1) == 'Q' || playerDeck[i].charAt(1) == 'K') {
					faceCards[faceIndex] = playerDeck[i]; //add the face card to facecards
					faceIndex++; //increment index so that next index is filled
				} else {
					numberCards[numberIndex] = playerDeck[i];
					numberIndex++;
				}
			}
		}
		//use bubble sort to sort both types of cards
		if (numberCardCount > 1) { //only sort if more than one card
			for (int i = 0; i < numberCardCount; i++) {
				for (int j = 1; j < numberCardCount; j++) {
					int num1 = numberCards[j-1].charAt(1); //set variables for number
					int num2 = numberCards[j].charAt(1);
					if (num1 > num2) {
						String temp = numberCards[j-1];
						numberCards[j-1] = numberCards[j];
						numberCards[j] = temp;
					}
				}
			}
		
		}
		
		String sortedFace = "CJ DJ HJ SJ CQ DQ HQ SQ CK DK HK SK"; //initiate string with sorted face cards to compare to
		if (faceCardCount > 1) { //only sort if more than one card
			for (int i = 0; i < faceCardCount; i++) {
				for (int j = 1; j < faceCardCount; j++) {
					int num1 = sortedFace.indexOf(faceCards[j-1]); //set numbers to be compared to the index of sorted cards in sortedFace
					int num2 = sortedFace.indexOf(faceCards[j]);
					if (num1 > num2) {
						String temp = faceCards[j-1];
						faceCards[j-1] = faceCards[j];
						faceCards[j] = temp;
					}
				}
			}
		} 
		
		int currIndex = 0; //create index counter to start putting sorted cards into player's deck
		// fill with number cards, then face cards, then fill remaining space with empty slots.
		for (int i = 0; i < numberCardCount; i ++) {
			playerDeck[currIndex] = numberCards[i];
			currIndex++; //increment index for player's overall deck
		}
		for (int i = 0; i < faceCardCount; i++) { //same thing for face cards
			playerDeck[currIndex] = faceCards[i];
			currIndex++;
		}
		for (int i = (20 - nullCardCount); i < 20; i++) { //fill remaining space with empty slots
			playerDeck[i] = "NONE";
		}
		
		//finally, print out all the cards
		for (int i = 0; i < 20; i ++) {
			if (!playerDeck[i].equals("NONE")) { //if the card in the player's deck isn't empty, print it out.
				System.out.print("[ " + playerDeck[i] + " ] ");
			}
		}
		System.out.println("\n> Please note that '0's are TENS !");
	}

	public static int calculatePoints(String[] deck, String player) throws IOException{ //this method calculates the total points of the player/CPU across all rounds, as well as updates the files
		int roundPoints = 0; //initiate variable to calculate amount of points earned in round
		int totalPoints = 0; //create variable to calculate total points in round
		boolean winFlag = true; //create boolean to express if the player/cpu has won
		
		for (int i = 0; i < 20; i++) { //go through every card, add points accordingly
			if (!deck[i].equals("NONE")) { //except the empty slots
				winFlag = false; //set win flag to false b/c they still have cards
				if (deck[i].charAt(1) == '8') { // add 50 points if a leftover card is an 8
					roundPoints += 50;
				} else if (deck[i].charAt(1) == 'J' || deck[i].charAt(1) == 'Q' || deck[i].charAt(1) == 'K') { // add 25 points to face card
					roundPoints += 25;
				} else {
					roundPoints += 5; // give 5 points for each number card
				}
			}
		}
		
		if (winFlag == true) { //if the player won, give them 1000 points for this round
			roundPoints = 500;
		}
		
		if (player.equals("p")) { //open player file if it is players deck passed in
			String playerFile = "C:\\Users\\dorothy\\Documents\\VSCode_for_Java\\ICS3U\\playerPoints.txt"  ; //"H:\\ICS3\\ICS3U\\src\\FinalProject\\playerPoints.txt"
			PrintWriter pointWriter = new PrintWriter(new FileWriter (playerFile, true)); //open file writer for appending to player's point file
			pointWriter.println(roundPoints); //print round's points on new line (append)
			pointWriter.close(); //close to save file
			File file = new File(playerFile); //create file case
			Scanner myFileScanner = new Scanner(file); //create file scanenr to read from file
			while (myFileScanner.hasNext()) { //add up all lines of point file to get total points
				totalPoints += myFileScanner.nextInt();
			}
			myFileScanner.close();
		} else { //calculate points if CPU deck passed in
			String CPUFile = "C:\\Users\\dorothy\\Documents\\VSCode_for_Java\\ICS3U\\Crazy8s\\CPUPoints.txt"; // "H:\\ICS3\\ICS3U\\src\\FinalProject\\CPUPoints.txt"
			PrintWriter pointWriter = new PrintWriter(new FileWriter (CPUFile, true)); //open file writer for appending to player's point file
			pointWriter.println(roundPoints); //print round's points on new line (append)
			pointWriter.close(); //close to save file
			File file = new File(CPUFile); //create file case
			Scanner myFileScanner = new Scanner(file); //create file scanenr to read from file
			while (myFileScanner.hasNext()) { //add up all lines of point file to get total points
				totalPoints += myFileScanner.nextInt();
			}
			myFileScanner.close();
		}
		
		return totalPoints; //return the total amount of points across all rounds to method invoked

	}

	public static void clearFile(String filePath) throws IOException{ //this method clears the point files when games are rerun
		FileWriter fileWriter = new FileWriter(filePath, false); //false overwrites the file; not writing anything, so will clear.
		fileWriter.close(); //save
	}
}
