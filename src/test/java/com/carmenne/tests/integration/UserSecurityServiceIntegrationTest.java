package com.carmenne.tests.integration;

import com.carmenne.DevopsApplication;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.service.UserSecurityService;
import com.carmenne.utils.UserUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.notNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsApplication.class)
public class UserSecurityServiceIntegrationTest {

    @Autowired
    private UserDetailsService userSecurityService;

    @Test
    public void testFindByUserName_positive() {

        UserDetails foundUser = userSecurityService.loadUserByUsername("basicUser");
        Assert.assertNotNull(foundUser);

    }

    @Test(expected = UsernameNotFoundException.class)
    public void testFindByUserName_negative() {

        User user = UserUtils.createBasicUser();
        UserDetails foundUser = userSecurityService.loadUserByUsername("username");
        Assert.assertNotNull(foundUser);

    }

}
