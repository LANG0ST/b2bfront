package org.fx.b2bfront.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.fx.b2bfront.dto.CompanyDto;

import java.io.IOException;
import java.util.List;

public class CompanyApi {

    private static final String BASE_URL = "http://localhost:8082/api/companies";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // =======================================================
    // 1) GET ALL COMPANIES
    // =======================================================
    public static List<CompanyDto> findAll() {
        try {
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();

            return gson.fromJson(json, new TypeToken<List<CompanyDto>>(){}.getType());

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch companies: " + e.getMessage());
        }
    }

    // =======================================================
    // 2) GET COMPANY BY ID
    // =======================================================

    public static CompanyDto getById(Long id) {

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
            return gson.fromJson(json, CompanyDto.class);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load company: " + e.getMessage(), e);
        }
    }

    // =======================================================
    // 3) CREATE COMPANY
    // =======================================================
    public static CompanyDto create(CompanyDto companyDto) {

        String jsonBody = gson.toJson(companyDto);

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            String json = response.body().string();
            return gson.fromJson(json, CompanyDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create company: " + e.getMessage(), e);
        }
    }

    // =======================================================
    // 4) UPDATE COMPANY
    // =======================================================
    public static CompanyDto update(Long id, CompanyDto companyDto) {

        String jsonBody = gson.toJson(companyDto);

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            String json = response.body().string();
            return gson.fromJson(json, CompanyDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update company: " + e.getMessage(), e);
        }
    }

    // =======================================================
    // 5) ENABLE COMPANY
    // =======================================================
    public static CompanyDto enable(Long id) {

        Request request = new Request.Builder()
                .url(BASE_URL + "/enable/" + id)
                .put(RequestBody.create(new byte[0], null))
                .build();

        try (Response response = client.newCall(request).execute()) {

            String json = response.body().string();
            return gson.fromJson(json, CompanyDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to enable company: " + e.getMessage(), e);
        }
    }

    // =======================================================
    // 6) DISABLE COMPANY
    // =======================================================
    public static CompanyDto disable(Long id) {

        Request request = new Request.Builder()
                .url(BASE_URL + "/disable/" + id)
                .put(RequestBody.create(new byte[0], null))
                .build();

        try (Response response = client.newCall(request).execute()) {

            String json = response.body().string();
            return gson.fromJson(json, CompanyDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to disable company: " + e.getMessage(), e);
        }
    }

    // =======================================================
    // 7) DELETE COMPANY
    // =======================================================
    public static void delete(Long id) {

        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to delete company, backend returned: " + response.code());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete company: " + e.getMessage(), e);
        }
    }
}
