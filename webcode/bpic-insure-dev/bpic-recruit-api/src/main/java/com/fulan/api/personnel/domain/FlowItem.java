package com.fulan.api.personnel.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 基础面试流程
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
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
    private String businessName;

    @ApiModelProperty(value = "模块名", name = "moudleName")
    private String moudleName;

    @ApiModelProperty(value = "模块页面", name = "moudleUrl")
    private String moudleUrl;


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


}
