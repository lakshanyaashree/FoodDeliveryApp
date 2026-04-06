package edu.classproject.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Abstract base class providing shared utility methods for cart service implementations.
 * Matches the «abstract» AbstractCartService shown in the UML diagram.
 */
abstract class AbstractCartService implements CartService {

    /**
     * Generates a unique cart ID using UUID.
     */
    protected String generateCartId() {
        return "CART-" + UUID.randomUUID().toString();
    }

    /**
     * Validates that the cart is not null and has valid fields.
     * Subclasses can extend this for additional checks.
     */
    protected void validateCart(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart must not be null");
        }
    }

    /**
     * Merges a new CartLine into an existing list of lines.
     * - If the item already exists, quantities are summed.
     * - If the resulting quantity is 0, the item is removed.
     * - If the item is new and quantity > 0, it is added.
     *
     * @param lines   existing cart lines
     * @param newLine the new line to merge in
     * @return updated list of cart lines
     */
    protected List<CartLine> mergeLines(List<CartLine> lines, CartLine newLine) {
        List<CartLine> updated = new ArrayList<>();
        boolean found = false;

        for (CartLine existing : lines) {
            if (existing.menuItemId().equals(newLine.menuItemId())) {
                found = true;
                    if (newLine.quantity() == 0) {
        // quantity 0 = explicit remove, skip adding
    } else {
        int merged = existing.quantity() + newLine.quantity();
        if (merged > 0) {
            updated.add(new CartLine(existing.menuItemId(), existing.itemName(), merged));
        }
    }
                // if merged == 0, item is dropped (remove behaviour)
            } else {
                updated.add(existing);
            }
        }

        // Item wasn't in cart yet — add it if quantity > 0
        if (!found && newLine.quantity() > 0) {
            updated.add(newLine);
        }

        return updated;
    }
}
