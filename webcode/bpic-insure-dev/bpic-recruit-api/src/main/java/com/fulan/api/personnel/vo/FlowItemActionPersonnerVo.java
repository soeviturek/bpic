/**
 * 
 */
package com.fulan.api.personnel.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @description: 
 * @author: shenzhongwu
 * @date:2018年4月26日
 */
@Data
public class FlowItemActionPersonnerVo {
	
	private String flowItemName;//面试节点名称
	private String moudleName;//面试节点
	private String processingStatus;//面试结果
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date startTime;//开始时间
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date endTime;//结束时间
	
	

}
