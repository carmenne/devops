package com.carmenne.backend.service;

import com.carmenne.backend.persistence.domain.backend.Plan;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.domain.backend.UserRole;
import com.carmenne.backend.persistence.repositories.PlanRepository;
import com.carmenne.backend.persistence.repositories.RoleRepository;
import com.carmenne.backend.persistence.repositories.UserRepository;
import com.carmenne.enums.PlansEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);


    @Transactional
    public User createUser(User user,
                           PlansEnum plansEnum,
                           Set<UserRole> userRoles) {

        String encPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encPassword);

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

    @Transactional
    public void updateUserPassword(long userId, String password) {

        password = passwordEncoder.encode(password);
        userRepository.updateUserPassword(userId, password);

        LOG.debug("Password was updated successfully for user id {}",
            userId);
    }

    public User findUserById(long userId) {
        return userRepository.findOne(userId);
    }

}
