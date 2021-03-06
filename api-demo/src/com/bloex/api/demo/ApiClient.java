package com.bloex.api.demo;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ApiClient {

    //API地址
    private String apiUrl = "https://api.bloex.com";

    //替换成自己的API-KEY
    private String apiKey = "c7eedd959457407181c051bb3366e0c0";
    //替换成自己的SECRET-KEY
    private String secretKey = "976A66DA05E3494F989AE1169A0CEC67";

    public String submitOrder(String symbol, String type, double price, double amount) {
        Map<String, String> params = new HashMap<>();
        params.put("apiKey", apiKey);
        params.put("symbol", symbol);

        if ("buy".equals(type)) {
            params.put("type", "1");
        } else {
            params.put("type", "2");
        }

        params.put("price", String.valueOf(price));
        params.put("amount", String.valueOf(amount));
        params.put("time", String.valueOf(System.currentTimeMillis()));

        String signature = sign(params);
        params.put("signature", signature);

        String result = null;

        try {
            result = post("/order/submit", params);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String sign(Map<String, String> parameters) {
        //按顺序拼接内容
        String url = createSortedParameterUrl(parameters);
        //对内容进行签名
        String result = sign(url.getBytes(), secretKey.getBytes());
        return result;
    }

    private String createSortedParameterUrl(Map<String, String> parameters) {
        if (parameters == null) {
            return null;
        }

        //按英文参数名排序
        List<String> keys = new ArrayList<>(parameters.keySet());
        Collections.sort(keys);

        //按顺序拼接内容
        String result = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = parameters.get(key);
            if (i == keys.size() - 1) {
                result = result + key + "=" + value;
            } else {
                result = result + key + "=" + value + "&";
            }
        }

        return result;
    }

    private String sign(byte[] content, byte[] secretKey) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            return toHex(mac.doFinal(content));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String toHex(byte[] input) {
        StringBuilder builder = new StringBuilder();
        String value;
        for (int n = 0; input != null && n < input.length; n++) {
            value = Integer.toHexString(input[n] & 0XFF);
            if (value.length() == 1)
                builder.append('0');
            builder.append(value);
        }

        return builder.toString();
    }

    private String post(String requestUrl, Map<String, String> params) throws HttpException, IOException {
        String url = apiUrl + requestUrl;
        //http post
        String result = "";
        return result;
    }
}
