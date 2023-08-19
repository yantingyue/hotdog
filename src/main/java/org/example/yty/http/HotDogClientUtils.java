package org.example.yty.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.example.yty.domain.HotDogSellingResp;
import org.example.yty.domain.ProductDetailResp;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HotDogClientUtils {
    public static <T> T doPost(String url, String token, String jsonString, Class<T> tClass) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setHeader("token", token);
        String time = String.valueOf(System.currentTimeMillis());
        String val = time + "5c33494d1b277902d1b78f98093f6fd4";
        String sign = DigestUtils.md5DigestAsHex(val.getBytes(StandardCharsets.UTF_8));
        httpPost.setHeader("sign", sign);
        httpPost.setHeader("timestamp", time);

        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        // 由客户端执行(发送)Post请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                return JSON.parseObject(result, tClass);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
