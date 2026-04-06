package edu.classproject.cart;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory implementation of CartRepository using a HashMap.
 * No database or file I/O — data lives only while the JVM is running.
 */
class InMemoryCartRepository implements CartRepository {

    private final Map<String, Cart> store = new HashMap<>();

    @Override
    public Cart save(Cart cart) {
        store.put(cart.cartId(), cart);
        return cart;
    }

    @Override
    public Optional<Cart> findById(String cartId) {
        return Optional.ofNullable(store.get(cartId));
    }

    @Override
    public void delete(String cartId) {
        store.remove(cartId);
    }
}
