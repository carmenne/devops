package com.carmenne.web.controllers;

import com.carmenne.backend.persistence.domain.backend.PasswordResetToken;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.service.EmailService;
import com.carmenne.backend.service.PasswordResetTokenService;
import com.carmenne.utils.EmailUtils;
import com.carmenne.utils.UserUtils;
import com.carmenne.web.i18n.I18NService;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotMyPasswordController {

  public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";
  public static final String CHANGE_PASSOWRD_PATH = "/changeuserpassword";
  /**
   * The application logger
   */
  private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);
  private static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailform";
  private static final String EMAIL_SENT_KEY = "mailSent";
  private static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";

  @Value("${webmaster.email}")
  private String webmasterEmail;

  @Autowired
  private PasswordResetTokenService passwordResetTokenService;

  @Autowired
  private I18NService i18NService;

  @Autowired
  private EmailService emailService;

  @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
  public String forgotPassword() {
    return EMAIL_ADDRESS_VIEW_NAME;
  }

  @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
  public String forgotPasswordPost(HttpServletRequest request,
      @RequestParam("email") String email,
      ModelMap model) {

    PasswordResetToken passwordResetToken =
        passwordResetTokenService.createPasswordResetTokenForEmail(email);

    if (null == passwordResetToken) {
      LOG.warn("Couldn't find a password reset token for email {}", email);
    } else {

      User user = passwordResetToken.getUser();
      String token = passwordResetToken.getToken();

      String resetUrl = UserUtils.createUrl(request, user.getId(), token);

      String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME,
          request.getLocale());

      emailService.sendGenericEmailMessage(
          EmailUtils.getSimpleMailMessage(email,
              webmasterEmail,
              "How to reset password",
              emailText + System.lineSeparator() + resetUrl));

      LOG.info("Password reset URL {}", resetUrl);

    }

    model.addAttribute(EMAIL_SENT_KEY, "true");

    return EMAIL_ADDRESS_VIEW_NAME;
  }

}
