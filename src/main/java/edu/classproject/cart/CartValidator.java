package edu.classproject.cart;

/**
 * Handles input validation for cart operations.
 * Used internally by CartServiceImpl.
 */
class CartValidator {

    /**
     * Validates inputs for createCart.
     *
     * @throws IllegalArgumentException if customerId or restaurantId is null or blank
     */
    void validateCreateCart(String customerId, String restaurantId) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId must not be null or blank");
        }
        if (restaurantId == null || restaurantId.isBlank()) {
            throw new IllegalArgumentException("restaurantId must not be null or blank");
        }
    }

    /**
     * Validates inputs for addItem.
     *
     * @throws IllegalArgumentException if cartId or menuItemId is null/blank, or if quantity is negative
     */
    void validateAddItem(String cartId, String menuItemId, int quantity) {
        if (cartId == null || cartId.isBlank()) {
            throw new IllegalArgumentException("cartId must not be null or blank");
        }
        if (menuItemId == null || menuItemId.isBlank()) {
            throw new IllegalArgumentException("menuItemId must not be null or blank");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must not be negative (use 0 to remove an item)");
        }
    }

    /**
     * Validates that the item being added belongs to the same restaurant as the cart.
     *
     * @throws IllegalStateException if restaurantIds don't match
     */
    void validateSameRestaurant(String cartRestaurantId, String itemRestaurantId) {
        if (!cartRestaurantId.equals(itemRestaurantId)) {
            throw new IllegalStateException(
                "Cannot add item from restaurant '" + itemRestaurantId +
                "' to a cart for restaurant '" + cartRestaurantId + "'"
            );
        }
    }
}
