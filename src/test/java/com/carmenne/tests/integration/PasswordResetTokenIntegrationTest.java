package com.carmenne.tests.integration;

import com.carmenne.DevopsApplication;
import com.carmenne.backend.persistence.domain.backend.PasswordResetToken;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.repositories.PasswordResetTokenRepository;
import com.carmenne.backend.persistence.repositories.UserRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsApplication.class)
public class PasswordResetTokenIntegrationTest extends AbstractIntegrationTest {

  @Rule
  public TestName testName = new TestName();

  @Value("${token.expiration.length.minutes}")
  private int expirationTimeInMinutes;

  @Autowired
  private PasswordResetTokenRepository passwordResetTokenRepository;

  @Autowired
  private UserRepository userRepository;

  @Before
  public void init() {
    Assert.assertFalse(expirationTimeInMinutes == 0);
  }

  @Test
  public void testTokenExpirationLength() throws Exception {

    User user = createNewUser(testName);

    Assert.assertNotNull(user);
    Assert.assertNotNull(user.getId());

    LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
    String token = UUID.randomUUID().toString();

    LocalDateTime expirationTime = now.plusMinutes(expirationTimeInMinutes);

    PasswordResetToken passwordResetToken = createPasswordResetToken(
        token, user, now);

    LocalDateTime actualTime = passwordResetToken.getExpiryDate();
    Assert.assertNotNull(actualTime);
    Assert.assertEquals(expirationTime, actualTime);


  }

  @Test
  public void testFindTokenByTokenValue() throws Exception {

    User user = createNewUser(testName);
    LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
    String token = UUID.randomUUID().toString();

    createPasswordResetToken(token, user, now);

    PasswordResetToken passwordResetToken =
        passwordResetTokenRepository.findByToken(token);

    Assert.assertNotNull(passwordResetToken);
    Assert.assertNotNull(passwordResetToken.getId());
    Assert.assertNotNull(passwordResetToken.getUser());

  }

  @Test
  public void testDeleteToken() throws Exception {
    User user = createNewUser(testName);
    LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
    String token = UUID.randomUUID().toString();

    PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);

    long id = passwordResetToken.getId();
    passwordResetTokenRepository.delete(id);

    PasswordResetToken shouldNotExist = passwordResetTokenRepository.findOne(id);
    Assert.assertNull(shouldNotExist);
  }


  @Test
  public void testCascadeDeleteFromUserEntity() {

    User user = createNewUser(testName);
    LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
    String token = UUID.randomUUID().toString();

    PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);

    userRepository.delete(user.getId());
    Set<PasswordResetToken> shouldBeEmpty =
        passwordResetTokenRepository.findAllByUserId(user.getId());

    Assert.assertTrue(shouldBeEmpty.isEmpty());
  }


  @Test
  public void testMultipleTokensAreReturnedWhenQueryByUserId() throws Exception {

    User user = createNewUser(testName);
    LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

    String token1 = UUID.randomUUID().toString();
    String token2 = UUID.randomUUID().toString();
    String token3 = UUID.randomUUID().toString();

    Set<PasswordResetToken> tokens = new HashSet<>();
    tokens.add(createPasswordResetToken(token1, user, now));
    tokens.add(createPasswordResetToken(token2, user, now));
    tokens.add(createPasswordResetToken(token3, user, now));

    passwordResetTokenRepository.save(tokens);

    Set<PasswordResetToken> actualTokens =
        passwordResetTokenRepository.findAllByUserId(user.getId());

    Assert.assertEquals(tokens.size(), actualTokens.size());

    List<PasswordResetToken> tokensAsList = tokens.stream().collect(Collectors.toList());
    List<PasswordResetToken> actualTokensAsList = actualTokens.stream().collect(Collectors.toList());


    Assert.assertEquals(tokensAsList, actualTokensAsList);


  }

  //------------------> Private methods

  private PasswordResetToken createPasswordResetToken(String token, User user, LocalDateTime now) {

    PasswordResetToken passwordResetToken =
        new PasswordResetToken(user, token, now, expirationTimeInMinutes);
    passwordResetTokenRepository.save(passwordResetToken);

    Assert.assertNotNull(passwordResetToken.getId());

    return passwordResetToken;
  }

}
