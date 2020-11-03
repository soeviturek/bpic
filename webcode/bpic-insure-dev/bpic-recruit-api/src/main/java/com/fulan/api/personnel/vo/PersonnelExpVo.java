package com.fulan.api.personnel.vo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonnelExpVo implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "",name="id")
	@TableField("id")
	private Long id;
	
	@ApiModelProperty(value = "人才状态",name="personnelStatus")
	private String personnelStatus;

	@ApiModelProperty(value = "姓名",name="name")
	private String name;
	
	@ApiModelProperty(value = "初审是否通过",name="trialResult")
	private String trialResult;
	
	@ApiModelProperty(value = "性别",name="sex")
	private String sex;

	@ApiModelProperty(value = "来源渠道",name="channel")
	private String channel;

	@ApiModelProperty(value = "手机号",name="cellphone")
	private String cellphone;

	@ApiModelProperty(value = "电子邮箱",name="email")
	private String email;

	@ApiModelProperty(value = "原保险机构名称",name="originalCompany")
	private String originalCompany;
	
	@ApiModelProperty(value = "微信",name="wechat")
	@TableField("wechat")
	private String wechat;
	
	@ApiModelProperty(value = "QQ",name="contactQQ")
	@TableField("contact_qq")
	private String contactQQ;
	
	@ApiModelProperty(value = "拟定职级",name="protocolPosition")
	@TableField("protocol_position")
	private String protocolPosition;
	
	@ApiModelProperty(value = "所在城市",name="city")
	@TableField("family_city")
	private String familyCity;

    @ApiModelProperty(value = "职位",name="post")
    @TableField("post")
	private String post;

    @ApiModelProperty(value = "公司",name="company")
    @TableField("company")
	private String company;
   

    @ApiModelProperty(value = "工作经历简介",name="workIntroduction")
    @TableField("work_introduction")
	private String workIntroduction;
}
