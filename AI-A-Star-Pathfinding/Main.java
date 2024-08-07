import java.io.*;
import java.util.Scanner;

/**
 * Main class to run the pathfinding application.
 */
class Main {
    public static void main(String args[]) throws IOException {
        // Create a scanner object for user input
        Scanner scan = new Scanner(System.in);

        // Prompt the user for the seed value
        System.out.print("Input seed (1-10): ");
        int seed = scan.nextInt();

        // Prompt the user for the number of maps to run
        System.out.print("Input number of maps to run (1-5): ");
        int maps = scan.nextInt();

        // Prompt the user for the speed of execution
        System.out.print("Input speed (1-9) (5 is normal): ");
        int speed = scan.nextInt();
        System.out.println();

        // Create a pathfinding object
        pathFinding A = new pathFinding();
        
        // Run the pathfinding with the given parameters
        A.run("map", seed, maps, speed);

        // Wait for user input to exit
        Scanner input = new Scanner(System.in); 
        String side1 = "";
        System.out.print("All maps complete wooo!!!! \n input y to exit: ");
        while(!(side1.equals("y") || side1.equals("Y"))){
            side1 = input.nextLine();
        }

        // Exit the program
        System.exit(0);
    }
}
