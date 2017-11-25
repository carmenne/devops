package com.carmenne.tests.integration;

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
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractIntegrationTest {

    @Autowired
    protected PlanRepository planRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected UserRepository userRepository;


    protected Plan createBasicPlan(PlansEnum plansEnum) {
        return new Plan(plansEnum);
    }

    protected Role createRole(RolesEnum rolesEnum) {
        return new Role(rolesEnum);
    }

    protected User createNewUser(String username, String email) {

        Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);

        User basicUser = UserUtils.createBasicUser(username, email);
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

    protected User createNewUser(TestName testName) {
        return createNewUser(testName.getMethodName(),
                testName.getMethodName() + "@com");

    }

}
