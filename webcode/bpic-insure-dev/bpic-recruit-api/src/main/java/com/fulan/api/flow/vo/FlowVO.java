package com.fulan.api.flow.vo;

import java.io.Serializable;

import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 面试流程基础项
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
@Api(tags = "FlowVO", description = "面试流程对应审核")
public class FlowVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "流程名称", name = "flowName")
	private String flowName;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "审核状态", name = "processingStatus")
	private Long processingStatus;
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "审核人", name = "processingPerson")
	private Long processingPerson;
	
	
	@ApiModelProperty(value = "面试描述", name = "processingDesc")
	private String processingDesc;
	
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "人才ID", name = "personnelId")
	private Long personnelId;

	
	@ApiModelProperty(value = "是否执行此节点", name = "flowNode")
	private String  flowNode;
	
	
	
	@ApiModelProperty(value = "区别查询试卷类型 1，还是个人信息2", name = "moudleName")
	private String  moudleName;
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "流程执行ID", name = "flowActionId")
	private Long flowActionId;
	
	
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "基本项ID", name = "flowItemId")
	private Long flowItemId;
	
	@ApiModelProperty(value = "组织Id", name = "orgId")
	private String orgId;
	
	@ApiModelProperty(value = "代理人ID", name = "accountId")
	private String accountId;
	
	
}
