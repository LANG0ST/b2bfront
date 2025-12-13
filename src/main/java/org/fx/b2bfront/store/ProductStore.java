package org.fx.b2bfront.store;

public class ProductStore {

    public static Long selectedProductId;

    public static void setSelectedProductId(Long id) {
        selectedProductId = id;
    }

    public static Long getSelectedProductId() {
        return selectedProductId;
    }

    public static void clear() {
        selectedProductId = null;
    }
}
