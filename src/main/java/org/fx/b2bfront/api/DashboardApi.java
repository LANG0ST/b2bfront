package org.fx.b2bfront.api;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.fx.b2bfront.dto.CommandeDto;
import org.fx.b2bfront.dto.LigneCommandeDto;
import org.fx.b2bfront.utils.GsonProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DashboardApi {

    private static final String BASE_URL = "http://localhost:8082/api/commandes";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = GsonProvider.get();

    // ======================================================
    // INTERNAL: Fix product name extraction
    // ======================================================
    private static List<CommandeDto> fixProduitNames(String json) {

        JsonElement root = JsonParser.parseString(json);

        List<CommandeDto> fixedList = new ArrayList<>();

        if (root.isJsonObject()) {
            // convert single object
            fixedList.add(fixSingleCommande(root.getAsJsonObject()));
        }
        else if (root.isJsonArray()) {
            for (JsonElement el : root.getAsJsonArray()) {
                fixedList.add(fixSingleCommande(el.getAsJsonObject()));
            }
        }

        return fixedList;
    }

    private static CommandeDto fixSingleCommande(JsonObject jsonCmd) {

        // Convert JSON → DTO normally
        CommandeDto dto = gson.fromJson(jsonCmd, CommandeDto.class);

        // Extract lignes in raw form
        JsonArray lignesRaw = jsonCmd.getAsJsonArray("lignes");

        if (lignesRaw != null && dto.getLignes() != null) {

            for (int i = 0; i < lignesRaw.size(); i++) {

                JsonObject ligneObj = lignesRaw.get(i).getAsJsonObject();
                LigneCommandeDto dtoLigne = dto.getLignes().get(i);

                if (ligneObj.has("produit") && ligneObj.get("produit").isJsonObject()) {
                    JsonObject produitObj = ligneObj.getAsJsonObject("produit");

                    if (produitObj.has("name")) {
                        dtoLigne.setProduitName(produitObj.get("name").getAsString());
                    }
                }
            }
        }

        return dto;
    }

    // ======================================================
    // BUYER ORDERS
    // ======================================================
    public static List<CommandeDto> getBuyerOrders(Long companyId) throws Exception {

        String url = BASE_URL + "/Company/" + companyId;

        Request req = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response res = client.newCall(req).execute()) {

            if (!res.isSuccessful()) throw new RuntimeException("HTTP " + res.code());

            String json = res.body().string();
            return fixProduitNames(json);
        }
    }

    // ======================================================
    // SELLER ORDERS
    // ======================================================
    public static List<CommandeDto> getSellerOrders(Long sellerId) throws Exception {

        String url = BASE_URL + "/seller/" + sellerId;

        Request req = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response res = client.newCall(req).execute()) {

            if (!res.isSuccessful()) throw new RuntimeException("HTTP " + res.code());

            String json = res.body().string();
            return fixProduitNames(json);
        }
    }

    // ======================================================
    // UPDATE ORDER STATUS
    // ======================================================
    public static void updateOrderStatus(Long orderId, String statut) throws Exception {

        String url = BASE_URL + "/" + orderId + "/sellerDecision";
        String bodyJson = "{\"statut\":\"" + statut + "\"}";

        RequestBody body = RequestBody.create(
                bodyJson,
                MediaType.parse("application/json")
        );

        Request req = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        try (Response res = client.newCall(req).execute()) {

            if (!res.isSuccessful())
                throw new RuntimeException("Failed to update statut: HTTP " + res.code());
        }
    }
}
