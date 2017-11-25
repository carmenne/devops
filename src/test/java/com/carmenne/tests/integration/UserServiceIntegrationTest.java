package com.carmenne.tests.integration;

import com.carmenne.DevopsApplication;
import com.carmenne.backend.persistence.domain.backend.User;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.util.Assert.notNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsApplication.class)
public class UserServiceIntegrationTest extends  AbstractServiceIntegrationTest{


    @Rule
    public TestName testName = new TestName();

    @Test
    public void testCreateNewUser() throws Exception {

        User user = createUser(testName);

        notNull(user);
        notNull(user.getId());


    }

    @Test
    public void testUpdateUserPasswordService() throws Exception {

        User user = createUser(testName);
        String password = UUID.randomUUID().toString();

        userService.updateUserPassword(user.getId(), password);
        User updatedUser = userService.findUserById(user.getId());

        Assert.assertNotNull(updatedUser);
        Assert.assertTrue(passwordEncoder.matches(password, updatedUser.getPassword()));

    }

}
