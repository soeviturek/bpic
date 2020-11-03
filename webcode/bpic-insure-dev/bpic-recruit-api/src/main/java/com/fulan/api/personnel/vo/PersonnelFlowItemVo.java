package com.fulan.api.personnel.vo;

import java.util.List;

import com.fulan.api.personnel.domain.Educational;
import com.fulan.api.personnel.domain.FamilyMember;
import com.fulan.api.personnel.domain.FlowItem;
import com.fulan.api.personnel.domain.InterviewAction;
import com.fulan.api.personnel.domain.WorkExperience;

import lombok.Data;

@Data
public class PersonnelFlowItemVo {
	private String id;
	private String name;
	private String sex;
	private String channel; //来源渠道
	private String age; //年龄  根据出生日期 再sql中转化
	private String cellphone;
	private String identityType;
	private String identityCode;
	private String maxEducation;
	private String School; //取最高学历所在学校
	private String refereeName; //推荐人 er_apply.referee_name
	private String referrerName;//引荐人   暂未发现 在哪里
	private String checkResult;//是否复核通过
	private String item;//当前环节
	private String personnelStatus;//当前状态
	private String itemName;//当前名称
	private String wechat;//微信
	private String familyCity;//所在城市
	private String workIntroduction;//工作经历简介
	private InterviewAction interviewAction;//面试情况
	private List<FlowItem> flowItem;
	private List<FlowItemActionPersonnerVo> flowItemPersonner;//面试执行情况
	private String protocolPosition;//拟定职级
	private String confirmPosition;//核定职级
	private String createUser;//创建人
	private String email;//邮箱
	private String  isDisability;//是否您身体有任何残疾
	private String  isBreakingTheLaw;//您是否曾被依法逮捕，触犯法律,或目前正处于官司诉讼中
	private String  isBrokenPromises;//您是否因严重失信行为被国家有关单位确定为失信联合惩戒对象，或者最近5年内具有其他严重失信不良记录
	private String  relevantIndustryCertificate;//保险代理人证书
	private String  insuranceBrokerCertificate;//保险经纪人证书
	private String  certificateOfInsurance;//保险公估人证书
	private String  rfcImg;//RFC
	private String  oatehrFicate;//其他证书
	private String  chinaCiticBank;//银行卡号
	private String  bankBranch;//银行卡支行
	List<FamilyMember>  familyMember;//紧急联系人
	List<WorkExperience>  workExperience;//工作经历
	private String  clientNum;//累计签寿险单的客户量（人）
	private String  maxManageNum;//最多同时辅导/管理人力（人）
	private String  nativeWorkTime;//本地工作时长
	private String  informDetail;//告知详情
	private String totalWorkTime;//工作总年限
	List<Educational>  educational;//学历
	
	
}
