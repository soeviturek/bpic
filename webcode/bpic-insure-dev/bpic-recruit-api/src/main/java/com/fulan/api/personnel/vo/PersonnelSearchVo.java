package com.fulan.api.personnel.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 陈磊
 * @date: 2019/9/16
 * Time:           19:27
 * Version:        1.0.0
 * Description:    PersonnelSearchVo类用于——列表页搜索条件
 */
@Data
public class PersonnelSearchVo {

//  标签的刷选
    private String personnelStatus;

//  标签的刷选(未签约)
    private String personnelStatus1;

//  关键字搜索
    private String keyWord;

//  推荐人搜素
    private String createUser;

//  分公司刷选--
    private String companyId;

//  核定职级刷选（高级业务经理...）
    private String confirmPosition;

//  拟定职级刷选（高级业务经理...）
    private String protocolPosition;

//  进度的刷选
    private String personnelStatus2;

//  申请时间开始的刷选
    private Date createTimeStart;

//  申请时间结束的刷选
    private Date createTimeEnd;

//  推荐人所在事业部的刷选
    private String companyId1;

//  核查状态（check_result: 0 —>核查不通过 ， 1—>核查通过 ， 2 —> 待重新提交资料 ，3 —> 未核查）
    private String checkResult;

//  最高学位（硕士...）
    private String education;

//  关闭的刷选
    private int isClose;


//  有权限的公司
    private List<String> subOrgId;

    int pageSize;

    int pageNo;



}
