package com.fulan.api.personnel.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
/**
 * 增员中人才库显示信息VO
 * @author Administrator
 *
 */
@Data
public class IncreaseingPersonnelPool {
	private String name;//姓名
	private String sex;//性别
	private String age; //年龄  根据出生日期 再sql中转化
	private String cellphone;//电话
	private String talentPlan; //人才计划
	private String education; //学历
	private String school;//毕业院校
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date createTime;//创建时间
	
	private String lastLink;//上一环节
	private String currentLink;//当前环节
}
