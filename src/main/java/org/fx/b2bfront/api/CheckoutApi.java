package org.fx.b2bfront.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.fx.b2bfront.dto.CommandeDto;
import org.fx.b2bfront.dto.CartItem;

import java.io.IOException;
import java.util.*;

public class CheckoutApi {

    private static final String BASE_URL = "http://localhost:8082/api/commandes";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    /**
     * Create a new order on backend:
     * - buyerId → company.id
     * - lignes → items in cart
     */
    public static CommandeDto createOrder(Long buyerId, List<CartItem> cartItems) {

        // ---- Build JSON payload exactly as backend expects ----

        Map<String, Object> payload = new HashMap<>();

        // Buyer company structure
        Map<String, Object> buyerMap = new HashMap<>();
        buyerMap.put("id", buyerId);

        payload.put("company", buyerMap);

        // Order lines
        List<Map<String, Object>> lines = new ArrayList<>();

        for (CartItem item : cartItems) {

            Map<String, Object> produitMap = new HashMap<>();
            produitMap.put("id", item.getProductId());

            Map<String, Object> line = new HashMap<>();
            line.put("produit", produitMap);
            line.put("quantite", item.getQuantity());
            line.put("prixUnitaire", item.getUnitPrice());

            lines.add(line);
        }

        payload.put("lignes", lines);

        String jsonBody = gson.toJson(payload);

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("Erreur backend: " + response.code());
            }

            String json = response.body().string();
            return gson.fromJson(json, CommandeDto.class);

        } catch (IOException e) {
            throw new RuntimeException("Erreur réseau: " + e.getMessage(), e);
        }
    }
}
