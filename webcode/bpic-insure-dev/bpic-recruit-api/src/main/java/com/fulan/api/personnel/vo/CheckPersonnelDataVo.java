package com.fulan.api.personnel.vo;

import lombok.Data;
/**
 * 后台人才管理列表  vo类
 * @author kang
 *
 */
@Data
public class CheckPersonnelDataVo{
	private String id;
	private String name;
	private String onceName;//曾用名
	private String sex;
	private String age; //年龄  根据出生日期 再sql中转化
	private String nation; //民族
	private String cellphone;
	private String identityType;//类型
	private String identityCode;//证件编码
	private String identityCodeDate;//证件有效期
	private String postcode;//邮编
	private String email;//邮箱
	private String familyAddrDetail;//详细地址
	private String contactAddress;//通讯地址
	private String frontIdentification;//身份证正面照
	private String backIdentification;//身份证背面照
	private String studyImg;//学历证书
	private String debitCard;//银行卡
	private String mortImg;//MDRT荣誉证书
	private String protocolPosition;//拟定职级
	private String confirmPosition;//核定职级
	private String workIntroduction;//同业经历
	private String cheakOption;//历史审批评语
}
