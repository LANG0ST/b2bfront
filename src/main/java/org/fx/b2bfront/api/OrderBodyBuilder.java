package org.fx.b2bfront.api;

import com.google.gson.Gson;
import org.fx.b2bfront.store.CartStore;
import org.fx.b2bfront.store.CheckoutStore;
import org.fx.b2bfront.store.AuthStore;

import java.util.List;

public class OrderBodyBuilder {

    private record Item(long productId, int quantity){}
    private record OrderBody(
            long companyId,
            List<Item> items,
            String address,
            String city,
            String zipcode,
            int shippingCost
    ) {}

    public static String buildJson() {

        var list = CartStore.getItems().stream()
                .map(i -> new Item(i.getProductId(), i.getQuantity()))
                .toList();

        OrderBody body = new OrderBody(
                AuthStore.companyId,
                list,
                CheckoutStore.address,
                CheckoutStore.city,
                CheckoutStore.zipcode,
                (int)CheckoutStore.shippingCost
        );

        return new Gson().toJson(body);
    }
}
