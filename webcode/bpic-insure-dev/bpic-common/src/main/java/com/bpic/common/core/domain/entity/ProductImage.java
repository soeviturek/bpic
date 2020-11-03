package com.bpic.common.core.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ProductImage {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 产品代码
     */
    private String productCode;

    /**
     * 渠道代码
     */
    private String channelCode;

    /**
     * 图片url
     */
    private String image;

    /**
     * 产品类型
     */
    private String productGroupCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 修改人
     */
    private String updater;
}
