package org.fx.b2bfront.store;

import org.fx.b2bfront.api.CartApi;
import org.fx.b2bfront.dto.CartDto;
import org.fx.b2bfront.dto.CartItem;

public class CartStore {

    private static CartDto cart = null;

    // ======================================================
    //  LOAD CART FROM BACKEND AFTER LOGIN
    // ======================================================
    public static void loadCartFromBackend(Long companyId) {
        cart = CartApi.getCart(companyId);

        if (cart == null) {
            cart = new CartDto();
            cart.setCompanyId(companyId);
        }

        System.out.println("Loaded cart from backend: " +
                (cart.getItems() != null ? cart.getItems().size() : 0) + " items");
    }

    // ======================================================
    //  ADD PRODUCT (sync backend)
    // ======================================================
    public static void add(Long productId, int qty) {
        cart = CartApi.add(cart.getCompanyId(), productId, qty);
    }

    // ======================================================
    //  UPDATE QTY (sync backend)
    // ======================================================
    public static void updateQuantity(Long productId, int qty) {
        cart = CartApi.updateQuantity(cart.getCompanyId(), productId, qty);
    }

    // ======================================================
    //  REMOVE ITEM
    // ======================================================
    public static void remove(Long productId) {
        cart = CartApi.remove(cart.getCompanyId(), productId);
    }

    // ======================================================
    //  GETTERS
    // ======================================================
    public static CartDto getCart() {
        return cart;
    }

    public static double getSubtotal() {
        return cart != null ? cart.getSubtotal() : 0;
    }

    public static java.util.List<CartItem> getItems() {
        return cart != null ? cart.getItems() : java.util.List.of();
    }


    public static void clear() {
        cart = null;
    }

}
