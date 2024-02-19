package com.project.wishlistapp;

import com.project.wishlistapp.models.WishlistItem;
import com.project.wishlistapp.repository.WishlistRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WishlistRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Test
    public void testSaveWishlistItem() {
        WishlistItem newItem = new WishlistItem();
        newItem.setName("Sample Item");

        WishlistItem savedItem = wishlistRepository.save(newItem);

        WishlistItem foundItem = entityManager.find(WishlistItem.class, savedItem.getId());

        assertNotNull(foundItem);
        assertEquals("Sample Item", foundItem.getName());
    }

    @Test
    public void testFindById() {
        WishlistItem item = new WishlistItem();
        item.setName("Find Test Item");
        entityManager.persist(item);
        entityManager.flush();
        Optional<WishlistItem> foundItem = wishlistRepository.findById(item.getId());
        assertTrue(foundItem.isPresent());
        assertEquals(item.getName(), foundItem.get().getName());
    }

    @Test
    public void testUpdateWishlistItem() {
        WishlistItem item = new WishlistItem();
        item.setName("Original Name");
        entityManager.persist(item);
        entityManager.flush();
        item.setName("Updated Name");
        wishlistRepository.save(item);
        WishlistItem updatedItem = entityManager.find(WishlistItem.class, item.getId());
        assertEquals("Updated Name", updatedItem.getName());
    }

    @Test
    public void testDeleteWishlistItem() {
        WishlistItem item = new WishlistItem();
        item.setName("Item to Delete");
        entityManager.persist(item);
        entityManager.flush();
        wishlistRepository.delete(item);
        WishlistItem deletedItem = entityManager.find(WishlistItem.class, item.getId());
        assertNull(deletedItem);
    }




}
