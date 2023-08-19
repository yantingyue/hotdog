package org.example.yty.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyProductRequestDTO {
    private Integer product_id;
    private int pageNumber;
    private int is_retrieved;
    private int pageSize;
    private Integer nft_product_size_id;
}
