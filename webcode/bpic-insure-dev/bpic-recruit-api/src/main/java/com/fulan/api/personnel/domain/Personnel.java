package com.fulan.api.personnel.domain;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bpic.common.utils.json.LongJsonDeserializer;
import com.bpic.common.utils.json.LongJsonSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 人才信息主表
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
@Api(tags = "Personnel", description = "人才信息主表")
@TableName("er_personnel")

public class Personnel implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "",name="id")
	@TableField("id")
	private Long id;
	
	@ApiModelProperty(value = "DMS返回的agentcode",name="agentCode")
	@TableField("agent_code")
	private String agentCode;

	@ApiModelProperty(value = "人才类型",name="personnelType")
	@TableField("personnel_type")
	private String personnelType;

	@ApiModelProperty(value = "人才状态",name="personnelStatus")
	@TableField("personnel_status")
	private String personnelStatus;

	
	@ApiModelProperty(value = "证件类型",name="identityType")
	@TableField("identity_type")
	private String identityType;

	@ApiModelProperty(value = "证件编码",name="identityCode")
	@TableField("identity_code")
	private String identityCode;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "证件有效期",name="ctfexpireDate")
	@TableField("ctfexpire_date")
	private Date ctfexpireDate;

	@ApiModelProperty(value = "姓名",name="name")
	private String name;
	
	@ApiModelProperty(value = "初审是否通过",name="trialResult")
	@TableField("trial_result")
	private String trialResult;

	@ApiModelProperty(value = "年龄",name="age")
	private Integer age;
	
	@ApiModelProperty(value = "性别",name="sex")
	private String sex;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "出生日期",name="birthday")
	private Date birthday;

	@ApiModelProperty(value = "民族",name="nation")
	private String nation;

	@ApiModelProperty(value = "政治面貌",name="political")
	private String political;

	@ApiModelProperty(value = "新人来源",name="source")
	private String source;

	@ApiModelProperty(value = "来源渠道",name="channel")
	private String channel;

	@ApiModelProperty(value = "学历",name="education")
	private String education;

	@ApiModelProperty(value = "手机号",name="cellphone")
	private String cellphone;

	@ApiModelProperty(value = "电子邮箱",name="email")
	private String email;

	@ApiModelProperty(value = "手机是否认证",name="isPhoneValidate")
	@TableField("is_phone_validate")
	private String isPhoneValidate;

	@ApiModelProperty(value = "是否有保险公司经验（1，有，2，无）",name="isInsuranceCompany")
	@TableField("is_insurance_company")
	private Integer isInsuranceCompany;

	@ApiModelProperty(value = "原保险机构名称",name="originalCompany")
	@TableField("original_company")
	private String originalCompany;

	@ApiModelProperty(value = "是否离职(1:是，0，否)",name="isQuit")
	@TableField("is_quit")
	private Integer isQuit;
	
	@ApiModelProperty(value = "离职是否满六个月（1，有，2，无）",name="isOverSix")
	@TableField("is_over_six")
	private Integer isOverSix;


	@ApiModelProperty(value = "本地工作时长",name="nativeWorkTime")
	@TableField("native_work_time")
	private String nativeWorkTime;
    
	@ApiModelProperty(value = "是否复核通过",name="checkResult")
	@TableField("check_result")
	private String checkResult;
	
	@ApiModelProperty(value = "是否复核通过",name="isCheck")
	@TableField("is_check")
	private String isCheck;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "",name="createTime")
	@TableField("create_time")
	private Date createTime;

	
	@ApiModelProperty(value = "",name="createUser")
	@TableField("create_user")
	private String createUser;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "",name="updateTime")
	@TableField("update_time")
	private Date updateTime;

	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "",name="updateUser")
	@TableField("update_user")
	private Long updateUser;

	@ApiModelProperty(value = "国籍",name="country")
	@TableField("country")
	private String country;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@ApiModelProperty(value = "",name="departureDate")
	@TableField("departure_date")
	private Date departureDate;
	
	@ApiModelProperty(value = "核查意见",name="cheakOption")
	@TableField("cheak_option")
	private String cheakOption;
	
	@ApiModelProperty(value = "创建该增员人员分公司id",name="orgId")
	@TableField("org_id")
	private String orgId;
	
	@ApiModelProperty(value = "创建该增员人员分公司名称",name="orgName")
	@TableField("org_name")
	private String orgName;
	
	@ApiModelProperty(value = "人才所在分公司id",name="companyId")
	@TableField("company_id")
	private String companyId;
	
	
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@ApiModelProperty(value = "",name="accountId")
	@TableField("account_id")
	private Long accountId;
	
	@ApiModelProperty(value = "人才qq",name="contactQQ")
	@TableField("contact_qq")
	private String contactQQ;
	
	
	@ApiModelProperty(value = "人才微信",name="wechat")
	@TableField("wechat")
	private String wechat;
	
	
	@ApiModelProperty(value = "拟定职级",name="protocolPosition")
	@TableField("protocol_position")
	private String protocolPosition;
	
	
	@ApiModelProperty(value = "核定职级",name="confirmPosition")
	@TableField("confirm_position")
	private String confirmPosition;
	
	@ApiModelProperty(value = "工作经历简介",name="workIntroduction")
	@TableField("work_introduction")
	private String workIntroduction;
	
	@ApiModelProperty(value = "曾用名",name="onceName")
	@TableField("once_name")
	private String onceName;
	
	@ApiModelProperty(value = "通讯地址",name="contactAddress")
	@TableField("contact_address")
	private String contactAddress;
	
	@ApiModelProperty(value = "邮编",name="postcode")
	@TableField("postcode")
	private String postcode;
	
	@ApiModelProperty(value = "电话",name="telephone")
	@TableField("telephone")
	private String telephone;

	@ApiModelProperty(value = "家庭详细地址",name="familyAddrDetail")
	@TableField("family_addr_detail")
	private String familyAddrDetail;
	
	@ApiModelProperty(value = "家庭地址所在省",name="familyProvince")
	@TableField("family_province")
	private String familyProvince;
	
	@ApiModelProperty(value = "家庭地址所在城市",name="familyCity")
	@TableField("family_city")
	private String familyCity;
	
	@ApiModelProperty(value = "家庭地址所在区域",name="familyArea")
	@TableField("family_area")
	private String familyArea;
	
	@ApiModelProperty(value = "家庭地址所在区域",name="isMdrt")
	@TableField("is_mdrt")
	private String isMdrt;
	

	@ApiModelProperty(value = "是否发送",name="isSend")
	@TableField("is_send")
	private String isSend;
	
	@ApiModelProperty(value = "户口所在地",name="domicilePlace")
	@TableField("domicile_place")
	private String domicilePlace;
	
	@ApiModelProperty(value = "是否具有相关行业从业证书",name="jobCertificate")
	@TableField("job_certificate")
	private String jobCertificate;
	
	@ApiModelProperty(value = "累计签寿险单的客户量",name="totalPortfolio")
	@TableField("total_portfolio")
	private Integer totalPortfolio;
	
	@ApiModelProperty(value = "最多同时辅导人数",name="maxCoacher")
	@TableField("max_coacher")
	private Integer maxCoacher;
	
	@ApiModelProperty(value = "银行卡号",name="chinaCiticBank")
	@TableField("china_citic_bank")
	private String chinaCiticBank;
	
	@ApiModelProperty(value = "银行支行",name="bankBranch")
	@TableField("bank_branch")
	private String bankBranch;
	
	@ApiModelProperty(value = "银行名称",name="bankName")
	@TableField("bank_name")
	private String bankName;
	
	@ApiModelProperty(value = "证书",name="certificate1")
	@TableField("certificate1")
	private String certificate1;
	
	@ApiModelProperty(value = "证书",name="certificate2")
	@TableField("certificate2")
	private String certificate2;
	
	@ApiModelProperty(value = "证书",name="certificate3")
	@TableField("certificate3")
	private String certificate3;
	
	@ApiModelProperty(value = "证书",name="certificate4")
	@TableField("certificate4")
	private String certificate4;
	
	@ApiModelProperty(value = "证书",name="certificateOther")
	@TableField("certificate_other")
	private String certificateOther;


	/**
	 * ——2019/3/27
	 */
	@ApiModelProperty(value = "总工作年限",name="totalWorkTime")
	@TableField("total_work_time")
	private String totalWorkTime;

	//——3/27
	@ApiModelProperty(value = "推荐人姓名",name="createUserName")
	@TableField("create_user_name")
	private String createUserName;

	@ApiModelProperty(value = "是否后台关闭",name="isClose")
	@TableField("is_close")
	private Integer isClose;


}
