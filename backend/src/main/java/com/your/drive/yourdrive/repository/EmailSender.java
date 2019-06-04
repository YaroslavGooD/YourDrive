package com.your.drive.yourdrive.repository;

public interface EmailSender {
    void sendEmail(String to, String subject, String content);
}
