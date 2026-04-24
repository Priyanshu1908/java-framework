package com.priyanshu.lib;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.opencsv.CSVReader;
import com.priyanshu.lib.model.EmailData;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.priyanshu.lib.BaseTest.CREDENTIALS_FILE_PATH;
import static com.priyanshu.lib.BaseTest.INPUT_DIR;

public class InboxAssistant {

    private static final String APPLICATION_NAME = "Inbox Assistant";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/gmail.modify",
            "https://www.googleapis.com/auth/gmail.send"
    );
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final String FILE_PATH = INPUT_DIR + "fibc/FIBCBags.txt";
    private static final String CSV_FILE_PATH = INPUT_DIR + "fibc/CustomerData.csv";

    public Gmail getGmailService() throws Exception {
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

    public Message createEmail(String to, String from, String subject, String bodyText) throws Exception {
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

    public MimeMessage createEmailWithAttachment(String to,
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

    public String sendMessage(Gmail service, String userId, MimeMessage emailContent) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        Message message = new Message();
        message.setRaw(encodedEmail);

        message = service.users().messages().send(userId, message).execute();
        return message.getId();
    }

    public void sendEmail(Gmail service, String toEmailAddress, String subject, String bodyText) throws Exception {
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

        Message message = new Message();
        message.setRaw(encodedEmail);

        message = service.users().messages().send("me", message).execute();
        System.out.println("Email sent with ID: " + message.getId());
    }

    public void sendDraft(Gmail service, String userId, String draftId) throws Exception {
        Draft draft = service.users().drafts().get(userId, draftId).execute();
        Message sentMessage = service.users().drafts().send(userId, draft).execute();
        System.out.println("Draft sent! Message ID: " + sentMessage.getId());
    }

    public List<EmailData> readEmailDataFromCsv(String filePath) throws Exception {
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

    public List<EmailData> readEmailDataUsingOpenCSV(String filePath) throws Exception {
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

    public MimeMessage createReplyEmail(Gmail service, String userId, String to,
                                               String subject, String bodyText, String messageId, File file) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("me"));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setText(bodyText);
        //email.setText(bodyText);
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

        email.setHeader("In-Reply-To", messageId);
        email.setHeader("References", messageId);

        return email;
    }

    public void sendReply(Gmail service, String userId, MimeMessage replyEmail, String threadId) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        replyEmail.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        Message message = new Message();
        message.setRaw(encodedEmail);
        message.setThreadId(threadId);

        Message sentMessage = service.users().messages().send(userId, message).execute();
        System.out.println("Replied! Message ID: " + sentMessage.getId());
    }

    public String getPlainTextFromMessage(Message message) throws Exception {
        if (message.getPayload().getParts() != null) {
            for (MessagePart part : message.getPayload().getParts()) {
                if (part.getMimeType().equals("text/plain")) {
                    byte[] bodyBytes = Base64.getDecoder().decode(part.getBody().getData());
                    return new String(bodyBytes, StandardCharsets.UTF_8);
                }
            }
        } else {
            byte[] bodyBytes = Base64.getDecoder().decode(message.getPayload().getBody().getData());
            return new String(bodyBytes, StandardCharsets.UTF_8);
        }
        return "";
    }
}
