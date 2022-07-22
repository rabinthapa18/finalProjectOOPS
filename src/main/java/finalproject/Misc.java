package finalproject;

import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.github.tomaslanger.chalk.Chalk;

public class Misc {

    public void clear() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    public void pressEnterToContinue() {

        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println(Chalk.on("Press enter to continue").yellow());
        scanner.nextLine();
    }

    public void sendEmail(String subject, String to, String body) {

        // gmail SMTP
        String host = "smtp.gmail.com";

        // get system properties
        Properties properties = System.getProperties();

        // mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("final.project.2022.apr@gmail.com", "vcptrtytiveiqcfp");

            }

        });

        try {
            // mime message
            MimeMessage message = new MimeMessage(session);

            // from
            message.setFrom("Final_Project");

            // to
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // email header
            message.setSubject(subject);

            // email body
            message.setText(body);

            // Send message
            Transport.send(message);
            System.out.println(Chalk.on("Email sent.").blue());
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }
}
