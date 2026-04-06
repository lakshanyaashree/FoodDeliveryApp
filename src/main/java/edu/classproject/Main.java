package edu.classproject;

import edu.classproject.cart.CartService;
import edu.classproject.cart.CartServiceImpl;
import edu.classproject.cart.Cart;

public class Main {
    public static void main(String[] args) {
        CartService cartService = new CartServiceImpl();

        Cart cart = cartService.createCart("customer1", "restaurant1");
        System.out.println("Created cart: " + cart.cartId());

        cart = cartService.addItem(cart.cartId(), "burger", 2);
        cart = cartService.addItem(cart.cartId(), "fries", 1);
        System.out.println("Cart lines: " + cart.lines());

        cartService.clearCart(cart.cartId());
        System.out.println("Cart cleared.");
    }
}