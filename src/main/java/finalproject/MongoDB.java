package finalproject;

import java.util.Scanner;

import org.bson.Document;

import com.github.tomaslanger.chalk.Chalk;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB {

    private MongoClient mongoClient;
    private MongoDatabase database;

    Misc misc = new Misc();

    public MongoDB() {
        mongoClient = new MongoClient("localhost", 27017);
        database = mongoClient.getDatabase("finalproject");
    }

    public void getCollection(String collectionName) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection(collectionName);
        // print collection
        collection.find();
    }

    public void addData(String collectionName, Document data) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection(collectionName);
        // add data to collection
        collection.insertOne(data);
    }

    public Boolean findUsername(String collectionName, String username) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection(collectionName);
        // find usernames
        if (collection.find(new Document("username", username)).first() != null) {
            return true;
        }
        return false;
    }

    public Document getDoctorByUsername(String username) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("doctors");
        // find usernames
        return collection.find(new Document("username", username)).first();
    }

    // get patient count
    public int getPatientCount() {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("patients");
        // get count
        return (int) collection.count();
    }

    // add new patient
    public void addNewPatient(String patientID, String name, String age, String email, String phone) {

        // create new document
        Document patient = new Document();
        // add data to document
        patient.put("patientID", patientID);
        patient.put("name", name);
        patient.put("age", age);
        patient.put("email", email);
        patient.put("phone", phone);
        // add document to collection
        addData("patients", patient);
        System.out.println(Chalk.on("Patient added successfully!").bgGreen());

    }

    // show patient
    public String showPatient(String string) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("patients");
        // find patient
        Document patient = null;
        patient = collection.find(new Document("patientID", string)).first();

        if (patient == null) {
            patient = collection.find(new Document("email", string)).first();
        }
        if (patient == null) {
            patient = collection.find(new Document("phone", string)).first();
        }

        if (patient == null) {
            misc.clear();
            System.out.println(Chalk.on("No patient found").bgRed());
            misc.pressEnterToContinue();
            return "";
        } else {
            misc.clear();
            System.out.println(Chalk.on("Patient found").bgGreen());
            System.out.println(Chalk.on("patientID: " + patient.get("patientID")).yellow());
            System.out.println(Chalk.on("name: " + patient.get("name")).yellow());
            System.out.println(Chalk.on("age: " + patient.get("age")).yellow());
            System.out.println(Chalk.on("email: " + patient.get("email")).yellow());
            System.out.println(Chalk.on("phone: " + patient.get("phone")).yellow());
            return (String) patient.get("patientID");
        }
    }

    public void updatePatient(String input, String key, String value) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("patients");
        // find patient
        Document patient = collection.find(new Document("patientID", input)).first();

        // update patient
        patient.put(key, value);
        // update document
        collection.updateOne(new Document("patientID", input), new Document("$set", patient));

    }

    // check patient ID
    public Boolean checkPatient(String patientID) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("patients");
        // find patient
        Document patient = collection.find(new Document("patientID", patientID)).first();
        // check if patient exists
        if (patient == null) {
            return false;
        } else {
            return true;
        }
    }

    // get appointment count
    public int getAppointmentCount() {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("appointments");
        // get count
        return (int) collection.count();
    }

    public void createAppointment(String appointmentID, String docUserName, String patientID, String date,
            String startTime, String endTime,
            String reason) {

        // create new document
        Document appointment = new Document();
        // add data to document
        appointment.put("appointmentID", appointmentID);
        appointment.put("docUserName", docUserName);
        appointment.put("patientID", patientID);
        appointment.put("date", date);
        appointment.put("startTime", startTime);
        appointment.put("endTime", endTime);
        appointment.put("reason", reason);
        // add document to collection
        addData("appointments", appointment);

        misc.clear();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want to send an email to the patient? (y/n)");
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            Misc misc = new Misc();

            String appointmentData = "Appointment ID: " + appointment.get("appointmentID") + "\n"
                    + "Date: " + appointment.get("date") + "\n"
                    + "Start Time: " + appointment.get("startTime") + "\n"
                    + "End Time: " + appointment.get("endTime") + "\n"
                    + "Reason: " + appointment.get("reason");

            String patientEmail = getPatientEmail(appointment.get("patientID").toString());

            misc.sendEmail("Appointment added", patientEmail, appointmentData);
        }

        System.out.println("Appointment created");
        System.out.println(Chalk.on("appointmentID: " + appointment.get("appointmentID")).blue());
        misc.pressEnterToContinue();

    }

    // show today's all appointments by doctor username
    public void showTodayAppointments(String docUserName, String date) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("appointments");
        // find appointments
        FindIterable<Document> appointments = collection.find(new Document("docUserName", docUserName)
                .append("date", date));

        // if no appointments
        if (appointments.first() == null) {
            System.out.println(Chalk.on("No appointments").red());
        } else {
            misc.clear();
            System.out.println(Chalk.on("Today's appointments:").bgBlue());

            // print appointments
            for (Document appointment : appointments) {
                System.out.println();
                System.out.println(Chalk.on("appointmentID: " + appointment.get("appointmentID")).blue());
                System.out.println(Chalk.on("docUserName: " + appointment.get("docUserName")).yellow());
                System.out.println(Chalk.on("patientID: " + appointment.get("patientID")).yellow());
                System.out.println(Chalk.on("date: " + appointment.get("date")).yellow());
                System.out.println(Chalk.on("startTime: " + appointment.get("startTime")).yellow());
                System.out.println(Chalk.on("endTime: " + appointment.get("endTime")).yellow());
                System.out.println(Chalk.on("reason: " + appointment.get("reason")).yellow());
            }
        }

        misc.pressEnterToContinue();
    }

    // show appointment by appointmentID
    public String showAppointmentByID(String appointmentID) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("appointments");
        // find appointment
        Document appointment = collection.find(new Document("appointmentID", appointmentID)).first();
        misc.clear();

        if (appointment == null) {
            System.out.println("No appointment found");
            misc.pressEnterToContinue();
            return "";
        } else {
            System.out.println("Appointment found");
            System.out.println("appointmentID: " + appointment.get("appointmentID"));
            System.out.println("docUserName: " + appointment.get("docUserName"));
            System.out.println("patientID: " + appointment.get("patientID"));
            System.out.println("date: " + appointment.get("date"));
            System.out.println("startTime: " + appointment.get("startTime"));
            System.out.println("endTime: " + appointment.get("endTime"));
            System.out.println("reason: " + appointment.get("reason"));
            return (String) appointment.get("appointmentID");
        }
    }

    // show appointment by patientID and date
    public String showAppointmentByPatientIDAndDate(String patientID, String date) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("appointments");
        // find appointments
        FindIterable<Document> appointments = collection.find(new Document("patientID", patientID)
                .append("date", date));

        // if no appointments
        if (appointments.first() == null) {
            misc.clear();
            System.out.println("No appointments");
            misc.pressEnterToContinue();
            return "";
        } else {
            misc.clear();
            for (Document appointment : appointments) {
                System.out.println("appointmentID: " + appointment.get("appointmentID"));
                System.out.println("docUserName: " + appointment.get("docUserName"));
                System.out.println("patientID: " + appointment.get("patientID"));
                System.out.println("date: " + appointment.get("date"));
                System.out.println("startTime: " + appointment.get("startTime"));
                System.out.println("endTime: " + appointment.get("endTime"));
                System.out.println("reason: " + appointment.get("reason"));
            }
            misc.pressEnterToContinue();
            return appointments.first().get("appointmentID").toString();
        }
    }

    // update appointment
    public void updateAppointment(String appointmentID, String key, String value) {
        Scanner scanner = new Scanner(System.in);

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("appointments");
        // find appointment
        Document appointment = collection.find(new Document("appointmentID", appointmentID)).first();

        // update appointment
        appointment.put(key, value);
        // update document
        collection.updateOne(new Document("appointmentID", appointmentID), new Document("$set", appointment));
        // ask to send email
        System.out.println("Do you want to send an email to the patient? (y/n)");
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            // get patient email
            String patientEmail = getPatientEmail(appointment.get("patientID").toString());
            // send email
            Misc misc = new Misc();
            // change appointment data to string
            String appointmentData = "Appointment ID: " + appointment.get("appointmentID") + "\n"
                    + "Date: " + appointment.get("date") + "\n"
                    + "Start Time: " + appointment.get("startTime") + "\n"
                    + "End Time: " + appointment.get("endTime") + "\n"
                    + "Reason: " + appointment.get("reason");

            // send email
            misc.sendEmail("Appointment Updated", patientEmail, appointmentData);
        }
    }

    // get patient email
    public String getPatientEmail(String patientID) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("patients");
        // find patient
        Document patient = collection.find(new Document("patientID", patientID)).first();
        // return email
        return patient.get("email").toString();
    }

    // delete appointment
    public void deleteAppointment(String appointmentID) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("appointments");
        // only keep appointmentID
        collection.deleteOne(new Document("appointmentID", appointmentID));
        addData("appointments", new Document("appointmentID", appointmentID));

    }

    // add diagnosis
    public void addDiagnosis(String docUserName, String patientID, String appointmentID, String date,
            String diagnosis) {

        // create new document
        Document diagnosisDoc = new Document();
        // add data to document
        diagnosisDoc.put("docUserName", docUserName);
        diagnosisDoc.put("patientID", patientID);
        diagnosisDoc.put("appointmentID", appointmentID);
        diagnosisDoc.put("date", date);
        diagnosisDoc.put("diagnosis", diagnosis);
        // add document to collection
        addData("diagnoses", diagnosisDoc);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want to send an email to the patient? (y/n)");
        String answer = scanner.nextLine();
        if (answer.equals("y")) {
            // get patient email
            String patientEmail = getPatientEmail(patientID);
            // send email
            Misc misc = new Misc();
            // change appointment data to string
            String appointmentData = "Appointment ID: " + appointmentID + "\n"
                    + "Patient ID: " + patientID + "\n"
                    + "Date: " + date + "\n"
                    + "Diagnosis: " + diagnosis;

            // send email
            misc.sendEmail("New Diagnosis", patientEmail, appointmentData);
        }

        System.out.println("Diagnosis added");
        System.out.println(Chalk.on("appointmentID: " + diagnosisDoc.get("appointmentID")).blue());
    }

    public FindIterable<Document> getPreviousDiagnosis(String docUserName, String patientID) {

        // get collection from database
        MongoCollection<Document> collection = database.getCollection("diagnoses");
        // find diagnoses
        FindIterable<Document> diagnoses = collection.find(new Document("docUserName", docUserName)
                .append("patientID", patientID));
        // if no diagnoses
        if (diagnoses.first() == null) {
            System.out.println("No diagnoses");
            return null;
        }
        return diagnoses;
    }
}
