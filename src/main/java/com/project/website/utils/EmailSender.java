package com.project.website.utils;

import org.apache.commons.lang3.RandomStringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailSender {
    private static final String SENDER_EMAIL = "quizwebpasssender@gmail.com";
    private static final String SENDER_EMAIL_PASS = "ybplifvfbkiictoi";
    private static final String SENDER_EMAIL_NAME = "Quiz Webpage";
    private static final String EMAIL_SUBJECT = "Registration Success";

     public static synchronized String sendRandomPasswordToEmail(String useremail, String username) {
        String randomPassword = generateRandomPassword();
        Session session = initializeSession();
        sendEmail(session, useremail, username, randomPassword);
        return randomPassword;
    }

    private static String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        String pwd = RandomStringUtils.random( 15, characters );
        return pwd;
    }

    private static Session initializeSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_EMAIL_PASS);
            }
        };

        return Session.getInstance(props, auth);
    }

    private static void sendEmail(Session session, String useremail, String username, String randomPassword) {
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@example.com", SENDER_EMAIL_NAME));
            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
            msg.setSubject(EMAIL_SUBJECT, "UTF-8");
            String emailBody = emailText(username,randomPassword);
            msg.setText(emailBody, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(useremail, false));
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String emailText(String username, String randomPassword){
        StringBuilder emailText = new StringBuilder();
        emailText.append("Welcome Dear " + username + "!" + "\n" + "\n");
        emailText.append("You have successfully been registered. ");
        emailText.append("Your current password is: " + randomPassword + "\n");
        emailText.append("You can change the password anytime from your profile. Thank you!");
        return emailText.toString();
    }

}
