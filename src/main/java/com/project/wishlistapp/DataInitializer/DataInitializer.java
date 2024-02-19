package com.project.wishlistapp.DataInitializer;

import com.project.wishlistapp.models.ERole;
import com.project.wishlistapp.models.Role;
import com.project.wishlistapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * DataInitializer class is used to pre-load the Role entities into the database at application startup.
 * This ensures that essential roles are available in the system for assigning to users.
 * It implements CommandLineRunner to execute code after the Spring Boot application has started.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    /**
     * Overrides the run method from CommandLineRunner interface.
     * This method is called at application startup and initializes roles in the database.
     *
     * @param args the command line arguments passed to the application.
     */
    @Override
    public void run(String... args) {
        createRoleIfNotFound(ERole.ROLE_USER);
        createRoleIfNotFound(ERole.ROLE_MODERATOR);
        createRoleIfNotFound(ERole.ROLE_ADMIN);
    }

    /**
     * Checks if a role exists in the database and creates it if not found.
     * This method ensures that essential roles are always available for assignment.
     *
     * @param name the name of the role to check and create if absent.
     */
    private void createRoleIfNotFound(ERole name) {
        Optional<Role> role = roleRepository.findByName(name);
        if (!role.isPresent()) {
            Role newRole = new Role(name);
            roleRepository.save(newRole);
        }
    }
}
