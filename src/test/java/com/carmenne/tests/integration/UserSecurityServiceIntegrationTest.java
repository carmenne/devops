package com.carmenne.tests.integration;

import com.carmenne.DevopsApplication;
import com.carmenne.backend.persistence.domain.backend.Role;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.domain.backend.UserRole;
import com.carmenne.backend.service.UserSecurityService;
import com.carmenne.backend.service.UserService;
import com.carmenne.enums.PlansEnum;
import com.carmenne.enums.RolesEnum;
import com.carmenne.utils.UserUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.notNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsApplication.class)
public class UserSecurityServiceIntegrationTest {

    @Autowired
    private UserDetailsService userSecurityService;

    @Autowired
    private UserService userService;

    @Rule
    public TestName testName = new TestName();

    @Test
    public void testFindByUserName_positive() {

        User user = UserUtils.createBasicUser(testName.getMethodName(),
                testName.getMethodName() + "@com");

        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, new Role(RolesEnum.BASIC)));

        userService.createUser(user,
                PlansEnum.BASIC, userRoles);

        UserDetails foundUser = userSecurityService.loadUserByUsername(user.getUsername());
        Assert.assertNotNull(foundUser);
        Assert.assertEquals(user.getUsername(), foundUser.getUsername());

    }

    @Test(expected = UsernameNotFoundException.class)
    public void testFindByUserName_negative() {

        UserDetails foundUser = userSecurityService.loadUserByUsername("username");
        Assert.assertNotNull(foundUser);

    }

}
