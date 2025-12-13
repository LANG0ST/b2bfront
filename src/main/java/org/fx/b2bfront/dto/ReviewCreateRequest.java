package org.fx.b2bfront.dto;

public class ReviewCreateRequest {
    public Long productId;
    public Long companyId;
    public int rating;
    public String comment;

    public ReviewCreateRequest(Long productId, Long companyId, int rating, String comment) {
        this.productId = productId;
        this.companyId = companyId;
        this.rating = rating;
        this.comment = comment;
    }
}
