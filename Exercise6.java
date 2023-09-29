import java.util.Scanner;
/*
 * Name: Dorothy
 * Date:2023-09-28
 * Desc: This program prompts the user for an amount in cents
 * and then displays the minimum number of coins necessary to make the change
 */
public class Exercise6 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter change in cents: ");
        int change = in.nextInt();
        // get number of quarters
        int change1 = change % 25; // leftover after wuarters
        int quarters = (change - change1) / 25; // find quarters

        //get numner of dimes
        int change2 = change1 % 10;
        int dimes = (change1 - change2) / 10;

         //get numner of nickels
        int change3 = change2 % 5;
        int nickels = (change2 - change3) / 5;

        //get numner of pennies
        int pennies = change3;

        System.out.println("The minimum number of coins is: ");
        System.out.println("Quarters: " + quarters);
        System.out.println("Dimes: " + dimes);
        System.out.println("Nickels: " + nickels);
        System.out.println("Pennies: " + pennies);
        in.close();
    }
}
