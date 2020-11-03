package com.fulan.api.personnel.domain;

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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 工作经历
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
@Api(tags = "WorkExperience", description = "工作经历")
@TableName("er_work_experience")

public class WorkExperience implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "", name = "id")
    private Long id;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "人才id", name = "personnelId")
    @TableField("personnel_id")
    private Long personnelId;

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

    @ApiModelProperty(value = "工作年限", name = "workTime")
    @TableField("work_time")
    private BigDecimal workTime;

    @ApiModelProperty(value = "职业", name = "occupation")
    private String occupation;

    @ApiModelProperty(value = "职位", name = "post")
    @TableField("post")
    private String post;

    @ApiModelProperty(value = "公司", name = "company")
    @TableField("company")
    private String company;

    @ApiModelProperty(value = "初审是否通过", name = "trialResult")
    private String trialResult;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "年收入", name = "annualIncome")
    @TableField("annual_income")
    private Long annualIncome;

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


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "", name = "updateTime")
    @TableField("update_time")
    private Date updateTime;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "", name = "updateUser")
    @TableField("update_user")
    private Long updateUser;

    @ApiModelProperty(value = "工作经历简介", name = "workIntroduction")
    @TableField("work_introduction")
    private String workIntroduction;

    //——3/27
    @ApiModelProperty(value = "原公司部门",name="originalCompanyDepartment")
    @TableField("original_company_department")
    private String originalCompanyDepartment;


    @ApiModelProperty(value = "是否保险同行",name="isInsuranceWork")
    @TableField("is_insurance_work")
    private String isInsuranceWork;


    //——3/27

}
