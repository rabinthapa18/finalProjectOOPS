package finalproject;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.client.FindIterable;

import com.github.tomaslanger.chalk.Chalk;

public class Diagnosis {
    Misc misc = new Misc();
    Scanner scanner = new Scanner(System.in);

    public void newDiagnosis(String docUsername, String patientID, String appointmentID) {
        misc.clear();
        System.out.println(Chalk.on(
                "Please enter the path for the file containing the diagnosis, only .txt file allowed (You can just drag and drop the file to this terminal) :")
                .green());
        String path = scanner.nextLine();

        // check if file exists
        if (!new File(path).exists()) {
            System.out.println(Chalk.on("File does not exist").red());
            Dashboard dashboard = new Dashboard("", docUsername);
        }

        // check if file is text file
        if (!path.endsWith(".txt")) {
            System.out.println(Chalk.on("Invalid file type").red());
            Dashboard dashboard = new Dashboard("", docUsername);
        }

        // check if file is empty
        if (new File(path).length() == 0) {
            System.out.println(Chalk.on("File is empty").red());
            Dashboard dashboard = new Dashboard("", docUsername);
        }

        // copy contents of file to string
        String diagnosis = "";
        try {
            // copy contents inside file to string
            diagnosis = new String(Files.readAllBytes(new File(path).toPath()));

            // add to database
            MongoDB mongoDB = new MongoDB();
            // get current date
            String date = new SimpleDateFormat("dd-MM-YYYY").format(new Date());

            mongoDB.addDiagnosis(docUsername, patientID, appointmentID, date, diagnosis);
            System.out.println(Chalk.on("Diagnosis added").bgGreen());
            misc.pressEnterToContinue();

        } catch (Exception e) {

            System.out.println(Chalk.on("Error reading file").red());
            newDiagnosis(docUsername, patientID, appointmentID);
            misc.pressEnterToContinue();
        }
    }

    public void getPreviousDiagnosis(String docUsername, String patientID) {
        MongoDB mongoDB = new MongoDB();

        // print diagnosis with Date and ask user which one they want to see
        FindIterable<Document> list = mongoDB.getPreviousDiagnosis(docUsername, patientID);
        int i = 1;
        misc.clear();
        System.out.println(Chalk.on("Previous diagnoses:").bgBlue());
        for (Document document : list) {
            System.out.println(Chalk.on(i + ". " + document.getString("date")).yellow());
            i++;
        }

        System.out.println(Chalk.on("Enter which diagnosis you want to see: ").green());
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice < 1 || choice > i) {
            System.out.println(Chalk.on("Invalid choice").red());
            getPreviousDiagnosis(docUsername, patientID);
        }
        misc.clear();
        Document document = list.iterator().next();
        System.out.println(Chalk.on("Diagnosis: ").yellow());
        System.out.println(Chalk.on(document.getString("diagnosis")).cyan());
        misc.pressEnterToContinue();
        // want to save as a file?
        System.out.println(Chalk.on("Do you want to save this diagnosis as a file? (y/n): ").green());
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            System.out.println(Chalk.on("Enter the name for the file: ").green());
            String path = scanner.nextLine();
            try {
                // copy contents inside file to string
                Files.write(new File(path + ".txt").toPath(), document.getString("diagnosis").getBytes());
                System.out.println(Chalk.on("Diagnosis saved").bgGreen());
                misc.pressEnterToContinue();
            } catch (Exception e) {
                System.out.println(Chalk.on("Error writing file").red());
                misc.pressEnterToContinue();
                getPreviousDiagnosis(docUsername, patientID);
            }
        }
    }
}
