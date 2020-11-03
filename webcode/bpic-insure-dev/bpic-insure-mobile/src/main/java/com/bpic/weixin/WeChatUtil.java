package com.bpic.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.*;

public class WeChatUtil {
    /**
     * 获取token和openid
     * @param wechatAuthorizationUrl
     * @param code
     * @param APPID
     * @param APPSecret
     * @return
     */
    public static JSONObject getWxUserOpenid(String wechatAuthorizationUrl,String code, String APPID, String APPSecret) {
        //拼接url
        StringBuilder url = new StringBuilder(wechatAuthorizationUrl);
        url.append("appid=");//appid设置
        url.append(APPID);
        url.append("&secret=");//secret设置
        url.append(APPSecret);
        url.append("&code=");//code设置
        url.append(code);
        url.append("&grant_type=authorization_code");
        System.out.println(url);
        try {
            HttpClient client = HttpClientBuilder.create().build();//构建一个Client
            HttpGet get = new HttpGet(url.toString());    //构建一个GET请求
            HttpResponse response = client.execute(get);//提交GET请求
            HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
            String content = EntityUtils.toString(result);
            return JSON.parseObject(content);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户信息
     * @param url
     * @param openid
     * @param access_token
     * @return
     */
    public static JSONObject getWxUserInfo(String url,String openid,String access_token){
        String infoUrl = url + access_token + "&openid=" + openid
                + "&lang=zh_CN";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(infoUrl);
        HttpResponse response;
        try {
            String content;
            response = client.execute(get);
            HttpEntity result = response.getEntity();
            content = EntityUtils.toString(result,"UTF-8");
            return JSON.parseObject(content);
//            String encryptedData = (String)jsonObject.get("encryptedData");
//            String iv = (String)jsonObject.get("iv");
//            String sessionKey = (String)jsonObject.get("session_key");
//            return getUserInfo(encryptedData, sessionKey, iv);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     *
     * @param encryptedData
     * @param sessionKey
     * @param iv
     * @return String
     */
    public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv){
        // 被加密的数据
        byte[] dataByte = Base64.getDecoder().decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.getDecoder().decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.getDecoder().decode(iv);

        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
