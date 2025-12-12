package org.fx.b2bfront.api;

import okhttp3.*;
import org.fx.b2bfront.store.AuthStore;
import org.fx.b2bfront.store.CartStore;
import org.fx.b2bfront.store.CheckoutStore;

public class OrderApi {

    private static final OkHttpClient client = new OkHttpClient();

    public static void placeOrder() throws Exception {

        String json = OrderBodyBuilder.buildJson();

        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json")
        );

        Request req = new Request.Builder()
                .url("http://localhost:8082/api/orders")
                .post(body)
                .build();

        client.newCall(req).execute().close();
    }
}
