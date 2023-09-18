package Intro;

import java.util.Scanner;

public class Sel1 {
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Pick a number between 1 and 10: ");
		int number = scanner.nextInt();
		
		if (number >= 5) {
			System.out.println("You won!");
		}
	}
}
