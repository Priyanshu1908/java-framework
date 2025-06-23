package com.priyanshu.lib.model;

public class EmailData {
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