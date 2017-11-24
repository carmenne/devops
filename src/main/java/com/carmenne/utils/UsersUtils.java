package com.carmenne.utils;

import com.carmenne.backend.persistence.domain.backend.User;

public class UsersUtils {

    private UsersUtils() {
        throw new AssertionError("Non instantiable");
    }

    public static User createUser() {
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
