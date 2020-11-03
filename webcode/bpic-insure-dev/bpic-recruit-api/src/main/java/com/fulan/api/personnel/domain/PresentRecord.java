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

@Data
@Api(tags = "PresentRecord", description = "")
@TableName("er_present_record")
public class PresentRecord  implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "",name="id")
	@TableField("id")
	private Long id;
	
	@ApiModelProperty(value = "preCode",name="preCode")
	@TableField("pre_code")
	private String preCode;
	
	@ApiModelProperty(value = "subCode",name="subCode")
	@TableField("sub_code")
	private String subCode;
	
	@ApiModelProperty(value = "fullCode",name="fullCode")
	@TableField("full_code")
	private String fullCode;
	
	@ApiModelProperty(value = "type",name="type")
	@TableField("type")
	private String type;
	
	@ApiModelProperty(value = "createUser",name="createUser")
	@TableField("create_user")
	private String createUser;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "",name="createTime")
	@TableField("create_time")
	private Date createTime;
	
}
