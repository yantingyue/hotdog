package org.example.yty.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SellingTaskRequestDTO {
    private String token;
    private Integer productId;
    private int sellSize;
    private Integer nft_product_size_id;
    private Integer pwd;
}
