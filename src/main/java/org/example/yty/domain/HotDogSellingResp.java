package org.example.yty.domain;

public class HotDogSellingResp {
    private int code;
    private String msg;
    private SellingDataResp data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SellingDataResp getData() {
        return data;
    }

    public void setData(SellingDataResp data) {
        this.data = data;
    }
}
