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
import java.util.Date;

/**
 * <p>
 * 家庭成员
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
@Api(tags = "FamilyMember", description = "家庭成员")
@TableName("er_family_member")

public class FamilyMember implements Serializable {

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

    @ApiModelProperty(value = "姓名", name = "name")
    private String name;

    @ApiModelProperty(value = "关系", name = "relationship")
    private String relationship;

    @ApiModelProperty(value = "代理人id", name = "agentCode")
    @TableField("agent_code")
    private String agentCode;

    @ApiModelProperty(value = "电话", name = "telephone")
    private String telephone;

    @ApiModelProperty(value = "初审是否通过", name = "trialResult")
    private String trialResult;

    @ApiModelProperty(value = "所属分公司", name = "company")
    private String company;

    @ApiModelProperty(value = "渠道", name = "channel")
    private String channel;

    @ApiModelProperty(value = "职务", name = "post")
    private String post;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "入职日期", name = "entryDate")
    @TableField("entry_date")
    private String entryDate;

    @ApiModelProperty(value = "顺序", name = "seq")
    private Integer seq;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    @TableField("create_time")
    private Date createTime;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "创建人", name = "createUser")
    @TableField("create_user")
    private Long createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间", name = "updateTime")
    @TableField("update_time")
    private Date updateTime;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "更新人", name = "updateUser")
    @TableField("update_user")
    private Long updateUser;


    @ApiModelProperty(value = "人员类型", name = "memberType")
    @TableField("member_type")
    private String memberType;


}
