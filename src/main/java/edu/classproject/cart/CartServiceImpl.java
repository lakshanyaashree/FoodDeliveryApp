package edu.classproject.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Default implementation of CartService.
 *
 * Responsibilities:
 *  - Delegates persistence to CartRepository (InMemoryCartRepository by default)
 *  - Uses CartValidator for input validation
 *  - Uses AbstractCartService helpers: generateCartId(), validateCart(), mergeLines()
 *
 * Matches the «class» CartServiceImpl in the UML diagram.
 */
public class CartServiceImpl extends AbstractCartService {

    private final CartRepository repository;
    private final CartValidator validator;

    /**
     * Default constructor — uses in-memory storage.
     */
    public CartServiceImpl() {
        this(new InMemoryCartRepository(), new CartValidator());
    }

    /**
     * Constructor for dependency injection (used in tests).
     */
    CartServiceImpl(CartRepository repository, CartValidator validator) {
        this.repository = repository;
        this.validator  = validator;
    }

    // ── FR1: Create Cart ──────────────────────────────────────────────────────

    /**
     * Creates a new empty cart for the customer at the given restaurant.
     *
     * TC1  — happy path: returns cart with unique cartId and empty lines
     * TC2  — customerId null → IllegalArgumentException
     * TC3  — restaurantId null → IllegalArgumentException
     * TC4  — same customer + restaurant → new cart returned (no duplicate prevention at service level;
     *         idempotency can be added at a higher layer if needed)
     */
    @Override
    public Cart createCart(String customerId, String restaurantId) {
        validator.validateCreateCart(customerId, restaurantId);

        Cart cart = new Cart(generateCartId(), customerId, restaurantId, new ArrayList<>());
        return repository.save(cart);
    }

    // ── FR2 / FR3 / FR4: Add / Update / Remove Item ──────────────────────────

    /**
     * Adds an item to the cart.
     *
     * FR2 — TC5:  Add new item           → item appears with correct quantity
     * FR2 — TC6:  Add multiple items     → cart contains all items
     * FR3 — TC7:  Add same item again    → quantities are merged (e.g. 2+3 = 5)
     * FR4 — TC8:  Add with quantity 0    → item is removed from cart
     * NFR1 — TC12: Negative quantity     → IllegalArgumentException
     * NFR2 — TC13: Item from diff restaurant → IllegalStateException
     *
     * NOTE: The CartService interface does not carry restaurant info per item.
     * The single-restaurant rule is enforced by checking the cart's own restaurantId
     * against a simulated "item restaurantId" embedded in the menuItemId string.
     * Format expected: "REST1:ITEM1" — prefix before ':' is the restaurant.
     * If no prefix is present, the item is assumed to belong to the cart's restaurant.
     */
    @Override
    public Cart addItem(String cartId, String menuItemId, int quantity) {
        validator.validateAddItem(cartId, menuItemId, quantity);

        Cart cart = repository.findById(cartId)
            .orElseThrow(() -> new NoSuchElementException("Cart not found: " + cartId));

        // Single-restaurant constraint (NFR2 / TC13)
        if (menuItemId.contains(":")) {
            String itemRestaurantId = menuItemId.split(":")[0];
            validator.validateSameRestaurant(cart.restaurantId(), itemRestaurantId);
        }

        // Derive a clean itemName from the menuItemId
        String itemName = menuItemId.contains(":")
            ? menuItemId.split(":")[1]
            : menuItemId;

        CartLine newLine = new CartLine(menuItemId, itemName, quantity);
        List<CartLine> updatedLines = mergeLines(cart.lines(), newLine);

        Cart updated = new Cart(cart.cartId(), cart.customerId(), cart.restaurantId(), updatedLines);
        return repository.save(updated);
    }

    // ── FR5: Retrieve Cart ────────────────────────────────────────────────────

    /**
     * Retrieves the cart by its ID.
     *
     * TC9 — happy path: correct cart returned with all items
     */
    @Override
    public Cart getCart(String cartId) {
        return repository.findById(cartId)
            .orElseThrow(() -> new NoSuchElementException("Cart not found: " + cartId));
    }

    // ── FR6: Clear Cart ───────────────────────────────────────────────────────

    /**
     * Clears all items from the cart. Cart entity is NOT deleted.
     *
     * TC11 — happy path: lines list becomes empty after clear
     */
    @Override
    public void clearCart(String cartId) {
        Cart cart = repository.findById(cartId)
            .orElseThrow(() -> new NoSuchElementException("Cart not found: " + cartId));

        Cart cleared = new Cart(cart.cartId(), cart.customerId(), cart.restaurantId(), new ArrayList<>());
        repository.save(cleared);
    }
}
