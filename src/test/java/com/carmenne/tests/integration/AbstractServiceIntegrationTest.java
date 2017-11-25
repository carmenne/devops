package com.carmenne.tests.integration;

import com.carmenne.backend.persistence.domain.backend.Role;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.domain.backend.UserRole;
import com.carmenne.backend.service.UserService;
import com.carmenne.enums.PlansEnum;
import com.carmenne.enums.RolesEnum;
import com.carmenne.utils.UserUtils;
import java.util.HashSet;
import java.util.Set;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractServiceIntegrationTest {

  @Autowired
  protected UserService userService;

  protected User createUser(TestName testName) {
    Set<UserRole> userRoles = new HashSet<>();

    User basicUser = UserUtils.createBasicUser(testName.getMethodName(),
        testName.getMethodName() + "@com");

    userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));

    return userService.createUser(basicUser,
        PlansEnum.BASIC, userRoles);
  }

}
