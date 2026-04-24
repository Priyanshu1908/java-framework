package com.priyanshu.sanity_tests;

import com.priyanshu.lib.InboxAssistant;

import javax.swing.*;
import java.awt.*;

public class GmailUITest {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Gmail API - Simple UI");
        frame.setSize(400, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JButton sendButton = new JButton("Send Introduction Email");
        JButton replyButton = new JButton("Send Follow Up Reply");

        // Send Email Action
        sendButton.addActionListener(e -> {
            try {
                SendEmailWithAttachmentTest sendEmail = new SendEmailWithAttachmentTest();
                sendEmail.sendIntroductionEmail();  // Backend method with all data
                JOptionPane.showMessageDialog(frame, "Email Sent Successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Send Failed: " + ex.getMessage());
            }
        });

        // Send Reply Action
        replyButton.addActionListener(e -> {
            try {
                SendFollowUpMailTest sendReply = new SendFollowUpMailTest();
                sendReply.sendFollowupEmail();  // Backend method with all data
                JOptionPane.showMessageDialog(frame, "Reply Sent Successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Reply Failed: " + ex.getMessage());
            }
        });

        frame.add(sendButton);
        frame.add(replyButton);

        frame.setVisible(true);
    }
}
