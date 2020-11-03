package com.fulan.api.personnel.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fulan.api.personnel.domain.Educational;
import com.fulan.api.personnel.domain.WorkExperience;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 招募人才简要信息
 * @author Administrator
 *
 */
@Data
public class AgentPersonnelInfo {
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "",name="id")
	private long id;//姓名Id
	
	private String name;//姓名
	private String sex;//性别
	private String age; //年龄  根据出生日期 再sql中转化
	private String contactQq;	//qq
	private String wechat;		//微信
	private String cellphone;//电话
	private String email;	//电子邮箱
	private String workIntroduction;	//工作经历
	
	private String createUser;		//推荐人
	private String createUserName;	//推荐人姓名
	
	private String protocolPosition;	//拟定职级
	private String confirmPosition;	//核定职级
	private String position;	//拟定职级
	private String flowId;		//流程节点Id
	private String flow;		//流程中文描述
	private String status;		//招募状态
	
	private String personnelStatus;		
	
	private String checkResult;		
	
	private String workExperience ;		//同业经历

	//工作总年限
	private String totalWorkTime;

	//保险工作年限
	private String nativeWorkTime;

	//累计签寿险单的客户量
	private Integer totalPortfolio;

	//最多同时辅导人数
	private Integer maxCoacher;

//	是否已关闭
	private Integer isClose;

	//工作经历(4/11)
	private List<WorkExperience> workExperienceList;

	//同业经历(4/11)
	private List<WorkExperience> InsuranceWorkExperience;

	//学历
	private List<Educational> educationalList;

}
