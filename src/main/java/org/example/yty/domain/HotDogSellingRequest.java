package org.example.yty.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
@Data
public class HotDogSellingRequest implements Serializable {
    private Integer product_id;
    private Integer pageSize = 10;
    private int unlock = 1;
    private int pageNumber =1;

    private int nft_product_size_id;

    private String order_by = "price";
    private int prop_pack=0;

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setUnlock(int unlock) {
        this.unlock = unlock;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setNft_product_size_id(int nft_product_size_id) {
        this.nft_product_size_id = nft_product_size_id;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public void setProp_pack(int prop_pack) {
        this.prop_pack = prop_pack;
    }
}
