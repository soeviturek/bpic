package com.bpic.system.domain.vo;

import lombok.Data;

@Data
public class SalesVo {
    //员工编号
    private String sales;
    //员工姓名
    private String cEmpCnm;
    //当前机构
    private String deptName;
    //团队
    private String cSalegrpCde;
    //配置编码
    private String configCode;
}
