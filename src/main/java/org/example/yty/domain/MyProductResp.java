package org.example.yty.domain;

import lombok.Data;

@Data
public class MyProductResp {
    private int code;
    private String msg;
    private MyProductDataResp data;
}
