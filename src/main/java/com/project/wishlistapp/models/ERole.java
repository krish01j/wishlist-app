package com.project.wishlistapp.models;

/**
 * Roles include:
 * - ROLE_USER: Basic user role with standard access privileges.
 * - ROLE_MODERATOR: Intermediate role with additional permissions for content moderation.
 * - ROLE_ADMIN: Highest level role with full access and administrative permissions.
 */
public enum ERole {
  ROLE_USER,
  ROLE_MODERATOR,
  ROLE_ADMIN
}
