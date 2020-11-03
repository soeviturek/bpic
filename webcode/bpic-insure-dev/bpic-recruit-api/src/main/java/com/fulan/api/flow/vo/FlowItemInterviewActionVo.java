package com.fulan.api.flow.vo;

import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FlowItemInterviewActionVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "", name = "id")
	private Long id;
	
	@ApiModelProperty(value = "流程名", name = "flowItemName")
	private String flowItemName;

	@ApiModelProperty(value = "流程描述", name = "flowItemNameDesc")
	private String flowItemNameDesc;

	@ApiModelProperty(value = "系统名", name = "systemName")
	private String systemName;

	@ApiModelProperty(value = "业务名", name = "businessName")
	private String businessName;

	@ApiModelProperty(value = "模块名", name = "moudleName")
	private String moudleName;

	@ApiModelProperty(value = "模块页面url", name = "moudleUrl")
	private String moudleUrl;

	@ApiModelProperty(value = "", name = "createTime")
	private Date createTime;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "", name = "createUser")
	private Long createUser;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value = "", name = "updateTime")
	private Date updateTime;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "", name = "updateUser")
	private Long updateUser;
	
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

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value = "开始时间", name = "startTime")
	private Date startTime;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value = "结束时间", name = "endTime")
	private Date endTime;

	@ApiModelProperty(value = "面试官", name = "processingPerson")
    private String processingPerson;
	
	@ApiModelProperty(value = "面试官名字", name = "processingName")
    private String processingName;  

	@ApiModelProperty(value = "面试结果(1,通过，2不通过，3，更改计划)", name = "processingStatus")
	private String processingStatus;

	@ApiModelProperty(value = "面试描述", name = "processingDesc")
	private String processingDesc;

}
