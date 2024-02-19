package com.project.wishlistapp.services;

import com.project.wishlistapp.models.User;
import com.project.wishlistapp.models.WishlistItem;
import com.project.wishlistapp.payload.request.WishListCreateRequest;
import com.project.wishlistapp.repository.UserRepository;
import com.project.wishlistapp.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class for managing wishlist items.
 * Provides methods to interact with wishlist items for the current authenticated user,
 * such as finding all wishlist items, adding a new item, and deleting an item.
 */
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves all wishlist items for the current authenticated user.
     *
     * @return A list of {@link WishlistItem} belonging to the current user.
     */
    public List<WishlistItem> findAllForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return wishlistRepository.findByUser(user);
    }

    /**
     * Adds a new wishlist item for the current authenticated user.
     *
     * @param wishListCreateRequest Contains the details of the wishlist item to be created.
     * @return The saved {@link WishlistItem} entity.
     */
    public WishlistItem save(WishListCreateRequest wishListCreateRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setName(wishListCreateRequest.getName());
        wishlistItem.setUser(user);
        return wishlistRepository.save(wishlistItem);
    }

    /**
     * Deletes a wishlist item by its id if it belongs to the current authenticated user.
     *
     * @param id The id of the wishlist item to be deleted.
     * @throws ResponseStatusException if the item doesn't exist or doesn't belong to the current user.
     */
    public void deleteById(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<WishlistItem> wishlistItemOptional = wishlistRepository.findById(id);
        wishlistItemOptional.ifPresentOrElse(wishlistItem -> {
            if (!Objects.equals(wishlistItem.getUser().getId(), user.getId())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized user");
            }
            wishlistRepository.deleteById(id);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid wishlist to delete");
        });
    }
}
