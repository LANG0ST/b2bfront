package org.fx.b2bfront.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.fx.b2bfront.dto.FilterRequest;
import org.fx.b2bfront.dto.ProductDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsApi {

    private static final String BASE_URL = "http://localhost:8082/api/products";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // =======================================================
    // 1) TRENDING PRODUCTS
    // =======================================================
    public static List<ProductDto> getTrending() {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/trending")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();

            return gson.fromJson(json, new TypeToken<List<ProductDto>>(){}.getType());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load trending products: " + e.getMessage());
        }
    }

    // =======================================================
    // 2) PRODUCTS BY CATEGORY
    // =======================================================
    public static List<ProductDto> getByCategory(int categoryId) {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/categories/" + categoryId)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();

            return gson.fromJson(json, new TypeToken<List<ProductDto>>(){}.getType());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load products: " + e.getMessage());
        }
    }

    // =======================================================
    // 3) FILTER PRODUCTS (BY TAG)
    // =======================================================
    public static List<ProductDto> filter(int categoryId, String group, String value) {

        try {
            // Build request body
            Map<String, String> map = new HashMap<>();
            map.put(group, value);

            FilterRequest reqObj = new FilterRequest(categoryId, map);
            String jsonBody = gson.toJson(reqObj);

            RequestBody body = RequestBody.create(
                    jsonBody,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(BASE_URL + "/filter")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();

            return gson.fromJson(json, new TypeToken<List<ProductDto>>(){}.getType());

        } catch (Exception e) {
            throw new RuntimeException("Failed to filter products: " + e.getMessage());
        }
    }

    // =======================================================
    // 4) GET PRODUCT BY ID
    // =======================================================
    public static ProductDto getById(long id) {

        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("Backend error: " + response.code());
            }

            if (response.body() == null) {
                throw new RuntimeException("Empty response body");
            }

            String json = response.body().string();
            return gson.fromJson(json, ProductDto.class);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load product: " + e.getMessage(), e);
        }
    }

    // =======================================================


    public static void delete(long id) {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/" + id)
                    .delete()
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to delete product: " + response.code());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete product: " + e.getMessage());
        }
    }



    // =======================================================

    public static List<ProductDto> findAll() {

        try {
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();

            return gson.fromJson(json, new TypeToken<List<ProductDto>>(){}.getType());

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all products: " + e.getMessage());
        }
    }


    public static void addToCart(long companyId, long productId, int qty) {
        try {
            String url = "http://localhost:8082/api/cart/add"
                    + "?companyId=" + companyId
                    + "&productId=" + productId
                    + "&qty=" + qty;

            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(new byte[0]))   // empty POST body
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to add to cart. Code: " + response.code());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error adding to cart: " + e.getMessage());
        }
    }

}
