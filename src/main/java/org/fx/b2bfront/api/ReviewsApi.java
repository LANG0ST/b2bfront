package org.fx.b2bfront.api;

import com.google.gson.Gson;
import okhttp3.*;
import org.fx.b2bfront.dto.ReviewCreateRequest;
import org.fx.b2bfront.dto.ReviewDto;
import org.fx.b2bfront.utils.GsonProvider;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ReviewsApi {

    private static final String BASE_URL = "http://localhost:8082/api/reviews";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();


    private static final java.lang.reflect.Type LIST_TYPE =
            new TypeToken<List<ReviewDto>>() {}.getType();

    public static List<ReviewDto> getLatestForProduct(long productId, int limit) throws Exception {
        String url = BASE_URL + "/product/" + productId + "/latest?limit=" + limit;

        Request req = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response res = client.newCall(req).execute()) {
            String json = res.body().string();
            return GsonProvider.get().fromJson(json, LIST_TYPE);
        }
    }

    public static List<ReviewDto> getAllForProduct(long productId) throws Exception {
        String url = BASE_URL + "/product/" + productId;

        Request req = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response res = client.newCall(req).execute()) {
            String json = res.body().string();
            return GsonProvider.get().fromJson(json, LIST_TYPE);
        }
    }

    public static void addReview(long productId, long companyId, int rating, String comment) throws Exception {

        ReviewCreateRequest bodyObj =
                new ReviewCreateRequest(productId, companyId, rating, comment);

        String json = gson.toJson(bodyObj);

        RequestBody body = RequestBody.create(
                json,
                MediaType.get("application/json")
        );

        Request req = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        client.newCall(req).execute().close();
    }

    // --- FIXED CLASS ---
    public static class ReviewCreateBody {
        public Long productId;
        public Long companyId;
        public int rating;
        public String comment;

        public ReviewCreateBody(Long productId, Long companyId, int rating, String comment) {
            this.productId = productId;
            this.companyId = companyId;
            this.rating = rating;
            this.comment = comment;
        }
    }

}
