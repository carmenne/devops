package com.carmenne.tests.unit;

import com.carmenne.utils.UserUtils;
import com.carmenne.web.controllers.ForgotMyPasswordController;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class UserUtilsUnitTest {

  private MockHttpServletRequest mockHttpServletRequest;

  @Before
  public void init() {
    mockHttpServletRequest = new MockHttpServletRequest();
  }

  @Test
  public void testPasswordResetEmailUrlConstruction(){

    mockHttpServletRequest.setServerPort(8181);

    String token = UUID.randomUUID().toString();
    long userId = 1234;

    String expectedUrl = "http://localhost:8181"
        + ForgotMyPasswordController.CHANGE_PASSOWRD_PATH + "?id=" + userId +
        "&token="+ token;

    String actualUrl = UserUtils.createUrl(mockHttpServletRequest, userId, token);

    Assert.assertEquals(expectedUrl, actualUrl);


  }
}
