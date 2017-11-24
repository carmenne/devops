package com.carmenne.tests.integration;

import com.carmenne.DevopsApplication;
import com.carmenne.backend.persistence.domain.backend.Plan;
import com.carmenne.backend.persistence.domain.backend.Role;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.domain.backend.UserRole;
import com.carmenne.backend.persistence.repositories.PlanRepository;
import com.carmenne.backend.persistence.repositories.RoleRepository;
import com.carmenne.backend.persistence.repositories.UserRepository;
import com.carmenne.enums.PlansEnum;
import com.carmenne.enums.RolesEnum;
import com.carmenne.utils.UserUtils;
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
    @Transactional
    public void test_createUser() throws Exception {

        User basicUser = createNewUser();

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
    public void testDeleteUser() throws Exception{
        User basicUser = createNewUser();
        userRepository.delete(basicUser.getId());
    }

    //---------------> Private methods

    private Plan createBasicPlan(PlansEnum plansEnum) {
        return new Plan(plansEnum);
    }


    private Role createRole(RolesEnum rolesEnum) {
        return new Role(rolesEnum);
    }

    private User createNewUser() {

        Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);

        User basicUser = UserUtils.createBasicUser();
        basicUser.setPlan(basicPlan);

        Role basicRole = createRole(RolesEnum.BASIC);
        roleRepository.save(basicRole);

        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(basicUser, basicRole);
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);
        userRepository.save(basicUser);

        return basicUser;
    }

}
