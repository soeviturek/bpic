package com.fulan.api.personnel.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
/**
 * 后台人才管理列表  vo类
 * @author kang
 *
 */
@Data
public class PersonnelManageVo{
	private String id;
	private String name;
	private String sex;
	private String channel; //来源渠道
	private String age; //年龄  根据出生日期 再sql中转化
	private String cellphone;
	private String identityCode;
	private String maxEducation;
	private String School; //取最高学历所在学校
	private String refereeName; //推荐人 er_apply.referee_name
	private String referrerName;//引荐人   暂未发现 在哪里
	private String checkResult;//是否复核通过
	private String protocolPosition;//拟定职级
	private String confirmPosition;//核定职级
	private String personnelStatus;//人才状态
	private String isSend;//是否发送过
	private String createUser;//创建者
    private String companyId;//所在营业部ID

	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date createTime;//创建时间
	
	private String orgName;//创建者
	
	
}
