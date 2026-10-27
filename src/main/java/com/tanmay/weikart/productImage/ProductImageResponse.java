package com.tanmay.weikart.productImage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageResponse {
    private Integer id;
    private String url;
}
