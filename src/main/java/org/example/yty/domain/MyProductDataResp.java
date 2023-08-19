package org.example.yty.domain;

import lombok.Data;

import java.util.List;

@Data
public class MyProductDataResp {
    private List<MyProductDataResResp> res;
    private Integer nft_count;
}
