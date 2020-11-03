package com.fulan.api.flow.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 面试流程
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
@Api(value = "Flow", description = "面试流程")
@TableName("er_flow")
public class Flow implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "", name = "id")
	private Long id;

	@ApiModelProperty(value = "流程说明", name = "flowDesc")
	@TableField("flow_desc")
	private String flowDesc;

	@ApiModelProperty(value = "流程名称", name = "flowName")
	@TableField("flow_name")
	private String flowName;

	@ApiModelProperty(value = "所属分公司", name = "orgId")
	@TableField("org_id")
	private String orgId;
	
	@ApiModelProperty(value = "是否是总公司",name="headFlag")
	@TableField("head_flag")
	private String headFlag;

	@ApiModelProperty(value = "创建时间", name = "createTime")
	@TableField("create_time")
	private Date createTime;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "创建人", name = "createUser")
	@TableField("create_user")
	private Long createUser;

	@ApiModelProperty(value = "更新时间", name = "updateTime")
	@TableField("update_time")
	private Date updateTime;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "更新人", name = "updateUser")
	@TableField("update_user")
	private Long updateUser;
	
	@ApiModelProperty(value = "操作人姓名", name = "updateUserName")
	@TableField("update_user_name")
	private String updateUserName;

}
