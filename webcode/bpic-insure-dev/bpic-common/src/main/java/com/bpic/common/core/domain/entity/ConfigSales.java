package com.bpic.common.core.domain.entity;

import lombok.Data;

@Data
public class ConfigSales {
    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    private Integer id;
    /**
     *产品代码
     */
    private long	configCode;

    /**
     *祖籍公司
     */
    private String	deptName;

    /**
     *员工编码
     */
    private String	sales;

    /**
     * 公司编码
     */
    private String deptCode;
}
