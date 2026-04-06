package edu.classproject.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    private CartService service;

    @BeforeEach
    void setUp() {
        service = new CartServiceImpl();
    }

    // FR1 - Create Cart
    @Test void TC1_createCart_success() {
        Cart cart = service.createCart("C1", "R1");
        assertNotNull(cart.cartId());
        assertTrue(cart.lines().isEmpty());
    }

    @Test void TC2_createCart_nullCustomer_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.createCart(null, "R1"));
    }

    @Test void TC3_createCart_nullRestaurant_throws() {
        assertThrows(IllegalArgumentException.class, () -> service.createCart("C1", null));
    }

    @Test void TC4_createCart_duplicate_returnsNewCart() {
        Cart c1 = service.createCart("C1", "R1");
        Cart c2 = service.createCart("C1", "R1");
        assertNotNull(c2.cartId());
    }

    // FR2 - Add Item
    @Test void TC5_addItem_newItem_appearsInCart() {
        Cart cart = service.createCart("C1", "R1");
        cart = service.addItem(cart.cartId(), "burger", 2);
        assertEquals(1, cart.lines().size());
        assertEquals(2, cart.lines().get(0).quantity());
    }

    @Test void TC6_addItem_multipleItems_allPresent() {
        Cart cart = service.createCart("C1", "R1");
        cart = service.addItem(cart.cartId(), "burger", 2);
        cart = service.addItem(cart.cartId(), "fries", 1);
        assertEquals(2, cart.lines().size());
    }

    // FR3 - Update Quantity
    @Test void TC7_addItem_sameItem_mergesQuantity() {
        Cart cart = service.createCart("C1", "R1");
        cart = service.addItem(cart.cartId(), "burger", 2);
        cart = service.addItem(cart.cartId(), "burger", 3);
        assertEquals(5, cart.lines().get(0).quantity());
    }

    // FR4 - Remove Item (quantity 0 on existing item)
    @Test void TC8_addItem_quantityZero_removesItem() {
        Cart cart = service.createCart("C1", "R1");
        cart = service.addItem(cart.cartId(), "burger", 2);
        cart = service.addItem(cart.cartId(), "burger", 0); // merge to 0 = removed
        assertTrue(cart.lines().isEmpty(), "Cart should be empty after removing the only item");
    }

    // FR5 - Get Cart
    @Test void TC9_getCart_returnsCorrectCart() {
        Cart cart = service.createCart("C1", "R1");
        service.addItem(cart.cartId(), "burger", 1);
        Cart fetched = service.getCart(cart.cartId());
        assertEquals(cart.cartId(), fetched.cartId());
    }

    @Test void TC10_getCart_notFound_throws() {
        assertThrows(Exception.class, () -> service.getCart("INVALID"));
    }

    // FR6 - Clear Cart
    @Test void TC11_clearCart_emptiesLines() {
        Cart cart = service.createCart("C1", "R1");
        service.addItem(cart.cartId(), "burger", 2);
        service.clearCart(cart.cartId());
        assertTrue(service.getCart(cart.cartId()).lines().isEmpty());
    }

    // NFR1 - Input Validation
    @Test void TC12_addItem_negativeQuantity_throws() {
        Cart cart = service.createCart("C1", "R1");
        assertThrows(IllegalArgumentException.class, () -> service.addItem(cart.cartId(), "burger", -1));
    }

    // NFR2 - Data Consistency (single restaurant)
    @Test void TC13_addItem_differentRestaurant_throws() {
        Cart cart = service.createCart("C1", "R1");
        assertThrows(IllegalStateException.class, () -> service.addItem(cart.cartId(), "R2:burger", 1));
    }

    @Test void TC14_clearCart_notFound_throws() {
        assertThrows(Exception.class, () -> service.clearCart("INVALID"));
    }
}