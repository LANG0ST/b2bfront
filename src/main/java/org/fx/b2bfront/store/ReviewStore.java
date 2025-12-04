package org.fx.b2bfront.store;

import org.fx.b2bfront.dto.ReviewDto;

import java.util.*;

public class ReviewStore {

    private static final Map<Long, List<ReviewDto>> reviews = new HashMap<>();

    public static List<ReviewDto> getReviews(long productId) {
        return reviews.getOrDefault(productId, new ArrayList<>());
    }

    public static void addReview(long productId, ReviewDto review) {
        reviews.computeIfAbsent(productId, k -> new ArrayList<>()).add(review);
    }
}
