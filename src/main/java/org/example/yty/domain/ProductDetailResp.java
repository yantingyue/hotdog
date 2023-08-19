package org.example.yty.domain;

import lombok.Data;

@Data
public class ProductDetailResp {
    private int code;
    private String msg;
    private ProductDetailDataResp data;
}
