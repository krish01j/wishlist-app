package com.project.wishlistapp;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.project.wishlistapp.controllers.WishlistController;
import com.project.wishlistapp.models.WishlistItem;
import com.project.wishlistapp.payload.request.WishListCreateRequest;
import com.project.wishlistapp.services.WishlistService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WishlistControllerTests {

    @InjectMocks
    private WishlistController wishlistController;

    @Mock
    private WishlistService wishlistService;


    @Test
    public void testGetWishlist() {
        WishlistItem item1 = new WishlistItem();
        item1.setName("Test Item 1");
        WishlistItem item2 = new WishlistItem();
        item2.setName("Test Item 2");
        List<WishlistItem> mockItems = Arrays.asList(item1, item2);
        when(wishlistService.findAllForCurrentUser()).thenReturn(mockItems);

        ResponseEntity<List<WishlistItem>> response = wishlistController.getAllWishlistItems();

        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddWishlistItem(){
        WishListCreateRequest createRequest = new WishListCreateRequest();
        createRequest.setName("New Wishlist Item");

        WishlistItem addedItem = new WishlistItem();
        addedItem.setName(createRequest.getName());
        when(wishlistService.save(any(WishListCreateRequest.class))).thenReturn(addedItem);

        ResponseEntity<WishlistItem> response = wishlistController.addWishlistItem(createRequest);

        assertNotNull(response.getBody());
        assertEquals("New Wishlist Item", response.getBody().getName());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteWishlistItem() throws Exception {
        doNothing().when(wishlistService).deleteById(anyLong());
        ResponseEntity<?> response = wishlistController.deleteWishlistItem(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }



}


