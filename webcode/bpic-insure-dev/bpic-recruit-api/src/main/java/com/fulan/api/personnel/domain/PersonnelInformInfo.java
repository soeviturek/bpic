package com.fulan.api.personnel.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 计划申请
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
@Api(tags = "PersonnleInformInfo", description = "")
@TableName("er_personnel_inform_info")

public class PersonnelInformInfo implements Serializable {

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

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "人才id", name = "personnel_inform_id")
    @TableField("personnel_inform_id")
    private Long personnelInformId;

    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(value = "告知id", name = "informId")
    @TableField("inform_id")
    private Long informId;


    @ApiModelProperty(value = "告知详情", name = "informDetail")
    @TableField("inform_detail")
    private String informDetail;


    @ApiModelProperty(value = "告知类型", name = "informType")
    @TableField("inform_type")
    private String informType;

    @ApiModelProperty(value = "告知详情", name = "告知选择 A:是  B：否")
    @TableField("inform_option")
    private String informOption;


}
