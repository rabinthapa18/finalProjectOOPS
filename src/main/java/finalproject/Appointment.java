package finalproject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.github.tomaslanger.chalk.Chalk;

public class Appointment {
    Misc misc = new Misc();

    Appointment(String docUserName) {
        appointmentDashboard(docUserName);
    }

    private void appointmentDashboard(String docUserName) {
        Scanner scanner = new Scanner(System.in);
        misc.clear();
        System.out.println(Chalk.on("Welcome to the appointment system").bgWhite());
        System.out.println();
        System.out.println(Chalk.on("Please enter what you would like to do:").yellow());
        System.out.println(Chalk.on("1. Show today's appointments").yellow());
        System.out.println(Chalk.on("2. Create a new appointment").yellow());
        System.out.println(Chalk.on("3. Show an appointment").yellow());
        System.out.println(Chalk.on("4. Update an appointment").yellow());
        System.out.println(Chalk.on("5. Delete an appointment").yellow());
        System.out.println(Chalk.on("6. Go back").yellow());
        System.out.println(Chalk.on("Enter your choice: ").green());
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                showTodayAppointments(docUserName);
                appointmentDashboard(docUserName);
                break;
            case 2:
                createAppointment(docUserName);
                appointmentDashboard(docUserName);
                break;
            case 3:
                // showAppointment();
                showAppointment(docUserName);
                appointmentDashboard(docUserName);
                break;
            case 4:
                updateAppointment(docUserName);
                appointmentDashboard(docUserName);
                break;
            case 5:
                deleteAppointment(docUserName);
                appointmentDashboard(docUserName);
                break;
            case 6:
                Dashboard dashboard = new Dashboard("", docUserName);
                break;
            default:
                System.out.println("Invalid choice");
                System.out.println();
                appointmentDashboard(docUserName);
                break;
        }
    }

    public void showTodayAppointments(String docUserName) {
        MongoDB mongo = new MongoDB();

        // get today date
        String today = new SimpleDateFormat("dd-MM-YYYY").format(new Date());
        misc.clear();
        mongo.showTodayAppointments(docUserName, today);
    }

    public void createAppointment(String docUserName) {
        MongoDB mongo = new MongoDB();
        Scanner scanner = new Scanner(System.in);
        misc.clear();
        System.out.println(Chalk.on("Create a new appointment").bgWhite());
        System.out.println(Chalk.on("Enter the patient's ID:").green());
        String patientID = scanner.nextLine();
        if (!mongo.checkPatient(patientID)) {
            System.out.println(Chalk.on("Invalid patient ID").red());
            appointmentDashboard(docUserName);
        }
        System.out.println(Chalk.on("Enter the date of the appointment, please enter in DD-MM-YYYY format:").green());
        String date = scanner.nextLine();
        // date regex check
        if (!date.matches("^(0[1-9]|[12][0-9]|3[01])[-](0[1-9]|1[012])[-](20[0-9][0-9])")) {
            System.out.println(Chalk.on("Invalid date").red());
            appointmentDashboard(docUserName);
        }

        System.out.println(Chalk.on("Start time, Please enter in 24-hour format (13:00) : ").green());
        String startTime = scanner.nextLine();
        // time regex check
        if (!startTime.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            System.out.println(Chalk.on("Invalid time").red());
            appointmentDashboard(docUserName);
        }
        System.out.println(Chalk.on("End time, Please enter in 24-hour format (13:00) : ").green());
        String endTime = scanner.nextLine();
        // time regex check
        if (!endTime.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            System.out.println(Chalk.on("Invalid time").red());
            appointmentDashboard(docUserName);
        }
        System.out.println(Chalk.on("Enter the reason for the appointment:").green());
        String reason = scanner.nextLine();

        String appointmentId = "appointment_" + (mongo.getAppointmentCount() + 1);

        mongo.createAppointment(appointmentId, docUserName, patientID, date, startTime, endTime, reason);
    }

    public String showAppointment(String docUserName) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(Chalk.on("How would you like to search for an appointment?").yellow());
        System.out.println(Chalk.on("1. By appointment ID").yellow());
        System.out.println(Chalk.on("2. By patient ID and date").yellow());
        System.out.println(Chalk.on("3. Go back").yellow());
        System.out.println(Chalk.on("Enter your choice: ").green());

        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                System.out.println(Chalk.on("Enter the appointment ID:").green());
                String appointmentID = scanner.nextLine();

                MongoDB mongo = new MongoDB();
                return mongo.showAppointmentByID(appointmentID);
            case 2:
                System.out.println(Chalk.on("Enter the patient ID:").green());
                String patientID = scanner.nextLine();
                System.out.println(
                        Chalk.on("Enter the date of the appointment, please enter in DD-MM-YYYY format:").green());
                String date = scanner.nextLine();

                MongoDB mongo1 = new MongoDB();
                return mongo1.showAppointmentByPatientIDAndDate(patientID, date);
            case 3:
                appointmentDashboard(docUserName);
                return "";
            default:
                System.out.println(Chalk.on("Invalid choice").red());
                System.out.println();
                showAppointment(docUserName);
                return "";
        }
    }

    public void updateAppointment(String docUserName) {
        misc.clear();
        System.out.println(Chalk.on("First let's find the appointment you want to update.").yellow());
        String appointmentID = showAppointment(docUserName);
        Scanner scanner = new Scanner(System.in);

        System.out.println(Chalk.on("What field you want to update:").yellow());
        System.out.println(Chalk.on("1. Date").yellow());
        System.out.println(Chalk.on("2. Start time and End time").yellow());
        System.out.println(Chalk.on("3. Reason").yellow());
        System.out.println(Chalk.on("4. Go back").yellow());

        System.out.println(Chalk.on("Enter your choice: ").green());
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                System.out.println(
                        Chalk.on("Enter the new date of the appointment, please enter in DD-MM-YYYY format:").green());
                String date = scanner.nextLine();
                // date regex check
                if (!date.matches("^(0[1-9]|[12][0-9]|3[01])[-](0[1-9]|1[012])[-](20[0-9][0-9])")) {
                    System.out.println(Chalk.on("Invalid date").red());
                    appointmentDashboard(docUserName);
                }
                MongoDB mongo = new MongoDB();
                mongo.updateAppointment(appointmentID, "date", date);
                break;
            case 2:
                System.out.println("Enter the new start time, please enter in 24-hour format (13:00) : ");
                String startTime = scanner.nextLine();
                // time regex check
                if (!startTime.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                    System.out.println(Chalk.on("Invalid time").red());
                    appointmentDashboard(docUserName);
                }
                System.out.println("Enter the new end time, please enter in 24-hour format (13:00) : ");
                String endTime = scanner.nextLine();
                // time regex check
                if (!endTime.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
                    System.out.println(Chalk.on("Invalid time").red());
                    appointmentDashboard(docUserName);
                }
                MongoDB mongo1 = new MongoDB();
                mongo1.updateAppointment(appointmentID, "startTime", startTime);
                mongo1.updateAppointment(appointmentID, "endTime", endTime);
                break;
            case 3:
                System.out.println(Chalk.on("Enter the new reason for the appointment:").green());
                String reason = scanner.nextLine();
                MongoDB mongo2 = new MongoDB();
                mongo2.updateAppointment(appointmentID, "reason", reason);
                break;
            case 4:
                appointmentDashboard(docUserName);
                break;
            default:
                System.out.println(Chalk.on("Invalid choice").red());
                System.out.println();
                updateAppointment(docUserName);
                break;
        }

        System.out.println(Chalk.on("Appointment updated successfully").bgGreen());
        misc.pressEnterToContinue();
    }

    public void deleteAppointment(String docUserName) {
        misc.clear();
        System.out.println(Chalk.on("First let's find the appointment you want to delete.").yellow());
        String appointmentID = showAppointment(docUserName);
        MongoDB mongo = new MongoDB();
        mongo.deleteAppointment(appointmentID);
        System.out.println(Chalk.on("Appointment deleted successfully").bgGreen());
        misc.pressEnterToContinue();
    }
}
