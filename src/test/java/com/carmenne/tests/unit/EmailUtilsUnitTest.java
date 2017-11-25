package com.carmenne.tests.unit;

import com.carmenne.utils.EmailUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;

public class EmailUtilsUnitTest {

  @Test
  public void getSimpleMailMessage() throws Exception {

    SimpleMailMessage simpleMailMessage =
        EmailUtils.getSimpleMailMessage("", "", "", "");

    Assert.assertNotNull(simpleMailMessage);

    Assert.assertNotNull(simpleMailMessage.getTo());
    Assert.assertNotNull(simpleMailMessage.getFrom());
    Assert.assertNotNull(simpleMailMessage.getSubject());
    Assert.assertNotNull(simpleMailMessage.getText());

  }

}
