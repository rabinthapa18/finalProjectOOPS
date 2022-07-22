package finalproject;

import java.util.Scanner;

import com.github.tomaslanger.chalk.Chalk;

public class App {
    // Create a new instance of the MongoDB class

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Misc misc = new Misc();
        misc.pressEnterToContinue();
        misc.clear();
        Doctor welcomeMenu = new Doctor();
        System.out.println(Chalk.on("WELCOME TO THE DOCTOR'S APP").bgWhite());
        System.out
                .println(Chalk.on("Please enter what you would like to do:\n1. Login\n2. Register\n3. Exit").yellow());
        System.out.println(Chalk.on("Enter your choice: ").green());
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    welcomeMenu.login();
                    break;
                case 2:
                    welcomeMenu.signup();
                    break;
                case 3:
                    System.out.println(Chalk.on("Goodbye!").blue());
                    break;
                default:
                    System.out.println(Chalk.on("Invalid choice").red());
                    main(null);
                    break;

            }
        } catch (Exception e) {
            System.out.println();
            System.out.println(Chalk.on("Invalid input, please try again").red());
            main(null);
        }
    }

}