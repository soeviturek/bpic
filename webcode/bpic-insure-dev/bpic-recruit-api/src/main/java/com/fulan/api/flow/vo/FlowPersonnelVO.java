/**
 * Project Name:FWD-api-flow
 * File Name:FlowPersonnelVO.java
 * Package Name:com.fulan.api.flow.vo
 * Date:2018年1月25日下午4:40:45
 * Copyright (c) 上海复深蓝软件股份有限公司.
 *
*/

package com.fulan.api.flow.vo;

import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ClassName:FlowPersonnelVO
 * Reason:	 TODO ADD REASON
 * Date:     2018年1月25日 下午4:40:45 
 * @author   chen.zhuang
 * @version  
 * @since    JDK 1.8 
 */
@Data
@Api(tags = "FlowPersonnelVO", description = "面试个人得分数据")
public class FlowPersonnelVO {

	@ApiModelProperty(value = "试卷题目id", name = "id")
	private String id;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "审核状态", name = "processingStatus")
	private Long processingStatus;

	@ApiModelProperty(value = "面试描述", name = "processingDesc")
	private String processingDesc;
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "人才ID", name = "personnelId")
	private Long personnelId;

	
	@ApiModelProperty(value = "得分", name = "score")
	private String  score;
	
	
	
	@ApiModelProperty(value = "考题名称", name = "paperItemName")
	private String  paperItemName;
	
	@ApiModelProperty(value = "考题说明", name = "paperItemDesc")
	private String  paperItemDesc;
}

