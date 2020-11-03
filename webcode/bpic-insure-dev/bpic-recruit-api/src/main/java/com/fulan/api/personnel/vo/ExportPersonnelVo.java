package com.fulan.api.personnel.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 导出Vo
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
public class ExportPersonnelVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;//姓名
    private String sex;//性别
    private String onceName;//曾用名
    private String weChat;//微信号
    private String cellphone;//手机号
    private String telephone;//电话
    private String identityType;//证件类型
    private String identityCode;//证件编码
    private String postcode;//邮编
    private String email;//电子邮箱
    private String domicilePlace;//户籍地址
    private String contactAddress;//通讯地址
    private String birthYear;//出生年月
    private String nation;//民族
    private String culture;//文化程度
    private String politicsStatus;//政治面貌
    private String chinaCiticBank;//中信银行卡卡号
    private String bankBranch;//银行支行
    private String clientNum;//累计签寿险单的客户量（人）
    private String maxManageNum;//最多同时辅导/管理人力（人）
    private String emergencyContactOne;//紧急联系人1姓名
    private String relationOne;//关系
    private String relationPhoneOne;//紧急联系人电话
    private String emergencyContactTwo;//紧急联系人2姓名
    private String relationTwo;//关系2
    private String relationPhoneTwo;//紧急联系人电话2
    private String emergencyContactThree;//紧急联系人3姓名
    private String relationThree;//关系3
    private String relationPhoneThree;//紧急联系人电话3
    private String relevantIndustryCertificate;//保险代理人证书
    private String insuranceBrokerCertificate;//保险经纪人证书
    private String certificateOfInsurance;//保险公估人证书
    private String rfcImg;//RFC
    private String oatherImg;//其他
    private String startTime;//院校开始时间
    private String endTime;//院校结束时间
    private String school;//院校
    private String education;//学历
    private String specialty;//专业
    private String degree;//学位
    private String workStartTimeOne;//工作开始时间1
    private String workEndTimeOne;//工作结束时间1
    private String companyOne;//公司1
    private String departmentOne;//部门1
    private String positionOne;//职位1
    private String workStartTimeTwo;//工作开始时间2
    private String workEndTimeTwo;//工作结束时间2
    private String companyTwo;//公司2
    private String departmentTwo;//部门2
    private String positionTwo;//职位2
    private String workStartTimeThree;//工作开始时间3
    private String workEndTimeThree;//工作结束时间3
    private String companyThree;//公司3
    private String departmentThree;//部门3
    private String positionThree;//职位3
    private String isDisability;//是否您身体有任何残疾
    private String isBreakingTheLaw;//您是否曾被依法逮捕，触犯法律,或目前正处于官司诉讼中
    private String isBrokenPromises;//您是否因严重失信行为被国家有关单位确定为失信联合惩戒对象，或者最近5年内具有其他严重失信不良记录
    private String signatureImg;//签名图片
    private String bareheadedPhoto;//免冠照
    private String frontIdentification;//身份证正面照
    private String backIdentification;//身份证背面照
    private String studyImg;//学历证书
    private String debitCard;//银行卡
    private String mortImg;//MDRT荣誉证书
    private String illegalFundRaisingInsurance;//保险从业人员自觉抵制非法集资活动承诺书
    private String mortmoralGuidanceImg;//保险经纪从业人员执业道德指引
    private String personnelCommitment;//人身保险销售从业人员诚信展业承诺书
    private String middlemanDeal;//"经纪人服务协议	"
    private String firstReading;//"初审面试人	"
    private String reexamination;//"复审面试人	"
    private String nowItem;//"当前节点"
    private String employeeNum;//员工编号
    private String contractNum;//合同编号
    private String syb;//所属营业部
    private String businessUnit;//所属事业部
    private String tpersonnel;//推荐人
    private String bxTime;//保险从业年限
    private String creatUser;//创建人，用于查询推荐人
    private String protocolPosition;//拟定职级
	private String confirmPosition;//核定职级
    private String entryTime;//入职时间



}
