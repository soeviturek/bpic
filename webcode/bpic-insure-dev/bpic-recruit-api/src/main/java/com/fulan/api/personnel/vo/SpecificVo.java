/**
 * 
 */
package com.fulan.api.personnel.vo;

import lombok.Data;

/**
 * @description: 
 * @author: shenzhongwu
 * @date:2018年4月26日
 */
@Data
public class SpecificVo {
	
	private String presentCode;//员工编号
	
	private String name;//姓名
	
	private String sex;//性别
	
	private String certificateCode;//证件号
	
	private String dutyName; //职位
	
	private String branch;//分公司
	
	private String phone;//联系方式
	
	private String source;//面试官Code
	private String orgId;//创建该增员人员分公司id
	
}
