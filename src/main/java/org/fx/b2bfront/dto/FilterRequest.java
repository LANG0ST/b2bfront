package org.fx.b2bfront.dto;

import java.util.Map;

public class FilterRequest {
    private int categoryId;
    private Map<String, String> filters;

    public FilterRequest(int categoryId, Map<String, String> filters) {
        this.categoryId = categoryId;
        this.filters = filters;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public Map<String, String> getFilters() {
        return filters;
    }
}
