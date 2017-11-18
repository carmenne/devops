package com.carmenne.web.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class I18nService {

    @Autowired
    private MessageSource messageSource;

    /**
     * Return a message for the given message id and
     * default locale as in the session context
     * @param messageId The key as in the message file
     */
    public String getMessage(String messageId){
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(locale, messageId);
    }

    /**
     * Return a message for the given message and locale
     * @param locale The locale
     * @param messageId The key as in the messages file
     */
    private String getMessage(Locale locale, String messageId) {

        return messageSource.getMessage(messageId, null, locale);
    }
}
