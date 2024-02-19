package com.project.wishlistapp.repository;

import com.project.wishlistapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for {@link User} entities.
 * Facilitates database operations on User entities, such as finding a user by username
 * and checking for the existence of usernames and emails.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Finds a user by their username.
   *
   * @param username The username of the user to find.
   * @return An {@link Optional} containing the user if found, or an empty Optional if not.
   */
  Optional<User> findByUsername(String username);

  /**
   * Checks if a username exists in the database.
   *
   * @param username The username to check for existence.
   * @return True if the username exists, false otherwise.
   */
  Boolean existsByUsername(String username);

  /**
   * Checks if an email exists in the database.
   *
   * @param email The email to check for existence.
   * @return True if the email exists, false otherwise.
   */
  Boolean existsByEmail(String email);
}
