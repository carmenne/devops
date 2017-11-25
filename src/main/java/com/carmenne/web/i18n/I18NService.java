package com.carmenne.web.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class I18NService {


    /**
     * The application logger
     */
    private static final Logger LOG
            = LoggerFactory.getLogger(I18NService.class);


    @Autowired
    private MessageSource messageSource;

    /**
     * Return a message for the given message id and
     * default locale as in the session context
     * @param messageId The key as in the message file
     */
    public String getMessage(String messageId){

        LOG.info("Return i28n text for message {}", messageId);
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(messageId, locale);
    }

    /**
     * Return a message for the given message and locale
     * @param messageId The key as in the messages file
     * @param locale The locale
     */
    public String getMessage(String messageId, Locale locale) {
        return messageSource.getMessage(messageId, null, locale);
    }
}
