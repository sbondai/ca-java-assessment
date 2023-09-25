package com.ca.javaassessment.repositories;

import com.ca.javaassessment.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by its ID.
     *
     * @param id the ID of the user
     * @return an Optional of User
     */
    Optional<User> findById(Long id);
}