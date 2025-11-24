package org.fx.b2bfront.api;

import com.google.gson.Gson;
import okhttp3.*;
import org.fx.b2bfront.dto.LoginRequestFront;
import org.fx.b2bfront.dto.LoginResponseFront;
import org.fx.b2bfront.dto.RegisterRequestFront;

public class AuthApi {

    private static final String BASE_URL = "http://localhost:8082/api/auth";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();


    // ============================
    //          REGISTER
    // ============================
    public static ApiResult register(RegisterRequestFront req) {
        try {
            String json = gson.toJson(req);
            System.out.println("=== JSON SENDING TO BACKEND ===");
            System.out.println(gson.toJson(req));
            System.out.println("================================");

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(BASE_URL + "/register")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            String resBody = response.body().string();

            return new ApiResult(response.isSuccessful(), resBody);

        } catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }


    public record ApiResult(boolean success, String message) {}


    // ============================
    //            LOGIN
    // ============================
    public static LoginResult login(LoginRequestFront req) {
        try {
            String json = gson.toJson(req);

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                    .url(BASE_URL + "/login")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();

            if (!response.isSuccessful()) {
                return new LoginResult(false, null, res);
            }

            LoginResponseFront parsed = gson.fromJson(res, LoginResponseFront.class);

            return new LoginResult(true, parsed, null);

        } catch (Exception e) {
            return new LoginResult(false, null, e.getMessage());
        }
    }

    public record LoginResult(boolean success, LoginResponseFront data, String error) {}
}
