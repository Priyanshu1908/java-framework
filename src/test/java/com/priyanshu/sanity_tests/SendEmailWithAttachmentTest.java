package com.priyanshu.sanity_tests;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.priyanshu.lib.BaseTest;
import com.priyanshu.lib.InboxAssistant;
import com.priyanshu.lib.model.EmailData;
import com.priyanshu.model.TestEvidence;
import com.priyanshu.model.TestStatus;
import org.testng.annotations.Test;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static com.priyanshu.lib.Utilities.TryAssert;

@Test(groups = {"Api"}, enabled = true)
public class SendEmailWithAttachmentTest extends BaseTest {

    private static final String ATTACHMENT_FILE_PATH = INPUT_DIR + "fibc/FIBCBags.txt";
    private static final String CSV_FILE_PATH = INPUT_DIR + "fibc/CustomerData.csv";
    private static final String BODY_FILE_PATH = INPUT_DIR + "fibc/introduction.txt";

    String to = null;
    String name;
    String from = "me";
    String subject = "Welcome to FIBC";
    String bodyText = null;
    String sentMessageID;
    String messageId = null;

    public void sendIntroductionEmail() throws Exception {

        File attachmentFile;
        String defaultBodyText = Files.readString(Paths.get(BODY_FILE_PATH));

        InboxAssistant inboxAssistant = new InboxAssistant();
        Gmail service = inboxAssistant.getGmailService();

        try {
            List<EmailData> emailList = inboxAssistant.readEmailDataUsingOpenCSV(CSV_FILE_PATH);
            for (EmailData emailData : emailList) {
                to = emailData.getSendTo();
                name = emailData.getName();
                bodyText = defaultBodyText.replaceFirst("Name", name);
                attachmentFile = new File(ATTACHMENT_FILE_PATH);
                MimeMessage email = inboxAssistant.createEmailWithAttachment(to, from, subject, bodyText, attachmentFile);
                sentMessageID = inboxAssistant.sendMessage(service, "me", email);

                ListMessagesResponse response = service.users().messages().list("me").setLabelIds(Collections.singletonList("SENT")).setQ("to:" + to).setMaxResults(1L).execute();
                for (Message message : response.getMessages()) {
                    messageId = message.getId();
                    System.out.println("Message ID: " + messageId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getReport().TestData.Description = "Verify Send Intro Email Test";
        var status = TryAssert(() -> Assert.assertEquals(sentMessageID, messageId));
        getReport().AddEvidence(new TestEvidence() {{
            Expected = "Verify Send Intro Email Test";
            Actual = "Email did" + (status == TestStatus.Passed ? " " : " not ") + "sent";
            StepStatus = status;
            Details = "Validate Send Intro Email test";
            StepName = "Test Send Intro Email";
            TestType = com.priyanshu.model.TestType.Api;
        }});
    }
}
