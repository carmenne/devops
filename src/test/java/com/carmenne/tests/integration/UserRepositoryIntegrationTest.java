package com.carmenne.tests.integration;

import com.carmenne.DevopsApplication;
import com.carmenne.backend.persistence.domain.backend.Plan;
import com.carmenne.backend.persistence.domain.backend.Role;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.domain.backend.UserRole;
import com.carmenne.enums.PlansEnum;
import com.carmenne.enums.RolesEnum;
import java.util.Set;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsApplication.class)
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

  @Rule
  public TestName testName = new TestName();

  @Before
  public void init() {
    Assert.notNull(planRepository);
    Assert.notNull(roleRepository);
    Assert.notNull(userRepository);

  }

  @Test
  public void test_createNewPlan() throws Exception {
    Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
    planRepository.save(basicPlan);
    Plan retrievePlan = planRepository.findOne(PlansEnum.BASIC.getId());
    Assert.notNull(retrievePlan);
  }


  @Test
  public void test_createBasicRole() throws Exception {
    Role basicRole = createRole(RolesEnum.BASIC);
    roleRepository.save(basicRole);
    Role retrieveRole = roleRepository.findOne(RolesEnum.BASIC.getId());
    Assert.notNull(retrieveRole);
  }


  @Test
  public void test_createUser() throws Exception {

    User basicUser = createNewUser(testName);

    basicUser = userRepository.save(basicUser);
    User newlyCreatedUser = userRepository.findOne(basicUser.getId());

    Assert.notNull(newlyCreatedUser);
    Assert.isTrue(newlyCreatedUser.getId() != 0);
    Assert.notNull(newlyCreatedUser.getPlan());
    Assert.notNull(newlyCreatedUser.getPlan().getId());

    Set<UserRole> newlyCreatedUserRoles = newlyCreatedUser.getUserRoles();

    for (UserRole ur : newlyCreatedUserRoles) {
      Assert.notNull(ur.getRole());
      Assert.notNull(ur.getRole().getId());
    }
  }

  @Test
  public void testDeleteUser() throws Exception {
    User basicUser = createNewUser(testName);
    userRepository.delete(basicUser.getId());
  }

  @Test
  public void testGetUserByEmail() throws Exception {

    User user = createNewUser(testName);
    User userFound = userRepository.findByEmail(user.getEmail());

    Assert.notNull(userFound);
    Assert.notNull(userFound.getId());

  }

}
