package org.fx.b2bfront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Long productId;
    private String name;
    private String imageUrl;

    private double unitPrice;
    private int quantity;
}
