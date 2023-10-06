package projects;
import java.util.Random;
import java.util.Scanner;
/*
 * Author: Dorothy Lin
 * Class : ICS3U
 * Date: 10-13-2023
 * Description: This program is a PokemonGo-style game; it interacts with
 * the user with single-key inputs, randomly generates Pokemon and catch outcomes, 
 * and outputs scores.
 */

/*DECORATION NOTES:
 * use ASCII art to make more beautiful
 * ASCII text, pokemon
 * coloured tetxt?
 * duration?
 * transitions (??)
 * Error decoration
 */

public class BayviewmonGO {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Random randomGen = new Random();
		
		System.out.println("Bayviewmon Go!"); // Show ASCII title
		
		// receive user's trainer name, welcome them
		System.out.println("To start, enter your name: ");
		String trainerName = in.next();
		System.out.println("Welcome, trainer " + trainerName + "!");
		
		System.out.println("Would you like to see the rules? (y/n): ");
		String showRules = in.next(); //receive input whether to print rules or not
		
		if (showRules.equals("y")) {
			System.out.println("----------R U L E S----------");
			System.out.println();
			System.out.println("1. You need to throw balls to catch Bayviewmons! You will receive a total of 15 (5 of each):");
			System.out.println("Bokeballs - 60% chance of capture");
			System.out.println("Breatballs - 70% chance of capture");
			System.out.println("Bultraballs - 80% chance of capture");
			System.out.println();
			System.out.println("2. You will randomly encounter these Bayviewmons, and will receive their given number of points upon successful capture:");
			System.out.println("1 - Baykachu (2p)");
			System.out.println("2 - Bulbabay (2p)");
			System.out.println("3 - Charmanview (3p)");
			System.out.println("4 - Squirtew (4p)");
			System.out.println("5 - Eevay (5p)");
			System.out.println();
			System.out.println("3. You may capture as many pokemon as you like, as long as you have a sufficient number of balls!");
			System.out.println();
			System.out.println("4. Have FUN!");
			
		} else {
			System.out.println("Alright!");
			System.out.println();
		}

		// encounter pokemon
		System.out.println("Would you like to start? (y/n)");
		String runValidate = in.next(); //validate whether game should run via user input
		
		if (runValidate.equals("y")) {
			
		} else if (runValidate.equals("n")) {
			System.out.println(""); //print scores, exit program
		} else {
			System.out.println();
		}
		in.close(); //close scanner
	}
}
