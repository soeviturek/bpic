package com.fulan.api.personnel.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 计划申请
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Api(tags = "PersonnleInform", description = "")
@TableName("er_personnel_inform")
@Data
public class PersonnelInform implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "",name="id")
	private Long id;
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "人才id",name="personnelId")
	@TableField("personnel_id")
	private Long personnelId;
	
	@ApiModelProperty(value = "告知详情",name="informDetail")
	@TableField("inform_detail")
	private String informDetail;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "创建时间",name="createTime")
	@TableField("create_time")
	private Date createTime;
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "创建人",name="createUser")
	@TableField("create_user")
	private Long createUser;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "更新时间",name="updateTime")
	@TableField("update_time")
	private Date updateTime;
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "更新人",name="updateUser")
	@TableField("update_user")
	private Long updateUser;
	


}
