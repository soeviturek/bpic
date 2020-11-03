package com.fulan.api.personnel.vo;

import lombok.Data;
/**
 * 过滤条件
 * @author Administrator
 *
 */
@Data
public class EnterVo {
	private Long tagId;	//随机Id
	
	private String	currentId;	//当前更新人Id
	private String personnelId;		//人才ID
	
	private String result; 		//审核结果
	private String comment; 	//评语内容
	private String protocolPosition;	//拟定职级
	private String confirmPosition;		//核定职级

	private int currentStatus; 	//当前流程状态
	private int nextStatus; 	//下一个流程状态
	
	private String nextProPerson; 	//下一流程负责人Id
	private String nextProName;		//下一流程负责人姓名
	
}
