package com.bpic.system.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class AuthenticationVo {

    /**
     * 下线姓名
     */
    private String offlineName;

    /**
     * 业务员
     */
    private String salesman;


    /**
     * 提交日期
     */
    private String commitDate;

    /**
     * 审核状态
     */
    private String status;

    /**
     * 所属机构
     */
    private String deptId;

    /**
     * 手机号
     */
    private String cTelMob;

    /**
     * 标识 1：通过 2：不通过
     */
    private String flage;

    /**
     * 所属团队
     */
    private String team;
    /**
     * 审核意见
     */
    private String advice;

    private String deptName;

    private List<String> deptList;
}
