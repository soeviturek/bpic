package com.fulan.api.flow.domain;

import java.io.Serializable;

import java.util.Date;

import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import lombok.Data;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 面试执行情况
 * </p>
 *
 * @author chenzhuang123
 * @since 2018-01-24
 */
@Data
@Api(tags = "InterviewAction", description = "面试执行情况")
@TableName("er_interview_action")

public class InterviewAction implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "主键", name = "id")
	private Long id;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "人才id", name = "personnelId")
	private Long personnelId;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "流程模块id", name = "flowActionId")
    private Long flowActionId;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "需要处理的模块", name = "flowItemId")
	private Long flowItemId;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value = "开始时间", name = "startTime")
	@TableField("start_time")
	private Date startTime;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value = "结束时间", name = "endTime")
	private Date endTime;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "面试官", name = "processingPerson")
    private Long processingPerson;
	
	@ApiModelProperty(value = "面试官名字", name = "processingName")
	@TableField("processing_name")
    private String processingName;  

	@ApiModelProperty(value = "面试结果(1,通过，2不通过，3，更改计划)", name = "processingStatus")
	private String processingStatus;

	@ApiModelProperty(value = "面试描述", name = "processingDesc")
	private String processingDesc;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value = "创建时间", name = "createTime")
	private Date createTime;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "创建人", name = "createUser")
	private Long createUser;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "跟新人", name = "updateUser")
    private Long updateUser;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value = "跟新时间", name = "updateTime")
	private Date updateTime;

}
