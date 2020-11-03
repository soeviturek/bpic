package com.bpic.common.core.domain.entity;

import lombok.Data;

@Data
public class Product {

    private static final long serialVersionUID = 1L;
    /**
     *产品代码
     */
    private String	productCode;
    /**
     *渠道代码
     */
    private String 	channelCode;
    /**
     *费用比例-手续费
     */
    private float	poundage;
    /**
     *费用比例-绩效
     */
    private float	performance;
    /**
     *费用比例-展业费
     */
    private float	acquisitionFee;
    /**
     *费用比例-激励费用
     */
    private float	incentiveFee;
    /**
     *配置权限代码
     */
    private long	configCode;
    /**
     * 产品名称
     */
    private String productCName;
    /**
     * 产品类型
     */
    private String groupId;
}
