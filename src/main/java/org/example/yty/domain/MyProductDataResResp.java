package org.example.yty.domain;

import lombok.Data;

@Data
public class MyProductDataResResp {
    private long id;
    private String receiver_city;
    private String receiver_province;
    private String receiver_region;
    private String product_picture;
    private int pay_type;
    private int is_on_sale;
    private int is_owner;
    private int is_auction;
    private int is_pledge;
    private int is_can_transfer;
    private String price;
    private String code;
    private int is_pack_sell;
    private String bought_price;
    private int is_gas;
    private int is_zgt;
    private int is_xyz;
    private int is_box_need_pay;
    private int can_open_box_nft;
}
