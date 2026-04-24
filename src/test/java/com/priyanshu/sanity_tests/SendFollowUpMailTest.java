package com.priyanshu.sanity_tests;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
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

@Test(groups = {"Api"})
public class SendFollowUpMailTest extends BaseTest {

    private static final String ATTACHMENT_FILE_PATH = INPUT_DIR + "fibc/FIBCBags.txt";
    private static final String CSV_FILE_PATH = INPUT_DIR + "fibc/CustomerData.csv";
    private static final String FOLLOWUP_FILE_PATH = INPUT_DIR + "fibc/followup.txt";

    String to = null;
    String name;
    String from = "me";
    String subject = "Welcome to FIBC";
    String replyBody;
    String bodyText = null;
    String messageId = null;
    String threadId;

    public void sendFollowupEmail() throws Exception {

        File attachmentFile;
        String defaultBodyText = Files.readString(Paths.get(FOLLOWUP_FILE_PATH));

        InboxAssistant inboxAssistant = new InboxAssistant();
        Gmail service = inboxAssistant.getGmailService();

        try {
            List<EmailData> emailList = inboxAssistant.readEmailDataUsingOpenCSV(CSV_FILE_PATH);
            for (EmailData emailData : emailList) {
                to = emailData.getSendTo();
                name = emailData.getName();
                replyBody = defaultBodyText.replaceFirst("Name", name);
                attachmentFile = new File(ATTACHMENT_FILE_PATH);

                ListMessagesResponse response = service.users().messages().list(from).setLabelIds(Collections.singletonList("SENT")).setQ("to:" + to).setMaxResults(1L).execute();
                for (Message message : response.getMessages()) {
                    messageId = message.getId();
                    System.out.println("Message ID: " + messageId);
                    Message originalMessage = service.users().messages()
                            .get(from, messageId)
                            .setFormat("full")
                            .execute();
                    String originalBody = inboxAssistant.getPlainTextFromMessage(originalMessage);
                    bodyText = replyBody + "\n" +
                            originalBody;
                    System.out.println(bodyText);
                }
                Message message = service.users().messages().get(from, messageId).setFormat("metadata").execute();
                threadId = message.getThreadId();
                if (threadId.equalsIgnoreCase(messageId)) {
                    String messageId = message.getPayload().getHeaders().stream()
                            .filter(h -> "Message-ID".equalsIgnoreCase(h.getName()))
                            .map(MessagePartHeader::getValue)
                            .findFirst()
                            .orElse(null);

                    String replyTo = emailData.getSendTo();
                    String replySubject = "Re: " + subject;

                    MimeMessage replyEmail = inboxAssistant.createReplyEmail(service, from, replyTo, replySubject, bodyText, messageId,attachmentFile);
                    inboxAssistant.sendReply(service, from, replyEmail, threadId);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        getReport().TestData.Description = "Verify Followup Mail Test";
        var status = TryAssert(() -> Assert.assertEquals(messageId, threadId));
        getReport().AddEvidence(new TestEvidence() {{
            Expected = "Verify Followup Mail Test";
            Actual = "Email did" + (status == TestStatus.Passed ? " " : " not ") + "sent";
            StepStatus = status;
            Details = "Validate Followup Mail test";
            StepName = "Test Followup Email";
            TestType = com.priyanshu.model.TestType.Api;
        }});
    }
}
