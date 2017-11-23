package com.carmenne.config;

import com.carmenne.backend.service.AbstractEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService {

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);

    @Autowired
    private MailSender mailSender;

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage) {

        LOG.debug("Sending email for {}", simpleMailMessage);
        mailSender.send(simpleMailMessage);
        LOG.info("Email sent.");
    }
}
