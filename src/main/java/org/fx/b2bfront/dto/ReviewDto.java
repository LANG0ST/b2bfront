package org.fx.b2bfront.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewDto {
    private Long id;
    private Long productId;
    private Long companyId;

    @SerializedName("authorName")
    private String author;

    private int rating;

    @SerializedName("comment")
    private String content;

    private LocalDateTime createdAt;
}
