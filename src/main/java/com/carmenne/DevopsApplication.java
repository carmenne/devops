package com.carmenne;

import com.carmenne.backend.persistence.domain.backend.Role;
import com.carmenne.backend.persistence.domain.backend.User;
import com.carmenne.backend.persistence.domain.backend.UserRole;
import com.carmenne.backend.service.UserService;
import com.carmenne.enums.PlansEnum;
import com.carmenne.enums.RolesEnum;
import com.carmenne.utils.UsersUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class DevopsApplication implements CommandLineRunner {

    /**
     * The application logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(DevopsApplication.class);


    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(DevopsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception{
        User user = UsersUtils.createUser();

        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, new Role(RolesEnum.BASIC)));

        LOG.debug("Creating user with username {}", user.getUsername());

        userService.createUser(user, PlansEnum.BASIC, userRoles);
        LOG.info("User created {}", user.getUsername());
    }
}
