package com.carmenne.backend.persistence.repositories;

import com.carmenne.backend.persistence.domain.backend.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Returns an user based by username
     * @param username
     * @return
     */
    public User findByUsername(String username);

    /**
     * Returns an user by email
     * @param email
     * @return
     */
    public User findByEmail(String email);
}
