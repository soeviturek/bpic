package com.bpic.product;


import lombok.Data;

/**
 * 渤海销管系统-产品清单返回产品详情对象
 * by zyy
 * */

@Data
public class ChannelServerProduct {

    private String allProductSerial;//全部产品排序
    private String productPath;//产品投保地址
    private String productCName;//产品名称
    private String lightSpot;//产品最低保费
    private String productRecommendSerial;//产品热销排序
    private String productGroupSerial;//产品分组排序
    private String photoAddress;//产品首页展示图地址
    private String productBannerSerial;//产品banner图排序
    private String productCode;//产品代码
    private String productGroupCode;//产品分组
    private String productBanner;//是否为banner图
    private String describe;//产品信息描述
    private String productRecommend;//是否为热销
    private String channelId;//渠道代码
    private String productType;//产品性质
    private String productImgPath;//产品宣传图地址
    private String groupName;//类型名称
    private String groupId;//类型ID

    public String getGroupName() {
        switch (this.productGroupCode){
            case "02":
                this.groupName = "健康";
                break;
            case "03":
                this.groupName = "意外";
                break;
            case "04":
                this.groupName = "家财";
                break;
            case "05":
                this.groupName = "责任";
                break;
            default:
                this.groupName = "全部";
                break;
        }
        return groupName;
    }

    public String getGroupId() {
        return groupId= this.productGroupCode;
    }
}
