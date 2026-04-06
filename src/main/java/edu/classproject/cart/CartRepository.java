package edu.classproject.cart;

import java.util.Optional;

/**
 * Internal persistence contract for the cart module.
 * Not visible outside this package.
 */
interface CartRepository {

    Cart save(Cart cart);

    Optional<Cart> findById(String cartId);

    void delete(String cartId);
}
