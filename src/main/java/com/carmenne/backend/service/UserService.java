package com.carmenne.backend.service;

import com.carmenne.backend.persistence.domain.backend.Plan;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.domain.backend.UserRole;
import com.carmenne.backend.persistence.repositories.PlanRepository;
import com.carmenne.backend.persistence.repositories.RoleRepository;
import com.carmenne.backend.persistence.repositories.UserRepository;
import com.carmenne.enums.PlansEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PlanRepository planRepository;

    public User createUser(User user,
                           PlansEnum plansEnum,
                           Set<UserRole> userRoles) {

        Plan plan = new Plan(plansEnum);

        if (! planRepository.exists(plansEnum.getId())) {
            plan = planRepository.save(plan);
        }

        user.setPlan(plan);

        for (UserRole ur : userRoles) {
            roleRepository.save(ur.getRole());
        }

        user.getUserRoles().addAll(userRoles);

        return userRepository.save(user);

    }

}
