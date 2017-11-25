package com.carmenne.utils;

import com.carmenne.backend.service.EmailService;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by carmen on 25-11-17.
 */
public class EmailUtils {

  private EmailUtils() {
    throw new AssertionError("non-instantiable");
  }

  public static SimpleMailMessage getSimpleMailMessage(String to,
      String from,
      String subject,
      String text) {

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    simpleMailMessage.setTo(to);
    simpleMailMessage.setFrom(from);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(text);

    return simpleMailMessage;
  }

}
