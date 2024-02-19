package com.project.wishlistapp.repository;


import com.project.wishlistapp.models.User;
import com.project.wishlistapp.models.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link WishlistItem} entities.
 * Supports operations to access and manipulate wishlist items,
 * such as finding all wishlist items for a given user.
 */
@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    /**
     * Finds all wishlist items associated with a specific user.
     * @param user The user whose wishlist items are to be retrieved.
     * @return A list of {@link WishlistItem} objects associated with the given user.
     */
    List<WishlistItem> findByUser(User user);
}

