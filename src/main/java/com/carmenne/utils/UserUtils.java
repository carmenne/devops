package com.carmenne.utils;

import com.carmenne.backend.persistence.domain.backend.User;

public class UserUtils {

    private UserUtils() {
        throw new AssertionError("Non instantiable");
    }

    public static User createBasicUser(String username, String email) {

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);

        user.setPassword("secret");
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
