package com.carmenne.tests.integration;

import com.carmenne.DevopsApplication;
import com.carmenne.backend.persistence.domain.backend.Role;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.domain.backend.UserRole;
import com.carmenne.backend.service.UserService;
import com.carmenne.enums.PlansEnum;
import com.carmenne.enums.RolesEnum;
import com.carmenne.utils.UserUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.Assert.notNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsApplication.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    public void testCreateNewUser() throws Exception {

        Set<UserRole> userRoles = new HashSet<>();

        User basicUser = UserUtils.createBasicUser();

        userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));

        User user = userService.createUser(basicUser,
                PlansEnum.BASIC, userRoles);

        notNull(user);
        notNull(user.getId());


    }
}
