package com.fulan.api.personnel.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
/**
 * 人才个人信息-包含最高学历
 * 
 *
 */
@Data
public class PersonnelEducation {
	
	private String name;//姓名
	private String sex;//性别
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date birthday; //生日
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date startTime;//开始时间
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date endTime;//结束时间
	private String school;//学校
	private String nation;//民族
	private String education; //学历
	private String country;//国籍
		
}
