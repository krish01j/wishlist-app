package com.project.wishlistapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.project.wishlistapp.models.User;
import com.project.wishlistapp.models.WishlistItem;
import com.project.wishlistapp.payload.request.WishListCreateRequest;
import com.project.wishlistapp.repository.UserRepository;
import com.project.wishlistapp.repository.WishlistRepository;
import com.project.wishlistapp.services.WishlistService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class WishlistServiceTests {

    @InjectMocks
    private WishlistService wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Before
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    public void testRetrieveWishlist() {
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);

        User user = new User();
        user.setUsername(username);

        WishlistItem item1 = new WishlistItem();
        item1.setName("Item 1");
        item1.setUser(user);

        WishlistItem item2 = new WishlistItem();
        item2.setName("Item 2");
        item2.setUser(user);

        List<WishlistItem> mockItems = Arrays.asList(item1, item2);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(wishlistRepository.findByUser(user)).thenReturn(mockItems);

        List<WishlistItem> returnedItems = wishlistService.findAllForCurrentUser();

        assertNotNull("The returned list should not be null", returnedItems);
        assertEquals("The returned list should have 2 items", 2, returnedItems.size());
    }

    @Test
    public void testDeleteWishlistItem() {
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);
        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        WishlistItem item = new WishlistItem();
        item.setId(1L);
        item.setUser(user);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(wishlistRepository.findById(1L)).thenReturn(Optional.of(item));
        wishlistService.deleteById(1L);
        verify(wishlistRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAddWishlistItem() {
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);

        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        WishListCreateRequest createRequest = new WishListCreateRequest();
        createRequest.setName("New Wishlist Item");

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setName(createRequest.getName());
        wishlistItem.setUser(user);
        when(wishlistRepository.save(any(WishlistItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WishlistItem returnedItem = wishlistService.save(createRequest);

        assertNotNull("The saved item should not be null", returnedItem);
        assertEquals("The name of the returned item should match the request", "New Wishlist Item", returnedItem.getName());
        assertNotNull("The user of the returned item should not be null", returnedItem.getUser());
        assertEquals("The user of the returned item should match the authenticated user", user, returnedItem.getUser());

        verify(wishlistRepository, times(1)).save(any(WishlistItem.class));
    }

}
