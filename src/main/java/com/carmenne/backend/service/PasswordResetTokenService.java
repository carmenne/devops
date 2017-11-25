package com.carmenne.backend.service;

import com.carmenne.backend.persistence.domain.backend.PasswordResetToken;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.repositories.PasswordResetTokenRepository;
import com.carmenne.backend.persistence.repositories.UserRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetTokenService
{
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordResetTokenRepository passwordResetTokenRepository;

  @Value("${token.expiration.length.minutes}")
  private int expirationTimeInMinutes;

  /**
   * The application logger
   */
  private static final Logger LOG = LoggerFactory.getLogger(PasswordResetTokenService.class);


  /**
   * Retrieves the Password Reset Token for the given token id.
   * @param token, The token id
   * @return A Password Reset token if onw was found, null otherwise
   */
  public PasswordResetToken findByToken(String token) {
    return passwordResetTokenRepository.findByToken(token);
  }


  /**
   * Return a Password Reset Token for a given email
   * @param email of the user that wants to reset the password
   * @return a Password Reset Token if the user with the email is found
   */
  @Transactional
  public PasswordResetToken createPasswordResetTokenForEmail(String email) {

    PasswordResetToken passwordResetToken = null;

    User user = userRepository.findByEmail(email);

    if (null != user) {

      String token = UUID.randomUUID().toString();
      LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

      passwordResetToken = new PasswordResetToken(user,
          token,
          now,
          expirationTimeInMinutes);

      passwordResetTokenRepository.save(passwordResetToken);
      LOG.debug("Successfully created token {} for user email {}",
          token,
          email);
    } else {
      LOG.warn("User with email {} could not be found", email);
    }

    return passwordResetToken;

  }

}
