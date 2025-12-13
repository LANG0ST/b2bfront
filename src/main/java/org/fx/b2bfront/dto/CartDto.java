package org.fx.b2bfront.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartDto {

    private Long companyId;
    private List<CartItem> items;

    private double subtotal;
}
