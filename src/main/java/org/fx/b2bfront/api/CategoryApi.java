package org.fx.b2bfront.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.fx.b2bfront.dto.CategoryDto;

import java.util.List;
import java.util.Map;

public class CategoryApi {

    private static final String BASE_URL = "http://localhost:8082/api/categories";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // =====================================================
    // 1) GET ALL CATEGORIES
    // =====================================================
    public static List<CategoryDto> getAll() {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();

            return gson.fromJson(json, new TypeToken<List<CategoryDto>>(){}.getType());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load categories: " + e.getMessage());
        }
    }

    // =====================================================
    // 2) GET FILTERS FOR CATEGORY
    // =====================================================
    public static Map<String, List<String>> getFilters(int categoryId) {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + "/" + categoryId + "/filters")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();

            return gson.fromJson(json, new TypeToken<Map<String, List<String>>>(){}.getType());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load filters: " + e.getMessage());
        }
    }
}
