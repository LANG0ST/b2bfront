package org.fx.b2bfront.api;

import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.fx.b2bfront.dto.NotificationDto;
import org.fx.b2bfront.utils.GsonProvider;

import java.util.List;

public class NotificationApi {

    private static final String BASE_URL = "http://localhost:8082/api/notifications";
    private static final OkHttpClient client = new OkHttpClient();

    public static List<NotificationDto> getNotifications(Long companyId) throws Exception {
        String url = BASE_URL + "/" + companyId;

        Request req = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response res = client.newCall(req).execute()) {
            if (!res.isSuccessful()) {
                throw new RuntimeException("HTTP " + res.code());
            }
            String json = res.body().string();
            return GsonProvider.get().fromJson(
                    json,
                    new TypeToken<List<NotificationDto>>() {}.getType()
            );
        }
    }

    public static void markAsRead(Long id) throws Exception {
        String url = BASE_URL + "/" + id + "/read";

        Request req = new Request.Builder()
                .url(url)
                .post(RequestBody.create(new byte[0],
                        MediaType.get("application/json")))
                .build();

        try (Response res = client.newCall(req).execute()) {
            if (!res.isSuccessful()) {
                throw new RuntimeException("HTTP " + res.code());
            }
        }
    }

    public static void markAllAsRead(List<NotificationDto> list) throws Exception {
        for (NotificationDto n : list) {
            if (!n.isRead()) {
                markAsRead(n.getId());
            }
        }
    }


}
