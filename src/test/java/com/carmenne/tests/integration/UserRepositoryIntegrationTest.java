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
import org.junit.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsApplication.class)
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

  private static final String NEW_PASSWORD = "secret";
  @Rule
  public TestName testName = new TestName();

  @Before
  public void init() {
    Assert.assertNotNull(planRepository);
    Assert.assertNotNull(roleRepository);
    Assert.assertNotNull(userRepository);

  }

  @Test
  public void test_createNewPlan() throws Exception {
    Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
    planRepository.save(basicPlan);
    Plan retrievePlan = planRepository.findOne(PlansEnum.BASIC.getId());
    Assert.assertNotNull(retrievePlan);
  }


  @Test
  public void test_createBasicRole() throws Exception {
    Role basicRole = createRole(RolesEnum.BASIC);
    roleRepository.save(basicRole);
    Role retrieveRole = roleRepository.findOne(RolesEnum.BASIC.getId());
    Assert.assertNotNull(retrieveRole);
  }


  @Test
  public void test_createUser() throws Exception {

    User basicUser = createNewUser(testName);

    basicUser = userRepository.save(basicUser);
    User newlyCreatedUser = userRepository.findOne(basicUser.getId());

    Assert.assertNotNull(newlyCreatedUser);
    Assert.assertTrue(newlyCreatedUser.getId() != 0);
    Assert.assertNotNull(newlyCreatedUser.getPlan());
    Assert.assertNotNull(newlyCreatedUser.getPlan().getId());

    Set<UserRole> newlyCreatedUserRoles = newlyCreatedUser.getUserRoles();

    for (UserRole ur : newlyCreatedUserRoles) {
      Assert.assertNotNull(ur.getRole());
      Assert.assertNotNull(ur.getRole().getId());
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

    Assert.assertNotNull(userFound);
    Assert.assertNotNull(userFound.getId());

  }

  @Test
  public void testUpdateUserPassword() throws Exception {

    User user = createNewUser(testName);

    userRepository.updateUserPassword(user.getId(), NEW_PASSWORD);

    User foundUser = userRepository.findOne(user.getId());

    Assert.assertNotNull(foundUser);
    Assert.assertEquals(NEW_PASSWORD, foundUser.getPassword());

  }

  @Test
  public void testFindById() {
    User user = createNewUser(testName);
    User foundUser = userRepository.findOne(user.getId());

    Assert.assertNotNull(foundUser);
    Assert.assertEquals(user, foundUser);
  }

}
