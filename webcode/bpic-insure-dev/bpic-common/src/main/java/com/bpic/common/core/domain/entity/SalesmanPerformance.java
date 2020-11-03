package com.bpic.common.core.domain.entity;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 业务员清单信息对象
 * */


@Data
public class SalesmanPerformance implements Serializable {

    private String policyNo;//保单号
    private String insureNo;//投保单号
    private BigDecimal policyAmount;//保单金额
    private Date transactionTime;//交易日期
    private String productName;//产品名称
    private String productCode;//产品代码
    private String policyMark;//结算标志
    private Date dealTime;//成交日期
    private String salesmanNo;//业务员编号
    private String salesmanName;//业务员姓名
    private String salesmanPhone;//业务员手机号
    private Date createTime;//创建时间
    private String createFrom;//创建来源
    private Date updateTime;//更新时间
    private String updateFrom;//更新来源
    private String shareProductId; //分享产品链接ID
    private String offlineName;
    private String imgUrl;

}
