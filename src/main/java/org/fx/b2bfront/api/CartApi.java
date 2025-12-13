package org.fx.b2bfront.api;

import com.google.gson.Gson;
import okhttp3.*;
import org.fx.b2bfront.dto.CartDto;

public class CartApi {

    private static final String BASE_URL = "http://localhost:8082/api/cart";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();


    // GET /api/cart/{companyId}
    public static CartDto getCart(Long companyId) {
        try {
            Request req = new Request.Builder()
                    .url(BASE_URL + "/" + companyId)
                    .get()
                    .build();

            Response res = client.newCall(req).execute();
            String json = res.body().string();

            return gson.fromJson(json, CartDto.class);

        } catch (Exception e) {
            System.out.println("❌ CartApi.getCart failed: " + e.getMessage());
            return null;
        }
    }


    // POST /api/cart/add?companyId=..&productId=..&qty=..
    public static CartDto add(Long companyId, Long productId, int qty) {
        try {
            HttpUrl url = HttpUrl.parse(BASE_URL + "/add")
                    .newBuilder()
                    .addQueryParameter("companyId", companyId.toString())
                    .addQueryParameter("productId", productId.toString())
                    .addQueryParameter("qty", String.valueOf(qty))
                    .build();

            Request req = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create("", null))
                    .build();

            Response res = client.newCall(req).execute();
            return new Gson().fromJson(res.body().string(), CartDto.class);

        } catch (Exception e) {
            System.out.println("❌ CartApi.add failed: " + e.getMessage());
            return null;
        }
    }


    // PUT /api/cart/update
    public static CartDto updateQuantity(Long companyId, Long productId, int qty) {
        try {
            HttpUrl url = HttpUrl.parse(BASE_URL + "/update")
                    .newBuilder()
                    .addQueryParameter("companyId", companyId.toString())
                    .addQueryParameter("productId", productId.toString())
                    .addQueryParameter("qty", String.valueOf(qty))
                    .build();

            Request req = new Request.Builder()
                    .url(url)
                    .put(RequestBody.create("", null))
                    .build();

            Response res = client.newCall(req).execute();
            return gson.fromJson(res.body().string(), CartDto.class);

        } catch (Exception e) {
            System.out.println("❌ CartApi.updateQuantity failed: " + e.getMessage());
            return null;
        }
    }


    // DELETE /api/cart/remove
    public static CartDto remove(Long companyId, Long productId) {
        try {
            HttpUrl url = HttpUrl.parse(BASE_URL + "/remove")
                    .newBuilder()
                    .addQueryParameter("companyId", companyId.toString())
                    .addQueryParameter("productId", productId.toString())
                    .build();

            Request req = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();

            Response res = client.newCall(req).execute();
            return gson.fromJson(res.body().string(), CartDto.class);

        } catch (Exception e) {
            System.out.println("❌ CartApi.remove failed: " + e.getMessage());
            return null;
        }
    }
}
