package com.fulan.api.flow.vo;

import lombok.Data;
/**
 * 我的增员  vo类
 * @author chenzhuang
 *
 */
@Data
public class PersonnelAddVo{
	private String id;
	private String name;
	private String sex;//性别
	private String age; //年龄  根据出生日期 再sql中转化
	private String cellphone;
	private String talentPlan; //人才计划
	private String processProgress; //流程进度 step
	private String currentprogress; //当前环节，从属什么流程
	private String personnelStatus;// 当前环节  具体有什么状态后期确定
	private String countProgress;//总进度
	private String currentLink; //试卷类型
	
	
	
}
