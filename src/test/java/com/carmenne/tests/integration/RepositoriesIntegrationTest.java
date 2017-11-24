package com.carmenne.tests.integration;

import com.carmenne.DevopsApplication;
import com.carmenne.backend.persistence.domain.backend.Plan;
import com.carmenne.backend.persistence.domain.backend.Role;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.domain.backend.UserRole;
import com.carmenne.backend.persistence.repositories.PlanRepository;
import com.carmenne.backend.persistence.repositories.RoleRepository;
import com.carmenne.backend.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsApplication.class)
public class RepositoriesIntegrationTest {

    private static final int BASIC_PLAN_ID = 1;
    private static final int BASIC_ROLE_ID = 1;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        Assert.notNull(planRepository);
        Assert.notNull(roleRepository);
        Assert.notNull(userRepository);

    }

    @Test
    public void test_createNewPlan() throws Exception {
        Plan basicPlan = createBasicPlan();
        planRepository.save(basicPlan);
        Plan retrievePlan = planRepository.findOne(BASIC_PLAN_ID);
        Assert.notNull(retrievePlan);
    }


    @Test
    public void test_createBasicRole() throws Exception {
        Role basicRole = createBasicRole();
        roleRepository.save(basicRole);
        Role retrieveRole = roleRepository.findOne(BASIC_ROLE_ID);
        Assert.notNull(retrieveRole);
    }


    @Test
    @Transactional
    public void test_createUser() throws Exception {

        Plan basicPlan = createBasicPlan();
        planRepository.save(basicPlan);

        User basicUser = createUser();
        basicUser.setPlan(basicPlan);

        Role basicRole = createBasicRole();

        Set<UserRole> userRoles = new HashSet<>();

        UserRole userRole = new UserRole();
        userRole.setRole(basicRole);
        userRole.setUser(basicUser);

        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);

        for (UserRole ur : userRoles) {
            roleRepository.save(ur.getRole());
        }

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

    //---------------> Private methods

    private Plan createBasicPlan() {
        Plan plan = new Plan();
        plan.setId(BASIC_PLAN_ID);
        plan.setName("basic");
        return plan;
    }


    private Role createBasicRole() {
        Role role = new Role();
        role.setId(BASIC_ROLE_ID);
        role.setName("USER_ROLE");
        return role;
    }

    private User createUser() {
        User user = new User();

        user.setUsername("basicUser");
        user.setPassword("secret");
        user.setEmail("email@company.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("12345678");
        user.setCountry("GB");
        user.setEnabled(true);
        user.setDescription("A basic user");
        user.setProfile_image_url("https://blabla.com");

        return user;
    }
}
