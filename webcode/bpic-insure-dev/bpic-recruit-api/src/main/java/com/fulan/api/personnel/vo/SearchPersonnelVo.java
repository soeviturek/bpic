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
public class SearchPersonnelVo {
	
	private String keyWord;
	
	private String channel;//来源聚道
	
	private int searchType;//1:人才库列表2:增员人才列表3:复核列表
	
	private String orgId;
	
	private String companyId;
	
	private String personnelStatus; //人才状态 1:初审 2：复审 3：培训 4：签约审批 5：签约 6：资料审核
	
	private String protocolPosition;//拟定职级
	
	private String confirmPosition;//核定职级
	
	private String rank;//职级
	
	private String nowPlan;//环节
	
	private String personnelSign;//是否签约
	
	private String orgName;//分公司名称
	
	private String startDate;//开始时间
	
	private String endDate;//结束时间
	
	private String signStartDate;//签约开始时间
	
	private String signEndDate;//签约结束时间
	
	private int pageNo=1;
	
	private int pageSize=10;

}
