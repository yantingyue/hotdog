package org.example.yty.domain;

import lombok.Data;

@Data
public class CancelResp {
    private int code;
    private String msg;
    private String data;
}
