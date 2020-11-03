package com.fulan.api.flow.vo;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import lombok.Data;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 流程执行
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
public class FlowActionVo implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1320746329875127423L;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private Long id;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private Long flowId;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private Long flowItemId;
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private Long orgId;

	@ApiModelProperty(value = "步骤", name = "step")
	private Integer step;
	
	@ApiModelProperty(value = "是否核查", name = "isCheck")
	private Integer isCheck;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "负责人", name = "processingRole")
	@TableField("processing_role")
	private Long processingRole;

	@ApiModelProperty(value = "条件", name = "condition")
	private String condition;

	@ApiModelProperty(value = "", name = "createTime")
	@TableField("create_time")
	private Date createTime;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "", name = "createUser")
	@TableField("create_user")
	private Long createUser;

	@ApiModelProperty(value = "", name = "updateTime")
	@TableField("update_time")
	private Date updateTime;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "", name = "updateUser")
	@TableField("update_user")
	private Long updateUser;
	
	private String flowItemName;
	
}
