package com.carmenne.web.controllers;

import com.carmenne.backend.persistence.domain.backend.PasswordResetToken;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.service.EmailService;
import com.carmenne.backend.service.PasswordResetTokenService;
import com.carmenne.backend.service.UserService;
import com.carmenne.utils.EmailUtils;
import com.carmenne.utils.UserUtils;
import com.carmenne.web.i18n.I18NService;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
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
  private static final String CHANGE_PASSWORD_VIEW = "forgotmypassword/changepassword";
  private static final String PASSWORD_RESET_ATRRIBUTE_NAME = "passwordReset";
  private static final String MESSAGE_ATTRIBUTE_NAME = "message";

  @Value("${webmaster.email}")
  private String webmasterEmail;

  @Autowired
  private PasswordResetTokenService passwordResetTokenService;

  @Autowired
  private I18NService i18NService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private UserService userService;

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


  @RequestMapping(value = CHANGE_PASSOWRD_PATH, method = RequestMethod.GET)
  public String changeUserPasswordGet(@RequestParam("id") long id,
      @RequestParam("token") String token,
      Locale locale,
      ModelMap modelMap) {

    if (StringUtils.isEmpty(token) || id == 0) {
      LOG.error("Invalid user id {} or token value {}", id, token);
      modelMap.addAttribute(PASSWORD_RESET_ATRRIBUTE_NAME, "false");
      modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Invalid user id or token value");
      return CHANGE_PASSWORD_VIEW;
    }

    PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);

    if (null == passwordResetToken) {
      LOG.warn("A token could not be foudn with the value {}", token);
      modelMap.addAttribute(PASSWORD_RESET_ATRRIBUTE_NAME, "false");
      modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Token not found");
      return CHANGE_PASSWORD_VIEW;
    }

    User user = passwordResetToken.getUser();

    if (user.getId() != id) {
      LOG.error("The user id {} passed as parameter does not match the user id {}"
          + "associated with the token {}", user.getId(), id, token);
      modelMap.addAttribute(PASSWORD_RESET_ATRRIBUTE_NAME, "false");
      modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME,
          i18NService.getMessage("resetPassword.token.invalid", locale));
      return CHANGE_PASSWORD_VIEW;
    }

    if (LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.getExpiryDate())) {
      LOG.error("The token {} has expired", token);
      modelMap.addAttribute(PASSWORD_RESET_ATRRIBUTE_NAME, "false");
      modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME,
          i18NService.getMessage("resetPassword.token.expired", locale));
      return CHANGE_PASSWORD_VIEW;
    }

    modelMap.addAttribute("principalId", user.getId());

    // Ok to proceed, We authenticate the user so that in the POST request we can check if
    // the user is authenticated

    Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
        user.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);

    return CHANGE_PASSWORD_VIEW;
  }


  @RequestMapping(value = CHANGE_PASSOWRD_PATH, method = RequestMethod.POST)
  public String changeUserPasswordGet(@RequestParam("principal_id") long userId,
      @RequestParam("password") String password,
      ModelMap modelMap) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (null == authentication) {
      LOG.error("A unauthenticated user tried to reset the password POST method");
      modelMap.addAttribute(PASSWORD_RESET_ATRRIBUTE_NAME, "false");
      modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME,
          "You are not authorized to perform this request.");
    }

    User user = (User) authentication.getPrincipal();
    if (user.getId() != userId) {
      LOG.error("Security breach! User {} is trying to make a change password request "
          + "on behalf of user {}", user.getId(), userId);
      modelMap.addAttribute(PASSWORD_RESET_ATRRIBUTE_NAME, "false");
      modelMap.addAttribute(MESSAGE_ATTRIBUTE_NAME,
          "You are not authorized to perform this request.");

    }

    userService.updateUserPassword(userId, password);
    LOG.info("Password saved successfully for user {}", user.getUsername());
    modelMap.addAttribute(PASSWORD_RESET_ATRRIBUTE_NAME, "true");

    return CHANGE_PASSWORD_VIEW;
  }
}
