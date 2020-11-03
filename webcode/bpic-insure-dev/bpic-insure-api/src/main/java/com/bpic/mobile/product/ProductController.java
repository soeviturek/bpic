package com.bpic.mobile.product;


import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.Product;
import com.bpic.common.utils.StringUtils;
import com.bpic.system.service.ISysProductConfigService;
import com.bpic.system.service.IUserHistoryService;
import com.bpic.web.core.channel.ChannelProductServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 渤海销管系统-产品信息查询
 * by zyy
 * */

@RestController
@RequestMapping("api/product")
public class ProductController {

    public static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Resource
    private ChannelProductServiceImpl channelProductService;
    @Resource
    private ISysProductConfigService sysProductConfigService;
    @Resource
    private IUserHistoryService userHistoryService;

    @ApiOperation("获取产品类别列表")
    @GetMapping("type")
    public AjaxResult queryProductTypes(){
        AjaxResult ajaxResult = null;
        try{
            ChannelServerData data = channelProductService.productTypesFromChannel();
            ajaxResult = AjaxResult.success("操作成功!", data);
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取产品信息列表-异常:[{}]", e.getMessage());
            ajaxResult = AjaxResult.error(e.getMessage());
        }
        return ajaxResult;
    }

    @ApiOperation("获取产品信息列表")
    @PostMapping("list")
    public AjaxResult queryProductList(@RequestBody HashMap<String, String> map){
        AjaxResult ajaxResult = null;
        if (map.isEmpty() || StringUtils.isEmpty(map.get("code")) || StringUtils.isEmpty(map.get("phone"))){
            return ajaxResult = AjaxResult.error("参数不能为空!");
        }
        try{
            ChannelServerData data = channelProductService.productListFromChannel(map.get("code"));
            Set<String> products = sysProductConfigService.selectByTel(map.get("phone"));
            if (CollectionUtils.isEmpty(products) || CollectionUtils.isEmpty(data.getGroups())){
                ajaxResult = AjaxResult.success("操作成功!", new ChannelServerData());
            }else {
                List<ChannelServerProduct> list = new ArrayList<>();
                for (String productCode:products){
                    for (ChannelServerProduct pro:data.getGroups()) {
                        if (pro.getProductCode().equals(productCode)){
                            list.add(pro);
                        }
                    }
                }
                data.setGroups(list);
                ajaxResult = AjaxResult.success("操作成功!", data);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取产品信息列表-异常:[{}]", e);
            ajaxResult = AjaxResult.error(e.getMessage());
        }
        return ajaxResult;
    }
    /**
     * 根据名称查询产品种类
     * @param map
     * @return
     */
    @PostMapping("/selectProductByName")
    public AjaxResult selectProductByName(@RequestBody Map<String,String> map){
        AjaxResult ajaxResult = null;
        String productName = map.get("productName");
        String telPhone = map.get("telPhone");
        log.info("productName:{}",productName);
        try {
            ChannelServerData data = channelProductService.productListFromChannel("01");
            if (data == null) {
                ajaxResult = AjaxResult.fail("没有查询到相关数据!");
            } else {
                Set<String> products = sysProductConfigService.selectByTel(telPhone);
                List<ChannelServerProduct> list = new ArrayList<>();
                for (String productCode:products){
                    for (ChannelServerProduct pro:data.getGroups()) {
                        if (pro.getProductCode().equals(productCode)){
                            list.add(pro);
                        }
                    }
                }
                if (!StringUtils.isEmpty(productName)) {
                    list = list.stream().filter(t -> t.getProductCName().contains(productName)).collect(Collectors.toList());
                }
                ajaxResult = AjaxResult.success("操作成功!", list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取产品信息列表-异常:[{}]", e.getMessage());
            ajaxResult = AjaxResult.error("查询失败，请联系管理员");
        }
        try {
            String s = userHistoryService.insertHistory(telPhone, productName);
            log.info("查询历史记录+[{}]",s);
            ajaxResult.put("history", s);
        }catch (Exception e){
            log.error("错误异常==========================,{}",e.getMessage());
        }
        return ajaxResult;
    }
}
