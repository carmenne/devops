package com.carmenne.tests.integration;

import com.carmenne.DevopsApplication;
import com.carmenne.backend.persistence.domain.backend.PasswordResetToken;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.service.PasswordResetTokenService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsApplication.class)
public class PasswordResetTokenServiceIntegrationTest
    extends AbstractServiceIntegrationTest{

  @Autowired
  private PasswordResetTokenService passwordResetTokenService;

  @Rule
  public TestName testName = new TestName();

  @Test
  public void testCreateNewTokenForUserEmail() throws Exception {

    User user = createUser(testName);

    PasswordResetToken passwordResetToken
        = passwordResetTokenService.createPasswordResetTokenForEmail(user.getEmail());

    Assert.assertNotNull(passwordResetToken);
    Assert.assertNotNull(passwordResetToken.getToken());

  }

  @Test
  public void testFindByToken() throws Exception {

    User user = createUser(testName);

    PasswordResetToken passwordResetToken
        = passwordResetTokenService.createPasswordResetTokenForEmail(user.getEmail());

    PasswordResetToken foundPasswordToken = passwordResetTokenService.findByToken(
        passwordResetToken.getToken());

    Assert.assertNotNull(foundPasswordToken);
    Assert.assertNotNull(foundPasswordToken.getToken());
    Assert.assertEquals(passwordResetToken.getToken(), foundPasswordToken.getToken());


  }

}
