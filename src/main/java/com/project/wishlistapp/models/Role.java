package com.project.wishlistapp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Role entity represents a user role within the system.
 * Attributes:
 * - id: The unique identifier for each role.
 * - name: The name of the role, represented by the ERole enumeration.
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;

  /**
   * Constructor to create a role with a specific name.
   *
   * @param name The name of the role, using the ERole enumeration.
   */
  public Role(ERole name) {
    this.name = name;
  }
}
