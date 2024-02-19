package com.project.wishlistapp.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.project.wishlistapp.models.ERole;
import com.project.wishlistapp.models.Role;
import com.project.wishlistapp.models.User;
import com.project.wishlistapp.payload.request.LoginRequest;
import com.project.wishlistapp.payload.request.SignupRequest;
import com.project.wishlistapp.payload.response.MessageResponse;
import com.project.wishlistapp.payload.response.UserInfoResponse;
import com.project.wishlistapp.repository.RoleRepository;
import com.project.wishlistapp.repository.UserRepository;
import com.project.wishlistapp.security.jwt.JwtUtils;
import com.project.wishlistapp.services.UserDetailsImpl;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AuthController class handles authentication and user registration requests.
 * It supports operations for user login and signup.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationManager authenticationManager;

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder encoder;

  private final JwtUtils jwtUtils;

  /**
   * Authenticates a user by their login credentials.
   *
   * @param loginRequest contains the username and password for authentication.
   * @return a ResponseEntity containing the user information response or error details.
   */
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    // Authenticate the user
    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    // Set the authentication in the security context
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Retrieve user details from the authentication object
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // Generate JWT token
    String token = jwtUtils.generateJwtToken(userDetails);

    // Retrieve user roles
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    // Respond with user info and JWT token
    return ResponseEntity.ok().body(new UserInfoResponse(userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles,token));
  }

  /**
   * Registers a new user with the given signup details.
   *
   * @param signUpRequest contains the user's signup information, including username, email, and password.
   * @return a ResponseEntity with a message response indicating the outcome of the registration attempt.
   */
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    // Check for existing username
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    // Check for existing email
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create a new user with encoded password
    User user = new User(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

    // Process roles
    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      // Assign default role if no role specified
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      // Assign specified roles
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            break;
          case "mod":
            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);
            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    // Set roles to the user and save
    user.setRoles(roles);
    userRepository.save(user);

    // Respond with success message
    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
