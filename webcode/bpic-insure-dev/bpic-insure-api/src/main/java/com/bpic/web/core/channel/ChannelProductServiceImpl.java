package com.bpic.web.core.channel;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bpic.common.core.redis.RedisCache;
import com.bpic.common.utils.http.HttpClient;
import com.bpic.mobile.product.ChannelServerData;
import com.bpic.mobile.product.ChannelServerGroupType;
import com.bpic.mobile.product.ChannelServerProduct;
import com.bpic.mobile.weixin.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 渤海销管系统-产品清单接口
 * by zyy
 * */

@Service("channelProductService")
public class ChannelProductServiceImpl {

    public static final Logger log = LoggerFactory.getLogger(ChannelProductServiceImpl.class);

    @Value("${channelUrl}")
    private String channelUrl;

    @Value("${channelCode}")
    private String channelCode;

    @Resource
    private RedisCache redisCache;

    public ChannelServerData productTypesFromChannel(){
        ChannelServerData channelData = new ChannelServerData();
        String key = MD5Util.md5("01" + "productTypes");
        List<ChannelServerGroupType> typeList = redisCache.getCacheList(key);
        if (typeList == null || typeList.size() == 0){
            try {
                log.info("=================start request channelServer get Types===========================");
                String result = HttpClient.sendPost(channelUrl, channelCode);
                log.info("=================request channelServer end===========================");
                log.info("=================channelServer back[{}]===========================", result);
                log.info("=================start JSONObject.parse(result) ===========================");
                JSONObject jsonObject = JSON.parseObject(result);
                log.info("=================start JSONObject.parse(result) ===========================");
                String data = jsonObject.get("data").toString();
                String common = JSON.parseObject(data).get("common").toString();
                if (common.contains("01")){
                    List<ChannelServerGroupType> types = JSON.parseArray(JSON.parseObject(data).getString("groupsType"), ChannelServerGroupType.class);
                    channelData.setGroupsType(types);
                    redisCache.setCacheList(key, types);
                }
            } catch (IOException e) {
                log.error("productTypesFromChannel-请求渤海销管系统-产品清单接口异常，异常信息:[{}]",e.getMessage());
                e.printStackTrace();
            }
        }else {
            channelData.setGroupsType(typeList);
        }
        return channelData;
    }


    public ChannelServerData productListFromChannel(String code){
        ChannelServerData channelData = new ChannelServerData();
        String key = MD5Util.md5(code + "productList");
        Object cacheObject = redisCache.getCacheObject(key);
        if (cacheObject == null || cacheObject==""){
            try {
                log.info("=================start request channelServer get products===========================");
                String result = HttpClient.sendPost(channelUrl, channelCode);
                log.info("=================request channelServer end===========================");
                log.info("=================channelServer back[{}]===========================", result);
                log.info("=================start JSONObject.parse(result) ===========================");
                JSONObject jsonObject = JSON.parseObject(result);
                log.info("=================start JSONObject.parse(result) ===========================");
                log.info("common1:[{}] ===========================", jsonObject.get("data"));
                String data = jsonObject.get("data").toString();
                String common = JSON.parseObject(data).get("common").toString();
                if (common.contains("01")){
                    List<ChannelServerProduct> products = JSON.parseArray(JSON.parseObject(data).getString("groups"), ChannelServerProduct.class);
                    if (products == null || products.size() <= 0){
                        log.info("productListFromChannel-请求渤海销管系统-产品清单接口返回数据为空!");
                        return channelData;
                    }else {
                        if ("01".equals(code)){
                           redisCache.setCacheObject(key, JSONObject.toJSON(products));
                           channelData.setGroups(products);
                        }else {
                            List<ChannelServerProduct> redisProducts = new ArrayList<>();
                            for (ChannelServerProduct product:products) {
                                if (product.getProductGroupCode().equals(code)){
                                    redisProducts.add(product);
                                }
                            }
                            if (redisProducts == null || redisProducts.size() == 0){
                                log.info("productListFromChannel-根据code类型遍历产品-结果为空!");
                            }else {
                                redisCache.setCacheObject(key, redisProducts);
                            }
                            channelData.setGroups(redisProducts);
                        }
                    }
                }
            } catch (IOException e) {
                log.error("请求渤海销管系统-产品清单接口异常，异常信息:[{}]",e.getMessage());
                e.printStackTrace();
            }
        }else {
            List<ChannelServerProduct> list = JSONObject.parseArray(cacheObject.toString(), ChannelServerProduct.class);
            channelData.setGroups(list);
        }
        return channelData;
    }


}
