package com.bpic.mobile.weChat;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.redis.RedisCache;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.http.HttpClient;
import com.bpic.weixin.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("api/weChat")
public class JsApiTicketController {

    public static final Logger log = LoggerFactory.getLogger(JsApiTicketController.class);

    @Value("${wechat.appId}")
    private String appId;
    @Value("${wechat.secret}")
    private String secret;
    @Value("${wechat.refreshToken}")
    private String refreshTokenUrl;
    @Value("${wechat.jsApiTicket}")
    private String jsApiTicketUrl;
    @Autowired
    private RedisCache redisCache;

    /**
     * 获取JsApiToken配置信息
     * */
    @PostMapping("jsApiConfig")
    public AjaxResult getJsApiToken(@RequestBody HashMap<String, String> map){
        AjaxResult ajaxResult = null;
        if (map.isEmpty() || StringUtils.isEmpty(map.get("url"))){
            return ajaxResult = AjaxResult.error("url参数不能为空!");
        }
        //先获取用户token信息，根据token信息获取ticket,然后生成签名
        //1、先获取用户token信息
        String accessToken = getAccessToken();
        if (accessToken.equals("fail")){
            return ajaxResult = AjaxResult.error("获取access_token信息失败!");
        }
        String jsApiTicket = getJsApiTicket(accessToken);
        if (accessToken.equals("fail")){
            return ajaxResult = AjaxResult.error("获取jsApiTicket信息失败!");
        }
        Map<String, String> sign = sign(jsApiTicket, map.get("url"));
        return ajaxResult = AjaxResult.success("请求成功!", sign);
    }



    /**
     * 获取用户token信息
     * */
    public String getAccessToken() {
        String key = MD5Util.md5(appId);
        String token = redisCache.getCacheObject(key);
        if (StringUtils.isEmpty(token)){
            log.info("==================WeChatController.getAccessToken--缓存用户token信息已失效===========================");
            String requestUrl = refreshTokenUrl.replace("APPID", appId).replace("APPSECRET", secret);
            try {
                log.info("==================WeChatController.getAccessToken请求获取用户token信息=============start==============");
                log.info("==================getAccessToken请求requestUrl:{}", requestUrl);
                String responseContent = HttpClient.sendGet(requestUrl);
                log.info("==================getAccessToken获取responseContent:{}============================", responseContent);
                log.info("==================WeChatController.getAccessToken请求获取用户token信息=============end==============");
                if (responseContent.contains("access_token")){
                    String result = JSON.parseObject(responseContent).getString("access_token");
                    log.info("==================getAccessToken获取result:{}=======================================", result);
                    redisCache.setCacheObject(key, result, 2, TimeUnit.HOURS);//token存放redis
                    return result;
                } else {
                    return "fail";
                }
            } catch (Exception e) {
                log.error("================WeChatController.getAccessToken获取用户token信息异常:{}", e.getMessage());
                return "fail";
            }
        }else {
            return token;
        }
    }

    /**
     * 创建微信菜单
     * @param requestBody
     * @return
     */
    @PostMapping("/createMenu")
    public AjaxResult createMenu(@RequestBody HashMap<String, Object> requestBody){
        String token = getAccessToken();
        String url= "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;
        try{
            log.info("==========url:{}===========",url);
            log.info("-----------请求参数：{}------------",requestBody);
            String s = HttpClient.sendPost(url, JSONObject.toJSONString(requestBody));
            log.info("------------创建结果：{}------------",s);
            if(s.contains("ok")){
                return new AjaxResult(200,"操作成功",s);
            }
            return new AjaxResult(400,"操作失败",s);
        }catch (Exception e){
            log.error("请求微信平台创建菜单失败：{}",e.getMessage());
            return new AjaxResult(400,"操作失败");
        }
    }

    /**
     * 删除微信菜单
     * @param
     * @return
     */
    @GetMapping("/delete")
    public AjaxResult delete(){
        String token = getAccessToken();
        String url= "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + token;
        try{
            log.info("==========url:{}===========",url);
            String s = HttpClient.sendGet(url);
            log.info("------------创建结果：{}------------",s);
            if(s.contains("ok")){
                return new AjaxResult(200,"操作成功",s);
            }
            return new AjaxResult(400,"操作失败",s);
        }catch (Exception e){
            log.error("请求微信平台删除菜单失败：{}",e.getMessage());
            return new AjaxResult(400,"操作失败");
        }
    }

    /**
     * 获取JsApiTicket信息
     * */
    public String getJsApiTicket(String accessToken) {
        String key = "jsApi" + accessToken;
        String ticket = redisCache.getCacheObject(key);
        if (StringUtils.isEmpty(ticket)){
            try {
                log.info("==================WeChatController.getJsApiTicket获取JsApiTicket信息=============start==============");
                log.info("==================getJsApiTicket获取accessToken信息:{}", accessToken);
                String requestUrl = jsApiTicketUrl.replace("ACCESS_TOKEN", accessToken);
                String responseContent = HttpClient.sendGet(requestUrl);
                log.info("==================getJsApiTicket获取responseContent:{}============================", responseContent);
                if (StringUtils.isEmpty(responseContent)){
                    return "fail";
                }else {
                    String result = JSON.parseObject(responseContent).getString("ticket");
                    log.info("==================getJsApiTicket获取result:{}============================", result);
                    log.info("==================WeChatController.getJsApiTicket获取JsApiTicket信息=============end==============");
                    redisCache.setCacheObject(key, result, 2, TimeUnit.HOURS);//ticket存放redis
                    return result;
                }
            } catch (Exception e) {
                log.error("================WeChatController.getAccessToken获取用户token信息异常:{}", e.getMessage());
                return "fail";
            }
        }else {
            return ticket;
        }
    }


    /**
     * 生成签名-JSSDK配置信息使用
     * */
    public static Map<String, String> sign(String jsApi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String params;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序
        params = "jsapi_ticket=" + jsApi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        log.info("参数拼接:{}", params);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(params.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
            log.info("=======================jsApiTicket生成签名sign:{}===========================", signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ret.put("url", url);
        ret.put("jsapi_ticket", jsApi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 生成随机字符串-JSSDK配置信息使用
     * */
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取当前时间戳-JSSDK配置信息使用
     * */
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
