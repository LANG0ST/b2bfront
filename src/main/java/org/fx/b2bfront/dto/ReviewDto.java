package org.fx.b2bfront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDto {
    private String author;
    private int rating;
    private String content;
    private String date;
}
