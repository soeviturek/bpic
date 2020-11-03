package com.fulan.api.personnel.vo;

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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 面试执行情况
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
@Api(tags = "InterviewAction", description = "面试执行情况")
@TableName("er_interview_action")

public class InterviewActionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "", name = "id")
    private Long id;

    @ApiModelProperty(value = "人才id", name = "personnelId")
    @TableField("personnel_id")
    private String personnelId;


    @ApiModelProperty(value = "需要处理的模块", name = "flowItemId")
    @TableField("flow_item_id")
    private String flowItemId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间", name = "startTime")
    @TableField("start_time")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间", name = "endTime")
    @TableField("end_time")
    private Date endTime;

    @ApiModelProperty(value = "面试官", name = "processingPerson")
    @TableField("processing_person")
    private String processingPerson;

    @ApiModelProperty(value = "面试名字", name = "processingName")
    @TableField("processing_name")
    private String processingName;

    @ApiModelProperty(value = "面试结果(1,通过，2不通过，3，更改计划)", name = "processingStatus")
    @TableField("processing_status")
    private String processingStatus;

    @ApiModelProperty(value = "面试描述", name = "processingDesc")
    @TableField("processing_desc")
    private String processingDesc;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "", name = "createTime")
    @TableField("create_time")
    private Date createTime;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "", name = "createUser")
    @TableField("create_user")
    private Long createUser;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "", name = "updateUser")
    @TableField("update_user")
    private Long updateUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "", name = "updateTime")
    @TableField("update_time")
    private Date updateTime;


    private String nowItem;//当前所在环节

    private String send;//是否发送过

    private String phone;//手机号
    private String source;
    private String orgId;//创建该增员人员分公司id
    private String orgName;//创建该增员人员分公司名称
}
