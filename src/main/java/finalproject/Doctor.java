package finalproject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

import org.bson.Document;

import com.github.tomaslanger.chalk.Chalk;

public class Doctor {

    public void signup() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(Chalk.on("Enter your Fullname: ").green());
        String fullname = scanner.nextLine();
        if (!fullname.matches("[a-zA-Z ]+")) {
            System.out.println(Chalk.on("Invalid input, please try again").red());
            signup();
        }

        System.out.println(Chalk.on("Enter your Email: ").green());
        String email = scanner.nextLine();
        if (!email.matches(
                "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
            System.out.println(Chalk.on("Invalid input, please try again").red());
            signup();
        }

        System.out.println(Chalk.on("Enter your age: ").green());
        String age = scanner.nextLine();
        if (!age.matches("[0-9]+") || age.length() > 2) {
            System.out.println(Chalk.on("Invalid input, please try again").red());
            signup();
        }

        System.out.println(Chalk.on("Enter your username (it must be unique): ").green());
        String username = scanner.nextLine();
        if (!username.matches("[a-zA-Z0-9]+")) {
            System.out.println(Chalk.on("Invalid input, please try again").red());
            signup();
        }

        // check if username already exists
        // if yes, ask user to enter a new username
        // if no, continue
        MongoDB mongoDB = new MongoDB();

        if (mongoDB.findUsername("doctors", username)) {
            System.out.println(Chalk.on("Username already exists, please try again").red());
            signup();
        }

        System.out.println(Chalk.on("Enter your password :").green());
        String password = scanner.nextLine();
        if (password.length() < 8) {
            System.out.println(Chalk.on("Password must be at least 8 characters").red());
            signup();
        }
        // password Hashing
        PasswordHashing passwordHashing = new PasswordHashing();
        String[] hashed = { "", "" };
        try {
            hashed = passwordHashing.hashPassword(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

            e.printStackTrace();
        }
        String salt = hashed[0];
        String hash = hashed[1];

        // mongoDB.getCollection("doctors");
        mongoDB.addData("doctors",
                new Document("fullname", fullname).append("email", email).append("age", age)
                        .append("username", username)
                        .append("password", hash).append("salt", salt));
        System.out.println(Chalk.on("You have successfully registered!").bgGreen().black());
        System.out.println(Chalk.on("Please login to continue").bgGreen().black());
        login();
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(Chalk.on("Enter your username: ").green());
        String username = scanner.nextLine();
        System.out.println(Chalk.on("Enter your password: ").green());
        String password = scanner.nextLine();

        // get doctor by username
        MongoDB mongoDB = new MongoDB();
        Document doctor = mongoDB.getDoctorByUsername(username);
        if (doctor == null) {
            Misc misc = new Misc();
            misc.clear();
            System.out.println(Chalk.on("Username or password incorrect").red());
            login();
        }
        // check if password is correct
        PasswordHashing passwordHashing = new PasswordHashing();
        Boolean isCorrect = false;
        try {
            isCorrect = PasswordHashing.checkPassword(password, doctor.getString("password"), doctor.getString("salt"));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println(Chalk.on("Invalid password").red());
        }
        if (!isCorrect) {
            Misc misc = new Misc();
            misc.clear();

            System.out.println(Chalk.on("Username or password incorrect").red());
            login();
        }
        Dashboard dashboard = new Dashboard(doctor.getString("fullname"), doctor.getString("username"));
    }

}
