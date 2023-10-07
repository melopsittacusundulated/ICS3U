package projects;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;
/*
 * Author: Dorothy Lin
 * Class: ICS3U
 * Date: 10-13-2023
 * Description: This program is a PokemonGo-style game; it interacts with
 * the user with single-key inputs, and catches type a range errors. It randomly generates Pokemon and 
 * and the outcomes of catching them with balls of varying probabilities. After the user exhausts 
 * all balls, the game ends and feedback + a score are outputted. An option to restart the game is given as well.
 */

/*TO-DO:
 * use ASCII art to make more beautiful
 * duration? make it so rules, pokemon images appear slower
 * Error handling; if stringinputted during ball choice, handle error. why does it crash after happening twice?
 * maybe make a counter to find how many of each pokemon caught, give user a "badge" or ASCII drawing of pokemon
 */

public class BayviewmonGO {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in); // create new scanner class
		Random randomGen = new Random(); // create new random class

		String logoArt = //text generation from fsymbols.com
		"\n██████╗░░█████╗░██╗░░░██╗██╗░░░██╗██╗███████╗░██╗░░░░░░░██╗███╗░░░███╗░█████╗░███╗░░██╗\n" +
		"██╔══██╗██╔══██╗╚██╗░██╔╝██║░░░██║██║██╔════╝░██║░░██╗░░██║████╗░████║██╔══██╗████╗░██║\n" +
		"██████╦╝███████║░╚████╔╝░╚██╗░██╔╝██║█████╗░░░╚██╗████╗██╔╝██╔████╔██║██║░░██║██╔██╗██║\n" +
		"██╔══██╗██╔══██║░░╚██╔╝░░░╚████╔╝░██║██╔══╝░░░░████╔═████║░██║╚██╔╝██║██║░░██║██║╚████║\n" +
		"██████╦╝██║░░██║░░░██║░░░░░╚██╔╝░░██║███████╗░░╚██╔╝░╚██╔╝░██║░╚═╝░██║╚█████╔╝██║░╚███║\n" +
		"╚═════╝░╚═╝░░╚═╝░░░╚═╝░░░░░░╚═╝░░░╚═╝╚══════╝░░░╚═╝░░░╚═╝░░╚═╝░░░░░╚═╝░╚════╝░╚═╝░░╚══╝\n" +
		"                                ░██████╗░░█████╗░██╗\n" +
		"                                ██╔════╝░██╔══██╗██║\n" +
		"                                ██║░░██╗░██║░░██║██║\n" +
		"                                ██║░░╚██╗██║░░██║╚═╝\n" +
		"                                ╚██████╔╝╚█████╔╝██╗\n" +
		"                                ░╚═════╝░░╚════╝░╚═╝\n";
	System.out.println(logoArt); //print title
		
		// receive user's trainer name, welcome them
		System.out.println("To start, enter your name: ");
		String trainerName = in.next();
		System.out.println("\nWelcome, trainer " + trainerName + "!");

		String showRules; //declare show rules variable for loop do-while loop usage
		do { //create loop to validate input for rules
			System.out.println("Would you like to see the rules? (y/n): ");
			showRules = in.next(); //receive input whether to print rules or not
			if ( (!showRules.equals("y")) && (!showRules.equals("n")) ) {
				System.out.println("Invalid input\n"); //print invalid message
			}
		} while ( (!showRules.equals("y")) && (!showRules.equals("n")) );

		if (showRules.equals("y")) {
			System.out.println("/////////////// R U L E S ///////////////");
			System.out.println("\n1. You need to throw balls to catch Bayviewmons! You will receive a total of 15 (5 of each):");
			System.out.println("Bokeballs - 60% chance of capture");
			System.out.println("Breatballs - 70% chance of capture");
			System.out.println("Bultraballs - 80% chance of capture");
			System.out.println("\n2. You will randomly encounter these Bayviewmons, and will receive their given number of points upon successful capture:");
			System.out.println("1 - Baykachu (2p)");
			System.out.println("2 - Bulbabay (2p)");
			System.out.println("3 - Charmanview (3p)");
			System.out.println("4 - Squirtew (4p)");
			System.out.println("5 - Eevay (5p)");
			System.out.println("\n3. You may capture as many Bayviewmon as you like, as long as you have a sufficient number of balls!");
			System.out.println("\n4. Have FUN!");
			System.out.println("\n//////////////////////////////////////////////////");

		} else {
			System.out.println("\nAlright!\n");
			System.out.println("//////////////////////////////////////////////////");
		}
		
		String runValidate = "y"; //Create user-interaction-determined variable that controls whether game runs

		while (true) { // create a loop so that user can play as many times as they like
			
			do {
				System.out.println("\nAre you ready to begin, " + trainerName + "? (y/n)");
				runValidate = in.next(); //validate whether game should begin via user input
				if (!runValidate.equals("y") && !runValidate.equals("n")) { //output error message if input not valid
					System.out.println("\nInvalid input");
				} else if (runValidate.equals("n")) {
					System.out.println("\nNo rush!");
				}
			} while (!runValidate.equals("y")); //run again if user not ready or invalid input
			
			System.out.println("\nLet's catch'em all!\n");
			//creating, resetting some variables
			String bayviewMon; //declare bayviewMon name variable
			String playValidate; //declare play again loop validation
			String encounterValidate; //declare user input variable for encounters
			int ballChoice = 0; //initiate ball choice variable

			int bokeBalls = 5; //initiate ball quantities
			int breatBalls = 5;
			int bultraBalls = 5;
			int ballBalance = 15; //create variable to store total number of balls

			int captureChance = 0; // initiate variable for capture probability

			int bayviewMonPoints = 0; //initiate point variable to store respective bayviewmon's point value
			int points = 0; //initiate player's point total

			while (ballBalance > 0) { //run encounter loop continually if enough balls	
				// assign bayviewmon, potential points based on random number. loop if user doesnt want to encounter
				do {
					int bayviewMonAssign = randomGen.nextInt(6) + 1; // generate number 1-6 for bayviewmon
					if (bayviewMonAssign == 1) {
						bayviewMon = "Baykachu"; //assign bayviewmon name
						bayviewMonPoints = 2; //assign points value for respective bayviewmon
					} else if (bayviewMonAssign == 2) {
						bayviewMon = "Bulbabay";
						bayviewMonPoints = 2;
					} else if (bayviewMonAssign == 3) {
						bayviewMon = "Charmanview";
						bayviewMonPoints = 3;
					} else if (bayviewMonAssign == 4) {
						bayviewMon = "Squirtew";
						bayviewMonPoints = 4;
					} else {
						bayviewMon = "Eevay";
						bayviewMonPoints = 5;
					}
						do { //create loop to validate input
							System.out.println("\nA wild #" + bayviewMonAssign + ", a " + bayviewMon + " appeared! It is worth " + bayviewMonPoints + " points. Would you like to encounter it? (y/n)\n");
							encounterValidate = in.next();
							if ( (!encounterValidate.equals("y")) && (!encounterValidate.equals("n")) ) { //check if valid input
								System.out.println("\nInvalid input.");
							}
						} while ( (!encounterValidate.equals("y")) && (!encounterValidate.equals("n")) ); //continually ask for encounter input if invalid 

				} while (!encounterValidate.equals("y")); //continue to generate encounters until user inputs "y"
					
				while (true) { //create a loop to run until user chooses ball with valid quantity
					do {
						try { //create try block to catch if user accidentally types string in. this prevents the whole game from crashing if they type a letter rather than an int
							System.out.println("\nChoose a ball to capture the " + bayviewMon + "! (1/2/3)"); //allow user to choose balls
							System.out.println("(1) Bokeball - 60% capture chance - " + bokeBalls + " remaining");
							System.out.println("(2) Breatball - 70% capture chance - " + breatBalls + " remaining");
							System.out.println("(3) Bultraball - 80% capture chance - " + bultraBalls + " remaining\n");
							ballChoice = in.nextInt(); //get user's ball choice number
							if ( (ballChoice < 1) || (ballChoice > 3) ) { //print message if invalid number
								throw new InputMismatchException(); //if input type not int (or just not 1,2, or 3), throws exception
							}
						} catch (InputMismatchException notInt) { //handles error if user doesn't type a number
							System.out.println("\nInvalid input, please input 1,2, or 3.\n");
							ballChoice = in.nextInt(); //ask user again
							if ( (ballChoice < 1) || (ballChoice > 3) ) {
								System.out.println("\nThe ball choices are 1 (bokeballs), 2 (breatballs), or 3 (bultraballs)!"); //passive-aggressively remind user of valid inputs if they don't input a correct one
							}
						}
					} while ( (ballChoice < 1) || (ballChoice > 3) ); //ensure user inputs valid number

						if (ballChoice == 1) { //bokeball condition
							if (bokeBalls == 0) { //check if enough balls
							System.out.println("\nYou don't have enough Bokeballs...");
							} else {
								bokeBalls -= 1; //decrement ball amount because "used"
								captureChance = 6; //set probability out of 10 for capture
								break; //break out of loop if ball has been chosen
							}
						} else if (ballChoice == 2) { //same thing for breatballs
							if (breatBalls == 0){
								System.out.println("\nYou don't have enough Breatballs...");
							} else {
								breatBalls -= 1;
								captureChance = 7;
								break;
							}
						} else {
							if (bultraBalls == 0) { //same thing for bultraballs
								System.out.println("\nYou don't have enough Bultraballs...");
							} else {
								bultraBalls -= 1;
								captureChance = 8;
								break;
							}
						}
				}
				//this is where capture and points adding will happen
				int captureGen = randomGen.nextInt(10) + 1; //generate the chance of capture
				if (captureGen <= captureChance) { //check if capture chance falls within chance threshold
					System.out.println("\nYou caught the " + bayviewMon + " and earned " + bayviewMonPoints + " points!");
					points += bayviewMonPoints;
				} else { //print capture failure message if number does not fall within chance threshold
					System.out.println("\nOh no! Your capture failed and the " + bayviewMon + " got away! Better luck next time...");
				} 
				System.out.println("You have " + points + " points."); //let user know total points
				ballBalance = bokeBalls + breatBalls + bultraBalls; //total up number of balls to check if user can catch anymore
			} //end game when 0 balls as per conditional loop

			System.out.println("\nNo balls remaining...\n///////////////////GAME OVER///////////////////\n");

			//print respective feedback 
			if (points >= 20) { 
				System.out.println("You are an EXCELLENT Bayviewmon catcher!\nYou scored an AMAZING " + points + " points!");
			} else if (points >= 15) {
				System.out.println("Good job! You are a DECENT Bayviewmon catcher\nYou scored " + points + " points.");
			} else {
				System.out.println("You need more practice Bayviewmon catching, but good effort!\nYou scored " + points + " points.");
			}

			// Ask if user wants to play again; make sure valid input with loop
			do {
				System.out.println("\nWould you like to play again? (y/n)");
				playValidate = in.next(); //assign variable value to input 
				if ( (!playValidate.equals("y") ) && (!playValidate.equals("n")) ) {
					System.out.println("Invalid input");
				}
			} while ( (!playValidate.equals("y") ) && (!playValidate.equals("n")) ); //run loop if invalid

			if (playValidate.equals("n")) { //break out of game loop 
				System.out.println("\nOkay! See you next time, trainer " + trainerName + "!");
				break;
			} else {
				System.out.println("\nGreat! Let's go!\n"); //print restarting message if user says yes
			}
		}
		in.close(); //close the scanner
		System.out.println("Thanks for playing Bayviewmon GO!");
	
	}
}

