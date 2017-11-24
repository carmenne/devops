package com.carmenne.backend.persistence.repositories;

import com.carmenne.backend.persistence.domain.backend.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Returns a user based by username
     * @param username
     * @return
     */
    public User findByUsername(String username);
}
