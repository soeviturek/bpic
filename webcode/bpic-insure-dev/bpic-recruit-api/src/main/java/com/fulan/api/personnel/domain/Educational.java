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
 * 教育经历
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
@Api(tags = "Educational", description = "教育经历")
@TableName("er_educational")

public class Educational implements Serializable {

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
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "开始时间",name="startTime")
	@TableField("start_time")
	private Date startTime;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "结束时间",name="endTime")
	@TableField("end_time")
	private Date endTime;

	
	@ApiModelProperty(value = "学历",name="education")
	private String education;

	@ApiModelProperty(value = "学校",name="school")
	private String school;
	
	@ApiModelProperty(value = "专业",name="specialty")
	@TableField("specialty")
	private String specialty;
	
	@ApiModelProperty(value = "学位",name="degree")
	@TableField("degree")
	private String degree;
	
	
	@ApiModelProperty(value = "初审是否通过",name="trialResult")
	private String trialResult;

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
