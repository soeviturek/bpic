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
 * 面试流程基础项
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
@Api(tags = "FlowItem", description = "面试流程基础项")
@TableName("er_flow_item")

public class FlowItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "", name = "id")
    private Long id;

    @ApiModelProperty(value = "流程名", name = "flowItemName")
    @TableField("flow_item_name")
    private String flowItemName;

    @ApiModelProperty(value = "流程描述", name = "flowItemNameDesc")
    @TableField("flow_item_name_desc")
    private String flowItemNameDesc;

    @ApiModelProperty(value = "系统名", name = "systemName")
    @TableField("system_name")
    private String systemName;

    @ApiModelProperty(value = "业务名", name = "businessName")
    @TableField("business_name")
    private String businessName;

    @ApiModelProperty(value = "模块名", name = "moudleName")
    @TableField("moudle_name")
    private String moudleName;

    @ApiModelProperty(value = "模块页面url", name = "moudleUrl")
    @TableField("moudle_url")
    private String moudleUrl;

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
