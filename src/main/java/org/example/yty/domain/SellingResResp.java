package org.example.yty.domain;

import java.util.List;

public class SellingResResp {
    private long second_id;
    private long product_id;
    private long size_id;
    private int nft_product_size_id;
    private long order_id;
    private int is_lock;
    private int price;
    private String product_title;
    private String receiver_city;
    private String receiver_province;
    private String receiver_region;
    private String login_name;
    private String user_pic_url;
    private String product_picture;
    private int is_discount;
    private int sell_content;
    private int seller_user_id;
    private List<String> extend;
    private int is_owner;

    public long getSecond_id() {
        return second_id;
    }

    public void setSecond_id(long second_id) {
        this.second_id = second_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public long getSize_id() {
        return size_id;
    }

    public void setSize_id(long size_id) {
        this.size_id = size_id;
    }

    public int getNft_product_size_id() {
        return nft_product_size_id;
    }

    public void setNft_product_size_id(int nft_product_size_id) {
        this.nft_product_size_id = nft_product_size_id;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public int getIs_lock() {
        return is_lock;
    }

    public void setIs_lock(int is_lock) {
        this.is_lock = is_lock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getReceiver_city() {
        return receiver_city;
    }

    public void setReceiver_city(String receiver_city) {
        this.receiver_city = receiver_city;
    }

    public String getReceiver_province() {
        return receiver_province;
    }

    public void setReceiver_province(String receiver_province) {
        this.receiver_province = receiver_province;
    }

    public String getReceiver_region() {
        return receiver_region;
    }

    public void setReceiver_region(String receiver_region) {
        this.receiver_region = receiver_region;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getUser_pic_url() {
        return user_pic_url;
    }

    public void setUser_pic_url(String user_pic_url) {
        this.user_pic_url = user_pic_url;
    }

    public String getProduct_picture() {
        return product_picture;
    }

    public void setProduct_picture(String product_picture) {
        this.product_picture = product_picture;
    }

    public int getIs_discount() {
        return is_discount;
    }

    public void setIs_discount(int is_discount) {
        this.is_discount = is_discount;
    }

    public int getSell_content() {
        return sell_content;
    }

    public void setSell_content(int sell_content) {
        this.sell_content = sell_content;
    }

    public int getSeller_user_id() {
        return seller_user_id;
    }

    public void setSeller_user_id(int seller_user_id) {
        this.seller_user_id = seller_user_id;
    }

    public List<String> getExtend() {
        return extend;
    }

    public void setExtend(List<String> extend) {
        this.extend = extend;
    }

    public int getIs_owner() {
        return is_owner;
    }

    public void setIs_owner(int is_owner) {
        this.is_owner = is_owner;
    }
}
