package org.fx.b2bfront.utils;

import com.google.gson.*;
import java.time.LocalDateTime;

public class GsonProvider {

    public static final Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, type, ctx) ->
                                LocalDateTime.parse(json.getAsString()))
                .create();
    }

    public static Gson get() {
        return gson;
    }
}
