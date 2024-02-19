package com.project.wishlistapp.repository;

import java.util.Optional;

import com.project.wishlistapp.models.ERole;
import com.project.wishlistapp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



/**
 * Repository interface for {@link Role} entities.
 * Provides methods to perform operations on the database for Role entities,
 * including finding a role by its name.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  /**
   * Finds a role by its name.
   *
   * @param name The name of the role to find.
   * @return An {@link Optional} describing the found role, or an empty Optional if no role is found.
   */
  Optional<Role> findByName(ERole name);
}
