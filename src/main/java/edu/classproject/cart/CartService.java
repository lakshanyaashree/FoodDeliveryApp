package edu.classproject.cart;

/**
 * Public API contract for the Cart Management module.
 * All cross-module dependencies must reference this interface only.
 */
public interface CartService {

    /**
     * Creates a new empty cart for the given customer at the given restaurant.
     *
     * @param customerId   the ID of the customer
     * @param restaurantId the ID of the restaurant
     * @return a new Cart with a unique cartId and empty lines list
     * @throws IllegalArgumentException if customerId or restaurantId is null/blank
     */
    Cart createCart(String customerId, String restaurantId);

    /**
     * Adds an item to the cart, or merges quantity if item already exists.
     * Passing quantity 0 removes the item from the cart.
     *
     * @param cartId     the cart to update
     * @param menuItemId the menu item to add
     * @param quantity   the quantity to add (0 = remove)
     * @return the updated Cart
     * @throws IllegalArgumentException if quantity is negative
     * @throws IllegalStateException    if item belongs to a different restaurant
     * @throws java.util.NoSuchElementException if cartId is not found
     */
    Cart addItem(String cartId, String menuItemId, int quantity);

    /**
     * Retrieves the cart by its ID.
     *
     * @param cartId the cart ID
     * @return the Cart
     * @throws java.util.NoSuchElementException if not found
     */
    Cart getCart(String cartId);

    /**
     * Removes all items from the cart. The cart itself still exists.
     *
     * @param cartId the cart ID to clear
     * @throws java.util.NoSuchElementException if cartId is not found
     */
    void clearCart(String cartId);
}
