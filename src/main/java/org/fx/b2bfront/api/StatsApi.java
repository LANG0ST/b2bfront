package org.fx.b2bfront.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.fx.b2bfront.model.CategoryStats;
import org.fx.b2bfront.model.CompanyStats;
import org.fx.b2bfront.model.ProductStats;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class StatsApi {

    private static final String BASE_URL = "http://localhost:8082/api/stats";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();


    // ============================
    //     UTILITAIRE GET JSON
    // ============================
    private static ApiResult get(String path) {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL + path)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            String body = response.body().string();

            return new ApiResult(response.isSuccessful(), body);

        } catch (IOException e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public record ApiResult(boolean success, String body) {}



    // ============================
    //        SIMPLE INTEGERS
    // ============================

    public static Integer getCompaniesActives() {
        ApiResult result = get("/companies/actives/nbr");

        if (!result.success()) return null;

        try {
            return gson.fromJson(result.body(), Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getProductsCount() {
        ApiResult result = get("/products/nbr");

        if (!result.success()) return null;

        try {
            return gson.fromJson(result.body(), Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getOrdersCount() {
        ApiResult result = get("/commandes/nbr");

        if (!result.success()) return null;

        try {
            return gson.fromJson(result.body(), Integer.class);
        } catch (Exception e) {
            return null;
        }
    }



    // ============================
    //       COMPANY STATS
    // ============================

    private static final Type companyListType = new TypeToken<List<CompanyStats>>(){}.getType();

    public static List<CompanyStats> bestCompaniesAcheteuses() {
        ApiResult result = get("/companies/acheteuses");
        if (!result.success()) return null;
        return gson.fromJson(result.body(), companyListType);
    }

    public static List<CompanyStats> top3CompaniesAcheteuses() {
        ApiResult result = get("/companies/acheteuses/top3");
        if (!result.success()) return null;
        return gson.fromJson(result.body(), companyListType);
    }

    public static List<CompanyStats> bestCompaniesVendeuses() {
        ApiResult result = get("/companies/vendeuses");
        if (!result.success()) return null;
        return gson.fromJson(result.body(), companyListType);
    }

    public static List<CompanyStats> top3CompaniesVendeuses() {
        ApiResult result = get("/companies/vendeuses/top3");
        if (!result.success()) return null;
        return gson.fromJson(result.body(), companyListType);
    }



    // ============================
    //        PRODUCT STATS
    // ============================

    private static final Type productListType = new TypeToken<List<ProductStats>>(){}.getType();

    public static List<ProductStats> bestProducts() {
        ApiResult result = get("/products");
        if (!result.success()) return null;
        return gson.fromJson(result.body(), productListType);
    }

    public static List<ProductStats> top3Products() {
        ApiResult result = get("/products/top3");
        if (!result.success()) return null;
        return gson.fromJson(result.body(), productListType);
    }



    // ============================
    //       CATEGORY STATS
    // ============================

    private static final Type categoryListType = new TypeToken<List<CategoryStats>>(){}.getType();

    public static List<CategoryStats> bestCategories() {
        ApiResult result = get("/categories");
        if (!result.success()) return null;
        return gson.fromJson(result.body(), categoryListType);
    }

    public static List<CategoryStats> top3Categories() {
        ApiResult result = get("/categories/top3");
        if (!result.success()) return null;
        return gson.fromJson(result.body(), categoryListType);
    }
}
