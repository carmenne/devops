package com.carmenne.backend.service;

import com.carmenne.web.domain.frontend.FeedbackPojo;
import org.springframework.mail.SimpleMailMessage;

/** Contract for email service */

public interface EmailService {

    /**
     * Send the email with the contents from feedbackPojo
     * @param feedbackPojo the Feedback Pojo
     */
    public void sendFeedbackEmail(FeedbackPojo feedbackPojo);

    /**
     * Send an wmail with the content of a SimpleMailMessage
     * @param simpleMailMessage the object containing the email content
     */
    public void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage);
}
