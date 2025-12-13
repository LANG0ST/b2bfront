package org.fx.b2bfront.utils;

import java.util.List;
import java.util.Map;

public class CategoryAttributes {

    public static final Map<Integer, List<String>> MAP = Map.of(
            1, List.of("brand", "power", "weight"),
            2, List.of("brand", "material"),
            3, List.of("brand", "voltage", "phase"),
            4, List.of("brand", "norm"),
            5, List.of("brand", "type"),
            6, List.of("brand", "diameter")
    );

    public static List<String> get(int categoryId) {
        return MAP.getOrDefault(categoryId, List.of("brand"));
    }
}
