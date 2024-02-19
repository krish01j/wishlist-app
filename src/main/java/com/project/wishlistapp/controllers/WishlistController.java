package com.project.wishlistapp.controllers;


import com.project.wishlistapp.models.WishlistItem;
import com.project.wishlistapp.payload.request.WishListCreateRequest;
import com.project.wishlistapp.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // Retrieve all wishlist items
    @GetMapping
    public ResponseEntity<List<WishlistItem>> getAllWishlistItems() {
        List<WishlistItem> items = wishlistService.findAllForCurrentUser();
        return ResponseEntity.ok().body(items);
    }

    // Add a new wishlist item
    @PostMapping
    public ResponseEntity<WishlistItem> addWishlistItem(@RequestBody WishListCreateRequest wishListCreateRequest) {
        WishlistItem newItem = wishlistService.save(wishListCreateRequest);
        return ResponseEntity.ok().body(newItem);
    }

    // Delete a wishlist item by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWishlistItem(@PathVariable Long id) {
        wishlistService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

