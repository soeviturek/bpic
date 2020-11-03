package com.fulan.api.personnel.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author 陈磊
 * @date: 2019/9/16
 * Time:           20:05
 * Version:        1.0.0
 * Description:    PersonnelResultVo类用于——列表页显示
 */
@Data
public class PersonnelResultVo {

    private String id;

    private String name;

    private String createUser;

    private String createUserOrgName;

    private String protocolPosition;

    private String confirmPosition;

    private String age;

    private String sex;

    private String cellphone;

    private String personnelStatus;

    private String personnelStatusName;

    private Date createTime;

    private String idNum;

    private String education;

    private String checkResult;

    private String isSend;

    private String agentCode;

//  分公司ID
    private String companyId;

//  分公司名称
    private String companyName;

//  推荐人姓名
    private String createUserName;

//  关闭的返显
    private Integer isClose;

}
