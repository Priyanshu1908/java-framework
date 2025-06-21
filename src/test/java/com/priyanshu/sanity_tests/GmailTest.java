package com.priyanshu.sanity_tests;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.opencsv.CSVReader;
import com.priyanshu.lib.BaseTest;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class GmailTest extends BaseTest {

    private static final String APPLICATION_NAME = "Inbox Assistant";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/gmail.modify",
            "https://www.googleapis.com/auth/gmail.send"
    );
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final String FILE_PATH = INPUT_DIR + "fibc/FIBCBags.txt";
    private static final String CSV_FILE_PATH = INPUT_DIR + "fibc/CustomerData.csv";

    private static Gmail getGmailService() throws Exception {
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in)), SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
                new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user"))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static Message createEmail(String to, String from, String subject, String bodyText) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static MimeMessage createEmailWithAttachment(String to,
                                                        String from,
                                                        String subject,
                                                        String bodyText,
                                                        File file) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        // Create the message part
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setText(bodyText);

        // Create the attachment part
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        FileDataSource source = new FileDataSource(file);
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(file.getName());

        // Create multipart email
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        multipart.addBodyPart(attachmentBodyPart);

        // Set the content
        email.setContent(multipart);

        return email;
    }

    public static void sendMessage(Gmail service, String userId, MimeMessage emailContent) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        Message message = new Message();
        message.setRaw(encodedEmail);

        message = service.users().messages().send(userId, message).execute();
        System.out.println("Message sent with ID: " + message.getId());
    }

    public static void sendEmail(Gmail service, String toEmailAddress, String subject, String bodyText) throws Exception {
        // Set email properties
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        // Create Email
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("me")); // "me" refers to the authenticated user
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toEmailAddress));
        email.setSubject(subject);
        email.setText(bodyText);

        // Encode email
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        // Create Message object
        Message message = new Message();
        message.setRaw(encodedEmail);

        // Send Email
        message = service.users().messages().send("me", message).execute();
        System.out.println("Email sent with ID: " + message.getId());
    }

    public static void sendDraft(Gmail service, String userId, String draftId) throws Exception {
        Draft draft = service.users().drafts().get(userId, draftId).execute();
        Message sentMessage = service.users().drafts().send(userId, draft).execute();
        System.out.println("Draft sent! Message ID: " + sentMessage.getId());
    }

    public static class EmailData {
        private String sendTo;
        private String name;

        // Constructor
        public EmailData(String sendTo, String name) {
            this.sendTo = sendTo;
            this.name = name;
        }

        public String getSendTo() {
            return sendTo;
        }

        public String getName() {
            return name;
        }
    }

    public static List<EmailData> readEmailDataFromCsv(String filePath) throws Exception {
        List<EmailData> emailDataList = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        // Skip header row (index 0)
        for (int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split(",", -1); // -1 to include empty strings
            if (values.length >= 2) { // Ensure there are at least 3 columns
                String sendTo = values[0].trim();
                String name = values[1].trim();
                emailDataList.add(new EmailData(sendTo, name));
            }
        }
        return emailDataList;
    }

    public static List<EmailData> readEmailDataUsingOpenCSV(String filePath) throws Exception {
        List<EmailData> emailDataList = new ArrayList<>();
        CSVReader reader = new CSVReader(new FileReader(filePath));
        String[] nextLine;
        reader.readNext(); // skip header
        while ((nextLine = reader.readNext()) != null) {
            String sendTo = nextLine[0];
            String name = nextLine[1];
            emailDataList.add(new EmailData(sendTo, name));
        }
        reader.close();
        return emailDataList;
    }

    public static MimeMessage createReplyEmail(Gmail service, String userId, String to,
                                               String subject, String bodyText, String messageId) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("me")); // 'me' refers to authenticated user
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);

        // Important: Set headers to make it a reply
        email.setHeader("In-Reply-To", messageId);
        email.setHeader("References", messageId);

        return email;
    }

    public static void sendReply(Gmail service, String userId, MimeMessage replyEmail, String threadId) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        replyEmail.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        Message message = new Message();
        message.setRaw(encodedEmail);
        message.setThreadId(threadId); // Set thread ID to keep reply in the same conversation

        Message sentMessage = service.users().messages().send(userId, message).execute();
        System.out.println("Replied! Message ID: " + sentMessage.getId());
    }

    public static void main(String[] args) throws Exception {
        Gmail service = getGmailService();

        //Create Email Content
        String to = null;
        String name;
        String from = "me";
        String subject = "Welcome to FIBC";
        String defaultText = "Dear Name, \n\nWe provide FIBC bags. \nPFA. \n\nThanks and Reagrds,\nDivya Rathore";
        String bodyText = null;
        String messageId = null;

        try {
            List<EmailData> emailList = readEmailDataUsingOpenCSV(CSV_FILE_PATH);
            for (EmailData emailData : emailList) {
                to = emailData.getSendTo();
                name = emailData.getName();
                bodyText = defaultText.replaceFirst("Name", name);
                File file = new File(FILE_PATH);
//                MimeMessage email = createEmailWithAttachment(to, from, subject, bodyText, file);
//                sendMessage(service, "me", email);
            }

            ListMessagesResponse response = service.users().messages().list("me").setLabelIds(Collections.singletonList("SENT")).setQ("to:" + to).setMaxResults(1L).execute();
            for (Message message : response.getMessages()) {
                messageId = message.getId();  // This is a valid ID!
                System.out.println("Message ID: " + messageId);
            }
            Message message = service.users().messages().get("me", messageId).setFormat("metadata").execute();
            String threadId = message.getThreadId();
            for (EmailData emailData : emailList) {
                String replyTo = emailData.getSendTo(); // Original sender
                //String replySubject = "Re: Follow up Mail";
                String replyBodyText = "This is a follow-up reply in the same thread.";

                MimeMessage replyEmail = createReplyEmail(service, "me", replyTo, subject, replyBodyText, messageId);
                sendReply(service, "me", replyEmail, threadId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Create email and draft
//        Message message = createEmail(to, from, subject, bodyText);
//        Draft draft = new Draft();
//        draft.setMessage(message);

        // Create draft in Gmail
        //Draft createdDraft = service.users().drafts().create("me", draft).execute();
        //System.out.println("Draft ID: " + createdDraft.getId());
        //sendDraft(service, "me", createdDraft.getId());
        //sendEmail(service, to, subject, bodyText);
        //MimeMessage email = createEmailWithAttachment(to, from, subject, bodyText, file);
        //sendMessage(service, "me", email);
    }
}

