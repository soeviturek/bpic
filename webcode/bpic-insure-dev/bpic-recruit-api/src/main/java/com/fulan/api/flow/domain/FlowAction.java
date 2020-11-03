package com.fulan.api.flow.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 流程执行
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
@Api(tags = "FlowAction", description = "流程执行")
@TableName("er_flow_action")

public class FlowAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "", name = "id")
    private Long id;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "组织id", name = "orgId")
    @TableField("org_id")
    private Long orgId;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "流程id", name = "flowId")
    @TableField("flow_id")
    private Long flowId;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "流程模块id", name = "flowItemId")
    @TableField("flow_item_id")
    private Long flowItemId;

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

}
