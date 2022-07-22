package finalproject;

import java.util.Scanner;

import com.github.tomaslanger.chalk.Chalk;

public class Dashboard {
    Dashboard(String name, String username) {

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("\033[H\033[2J");
        System.out.flush();
        System.out.println(Chalk.on("Welcome " + name).bgWhite().black());
        Patient patient = new Patient();
        System.out.println();
        System.out.println(Chalk.on("Please enter what you would like to do:").yellow());
        System.out.println(Chalk.on("1. Go to the appointment system").yellow());
        System.out.println(Chalk.on("2. Add new patient").yellow());
        System.out.println(Chalk.on("3. Show a patient").yellow());
        System.out.println(Chalk.on("4. Update a patient").yellow());
        System.out.println(Chalk.on("5. Get previous diagnosis of a patient").yellow());
        System.out.println(Chalk.on("6. New diagnosis of a patient").yellow());
        System.out.println(Chalk.on("7. Logout").yellow());
        System.out.println(Chalk.on("Enter your choice: ").green());
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                Appointment appointment2 = new Appointment(username);
                break;
            case 2:
                patient.addNewPatient();
                Dashboard dashboard3 = new Dashboard(name, username);
                break;
            case 3:
                patient.showPatient();
                Dashboard dashboard4 = new Dashboard(name, username);
                break;
            case 4:
                patient.updatePatient();
                Dashboard dashboard5 = new Dashboard(name, username);
                break;
            case 5:
                Diagnosis diagnosis = new Diagnosis();
                System.out.println(Chalk.on("Please enter the patient ID: ").green());
                String patientID = scanner.nextLine();
                diagnosis.getPreviousDiagnosis(username, patientID);
                Dashboard dashboard6 = new Dashboard(name, username);
                break;
            case 6:
                Diagnosis diagnosis1 = new Diagnosis();
                // get patient ID
                System.out.println(Chalk.on("Enter the patient ID:").green());
                String patientID1 = scanner.nextLine();
                // get appointment ID
                System.out.println(Chalk.on("Enter the appointment ID:").green());
                String appointmentID = scanner.nextLine();

                diagnosis1.newDiagnosis(username, patientID1, appointmentID);
                Dashboard dashboard7 = new Dashboard(name, username);
                break;
            case 7:
                System.out.println(Chalk.on("Logout").blue());
                System.exit(0);
                break;
            default:
                System.out.println(Chalk.on("Invalid choice").red());
                System.out.println();
                System.out.println();
                break;
        }

    }
}
