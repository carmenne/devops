package com.carmenne.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

/**
 * To be used in dev profile
 */
public class MockEmailService extends AbstractEmailService {

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage) {
        LOG.debug("Simulating an email service...");
        LOG.info(simpleMailMessage.toString());
    }
}
