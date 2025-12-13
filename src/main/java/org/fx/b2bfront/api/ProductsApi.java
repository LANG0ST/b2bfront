package org.fx.b2bfront.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.fx.b2bfront.dto.FilterRequest;
import org.fx.b2bfront.dto.ProductDto;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fx.b2bfront.utils.GsonProvider.gson;

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


    public static void delete(long id) throws Exception {

        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .delete()
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            String msg = response.body() != null ? response.body().string() : "";
            throw new Exception("Impossible de supprimer : " + msg);
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

    public static void createProduct(ProductDto dto) {
        try {
            String jsonBody = gson.toJson(dto);

            RequestBody body = RequestBody.create(
                    jsonBody,
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to create product: " + response.code());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating product: " + e.getMessage());
        }
    }

    public static void update(ProductDto p) throws Exception {
        String json = gson.toJson(p);

        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json")
        );

        Request req = new Request.Builder()
                .url(BASE_URL + "/" + p.getId())
                .put(body)
                .build();

        client.newCall(req).execute().close();
    }
    public static String uploadImage(File file) throws Exception {

        String url = "http://localhost:8082/api/images/upload";

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file",
                        file.getName(),
                        RequestBody.create(file, MediaType.parse("image/*"))
                )
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("Upload failed: HTTP " + response.code());
            }

            return response.body().string(); // THIS IS THE URL
        }
    }




}
