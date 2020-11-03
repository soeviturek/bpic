package com.bpic.mobile.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SalesmanPerformanceVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String policyMark;//结算标志
    private Date dealTime;//成交日期
    private Date transactionTime;//交易日期
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal policyAmount;//保单金额
}
