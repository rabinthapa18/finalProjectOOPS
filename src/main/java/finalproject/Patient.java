package finalproject;

import java.util.Scanner;

import com.github.tomaslanger.chalk.Chalk;

public class Patient {
    Misc misc = new Misc();

    public void addNewPatient() {
        Scanner scanner = new Scanner(System.in);
        misc.clear();
        System.out.println(Chalk.on("Add a new patient").bgWhite());
        System.out.println(Chalk.on("Enter the patient's name:").green());
        String name = scanner.nextLine();
        System.out.println(Chalk.on("Enter the patient's age:").green());
        String age = scanner.nextLine();
        if (!age.matches("[0-9]+") || age.length() > 2) {
            System.out.println(Chalk.on("Invalid input, please try again").red());
            addNewPatient();
        }

        System.out.println(Chalk.on("Enter the patient's email address:").green());
        String email = scanner.nextLine();
        if (!email.matches(
                "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
            System.out.println(Chalk.on("Invalid input, please try again").red());
            addNewPatient();
        }

        System.out.println(Chalk.on("Enter the patient's phone number:").green());
        String phone = scanner.nextLine();
        if (!phone.matches("[0-9]+")) {
            System.out.println(Chalk.on("Invalid input, please try again").red());
            addNewPatient();
        }

        MongoDB mongo = new MongoDB();

        String patientID = "patient_" + (mongo.getPatientCount() + 1);

        mongo.addNewPatient(patientID, name, age, email, phone);
        System.out.println(Chalk.on("patientID is :" + patientID).bgBlue().white());
        misc.pressEnterToContinue();
    }

    public void showPatient() {
        misc.clear();
        Scanner scanner = new Scanner(System.in);

        System.out.println(Chalk.on("Show patient details").bgWhite());
        System.out.println(Chalk.on("Enter the patient's ID or email address or phone number:").green());
        String input = scanner.nextLine();
        MongoDB mongo = new MongoDB();
        mongo.showPatient(input);
        misc.pressEnterToContinue();
    }

    public void updatePatient() {
        misc.clear();
        Scanner scanner = new Scanner(System.in);

        System.out.println(Chalk.on("Update patient details").bgWhite());
        System.out.println(Chalk.on("Enter the patient's ID or email address or phone number:").green());
        String input = scanner.nextLine();
        MongoDB mongo = new MongoDB();
        String patientID = mongo.showPatient(input);

        if (patientID != "") {

            System.out.println(Chalk.on("What field you want to update:").yellow());
            System.out.println(Chalk.on("1. Name").yellow());
            System.out.println(Chalk.on("2. Age").yellow());
            System.out.println(Chalk.on("3. Email").yellow());
            System.out.println(Chalk.on("4. Phone number").yellow());
            System.out.println(Chalk.on("5. All").yellow());
            String field = scanner.nextLine();

            if (field.equals("1")) {
                System.out.println(Chalk.on("Enter the new name:").green());
                String name = scanner.nextLine();
                mongo.updatePatient(patientID, "name", name);
            } else if (field.equals("2")) {
                System.out.println(Chalk.on("Enter the new age:").green());
                String age = scanner.nextLine();
                mongo.updatePatient(patientID, "age", age);
            } else if (field.equals("3")) {
                System.out.println(Chalk.on("Enter the new email address:").green());
                String email = scanner.nextLine();
                mongo.updatePatient(patientID, "email", email);
            } else if (field.equals("4")) {
                System.out.println(Chalk.on("Enter the new phone number:").green());
                String phone = scanner.nextLine();
                mongo.updatePatient(patientID, "phone", phone);
            } else if (field.equals("5")) {
                System.out.println(Chalk.on("Enter the new name:").green());
                String name = scanner.nextLine();
                mongo.updatePatient(patientID, "name", name);
                System.out.println(Chalk.on("Enter the new age:").green());
                String age = scanner.nextLine();
                mongo.updatePatient(patientID, "age", age);
                System.out.println(Chalk.on("Enter the new email address:").green());
                String email = scanner.nextLine();
                mongo.updatePatient(patientID, "email", email);
                System.out.println(Chalk.on("Enter the new phone number:").green());
                String phone = scanner.nextLine();
                mongo.updatePatient(patientID, "phone", phone);
            } else {
                System.out.println(Chalk.on("Invalid input, please try again").red());
                updatePatient();
            }

            System.out.println(Chalk.on("Patient updated").bgGreen());
            misc.pressEnterToContinue();
        }
    }
}
