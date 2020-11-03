package com.fulan.api.personnel.vo;

import java.util.ArrayList;
import java.util.Date;

import lombok.Data;
/**
 * 过滤条件
 * @author Administrator
 *
 */
@Data
public class FilterVo {
	int pageNo = 1;
	int pageSize = 10;
	
	private String accountId;		//当前用户ID
	private String keyName;		//
	
	private String name;					//姓名
	
	private String phone;					//手机号
	private ArrayList<String> status;		//状态
	private ArrayList<String> position;	//职级条件

	private Date startTime;	//开始时间
	private Date endTime;		//结束时间

}
