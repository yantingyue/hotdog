package org.example.yty.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.yty.config.PwdConfig;
import org.example.yty.domain.*;
import org.example.yty.http.HotDogClientUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class SellingTaskProcessService implements InitializingBean {
    private final int maxSaleSize = 1;
    private static final String sellingListUrl = "https://api.aichaoliuapp.cn/aiera/ai_match_trading/nft_second/sell_product/list";
    private static final String mySellingProductUrl = "https://api.aichaoliuapp.cn/aiera/ai_match_trading/nft/order/list2/detail_list";
    private static final String cancelSellingProductUrl = "https://api.aichaoliuapp.cn/aiera/ai_match_trading/nft_second/sell/cancel";
    private static final String productOrderDetailUrl = "https://api.aichaoliuapp.cn/aiera/ai_match_trading/product_order/detail";
    private static final String saleUrl = "https://api.aichaoliuapp.cn/aiera/ai_match_trading/nft_second/sell/save";
    private LinkedBlockingQueue<SellingTaskRequestDTO> sellQueue = new LinkedBlockingQueue<>(8);
    private static final Map<String, Integer> myProductCount = new HashMap<>();

    private static final Map<String, Boolean> myProductSaleState = new HashMap<>();

    private static final List<String> exists = new ArrayList<>();

    public void start() throws InterruptedException {
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Thread thread = new Thread(new SellingTaskExecutorService("Thread-selling-" + i));
            list.add(thread);
            thread.setDaemon(true);
            thread.start();
        }
        Thread.currentThread().join();
    }

    public void addQueue(SellingTaskRequestDTO requestDTO) {
        String key = requestDTO.getToken() + "-" + requestDTO.getProductId();
        if (exists.contains(key)) {
            System.out.println("配置重复了：" + key);
            return;
        }
        sellQueue.offer(requestDTO);
        exists.add(key);
    }

    public void doSellProcess(SellingTaskRequestDTO take) throws Exception {
        while (true) {
            try {
                String key = take.getToken() + "-" + take.getProductId();
                if (myProductSaleState.get(key) != null && myProductSaleState.get(key).equals(Boolean.FALSE)) {
                    System.out.println("此商品已卖完了 token:" + take.getToken() + "--product_id:" + take.getProductId() + "--Nft_product_size_id:" + take.getNft_product_size_id());
                    return;
                }
                HotDogSellingRequest build = HotDogSellingRequest.builder().product_id(take.getProductId())
                        .order_by("price")
                        .unlock(0)
                        .pageNumber(1)
                        .pageSize(10)
                        .nft_product_size_id(take.getNft_product_size_id())
                        .build();
                HotDogSellingResp resp = HotDogClientUtils.doPost(sellingListUrl, take.getToken(), JSON.toJSON(build).toString(), HotDogSellingResp.class);

                assert resp != null;
                if (resp.getCode() == 0) {
                    if (resp.getData() == null || resp.getData().getRes().isEmpty()) {
                        System.out.println("此商品寄售列表为空 token:" + take.getToken() + "--product_id:" + take.getProductId() + "--Nft_product_size_id:" + take.getNft_product_size_id());
                        return;
                    }
                } else {
                    System.out.println("休息一下，此商品寄售 token:" + take.getToken() + "--product_id:" + take.getProductId() + "--Nft_product_size_id:" + take.getNft_product_size_id() + "res:" + JSON.toJSON(resp));
                    Thread.sleep(10 * 1000);
                }

                SellingResResp sellingResResp = resp.getData().getRes().get(0);

                if (sellingResResp.getIs_owner() == 1) {
                    System.out.println("此商品寄售你是第一个，等等吧 token:" + take.getToken() + "--product_id:" + take.getProductId() + "--Nft_product_size_id:" + take.getNft_product_size_id());
                    Thread.sleep(60 * 1000);
                    continue;
                }

                List<MyProductDataResResp> myProductRes = saleList(take);

                if (myProductRes.isEmpty()) {
                    System.out.println("此商品你没有了 token:" + take.getToken() + "--product_id:" + take.getProductId() + "--Nft_product_size_id:" + take.getNft_product_size_id());
                    return;
                }

                List<MyProductDataResResp> myOnSaleList = new ArrayList<>();
                for (MyProductDataResResp re : myProductRes) {
                    if (re.getIs_on_sale() == 1) {
                        myOnSaleList.add(re);
                    }
                }

                if (myOnSaleList.size() > take.getSellSize() || myOnSaleList.size() >= maxSaleSize) {
                    System.out.println("maxSaleSize");
                    Thread.sleep(30 * 1000);
                    cancelSale(take, myOnSaleList);
                } else {
                    //开始卖
                    myProductRes.removeAll(myOnSaleList);
                    if (myProductRes.isEmpty()) {
                        System.out.println("此商品你没有了*** token:" + take.getToken() + "--product_id:" + take.getProductId() + "--Nft_product_size_id:" + take.getNft_product_size_id());
                        return;
                    }
                    JSONObject object = new JSONObject();
                    object.put("order_id", myProductRes.get(0).getId());
                    object.put("prop_user_uuid", "");
                    object.put("price", String.valueOf(Math.max(1, sellingResResp.getPrice() - 1)));
                    object.put("pwd", PwdConfig.getPwd(take.getPwd()));
                    System.out.println(JSON.toJSON(object));
                    HotDogClientUtils.doPost(saleUrl, take.getToken(), JSON.toJSON(object).toString(), ProductDetailResp.class);
                    System.out.println("开卖");
                }
                Thread.sleep(30 * 1000);
            } catch (Exception exception) {
                System.out.println("error" + exception.toString());
            }
        }
    }

    public void sellProcess() {
        while (true) {
            try {
                SellingTaskRequestDTO take = sellQueue.take();
                doSellProcess(take);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void cancelSale(SellingTaskRequestDTO take, List<MyProductDataResResp> onsaleList) {
        for (MyProductDataResResp myProductDataResResp : onsaleList) {
            Map<String, Long> param = new HashMap<>();
            param.put("id", myProductDataResResp.getId());
            //获得second_id
            ProductDetailResp resp = HotDogClientUtils.doPost(productOrderDetailUrl, take.getToken(), JSON.toJSON(param).toString(), ProductDetailResp.class);
            if (resp == null || resp.getData() == null) {
                continue;
            }
            Map<String, Integer> cancel = new HashMap<>();
            cancel.put("second_id", resp.getData().getSecond_id());
            CancelResp cancelResp = HotDogClientUtils.doPost(cancelSellingProductUrl, take.getToken(), JSON.toJSON(cancel).toString(), CancelResp.class);
            System.out.println("cancel result:" + JSON.toJSON(cancelResp));
        }
    }

    private List<MyProductDataResResp> saleList(SellingTaskRequestDTO taskRequestDTO) throws Exception {
        MyProductRequestDTO build = MyProductRequestDTO.builder().product_id(taskRequestDTO.getProductId())
                .nft_product_size_id(taskRequestDTO.getNft_product_size_id())
                .pageNumber(1)
                .pageSize(100)
                .is_retrieved(0)
                .build();
        MyProductResp resp = HotDogClientUtils.doPost(mySellingProductUrl, taskRequestDTO.getToken(), JSON.toJSON(build).toString(), MyProductResp.class);


        assert resp != null;
        if (resp.getCode() == 0) {
            if (resp.getData() == null || resp.getData().getRes().isEmpty()) {
                System.out.println("你没有这个商品 token:" + taskRequestDTO.getToken() + "--product_id:" + taskRequestDTO.getProductId() + "--Nft_product_size_id:" + taskRequestDTO.getNft_product_size_id());
                return new ArrayList<>();
            }
        }

        String key = taskRequestDTO.getToken() + "-" + taskRequestDTO.getProductId();
        if (myProductCount.containsKey(key)) {
            if (myProductCount.get(key) - resp.getData().getNft_count() >= taskRequestDTO.getSellSize()) {
                myProductSaleState.put(key, false);
            }
            System.out.println(key + "已卖出了" + (myProductCount.get(key) - resp.getData().getNft_count()));
        } else {
            myProductCount.put(key, resp.getData().getNft_count());
        }

        return resp.getData().getRes();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public class SellingTaskExecutorService extends Thread {
        public SellingTaskExecutorService(String name) {
            super(name);
        }

        @Override
        public void run() {
            SellingTaskProcessService.this.sellProcess();
        }
    }
}

