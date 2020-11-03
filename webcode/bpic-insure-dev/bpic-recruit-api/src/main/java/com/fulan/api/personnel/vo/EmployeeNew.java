package com.fulan.api.personnel.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EmployeeNew {
	
	/**
	 * 
	 */

	@JsonProperty
	@ApiModelProperty(value = "员工id",name="EmployeeID")
	private String EmployeeID="";//员工id
	
	@JsonProperty
	@ApiModelProperty(value = "员工编号",name="PresentCode")
	private String PresentCode="";//员工编号

	@JsonProperty
	@ApiModelProperty(value = "分支机构id,多个分支使用','分隔",name="BranchID")
	private String BranchID="";//分支机构id,多个分支使用","分隔

	@JsonProperty
	@ApiModelProperty(value = "部门id",name="DepartmentID")
	private String DepartmentID="";//部门id

	@JsonProperty
	@ApiModelProperty(value = "员工姓名",name="EmployeeName")
	private String EmployeeName="";//员工姓名

	@JsonProperty
	@ApiModelProperty(value = "员工姓名",name="EmployeeNameEN")
	private String EmployeeNameEN="";//员工姓名


	@JsonProperty
	@ApiModelProperty(value = "部门id",name="IntroducerID")
	private String IntroducerID="";//部门id

	@JsonProperty
	@ApiModelProperty(value = "CertificateType",name="CertificateType")
	private byte CertificateType;//证件号码

	@JsonProperty
	@ApiModelProperty(value = "name",name="CertificateCode")
	private String CertificateCode="";//证件号码

	@JsonProperty
	@ApiModelProperty(value = "PhotoPath",name="PhotoPath")
	private String PhotoPath="";//证件号码

	@JsonProperty
	@ApiModelProperty(value = "Sex",name="Sex")
	private int Sex;//证件号码

	@JsonProperty
	@ApiModelProperty(value = "NationID",name="NationID")
	private int NationID=1;//证件号码

	@JsonProperty
	@ApiModelProperty(value = "Native",name="Native")
	private String Native="";//证件号码

	@JsonProperty
	@ApiModelProperty(value = "ParentFamilyAddress",name="ParentFamilyAddress")
	private String ParentFamilyAddress="";//证件号码

	@JsonProperty
	@ApiModelProperty(value = "BloodType",name="BloodType")
	private byte BloodType;//证件号码

	@JsonProperty
	@ApiModelProperty(value = "Birthday",name="Birthday")
	private int Birthday;//证件号码


	@JsonProperty
	@ApiModelProperty(value = "EducationType",name="EducationType")
	private byte EducationType;//证件号码

	@JsonProperty
	@ApiModelProperty(value = "Married",name="Married")
	private byte Married;//证件号码

	@JsonProperty
	@ApiModelProperty(value = "Married",name="Height")
	private int Height=0;//证件号码


	@JsonProperty
	@ApiModelProperty(value = "Weight",name="Weight")
	private int Weight;//证件号码

	@JsonProperty
	@ApiModelProperty(value = "MobilePhone",name="MobilePhone")
	private String MobilePhone="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "CurrentAddress",name="CurrentAddress")
	private String CurrentAddress="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "CurrentPhone",name="CurrentPhone")
	private String CurrentPhone="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "CurrentZip",name="CurrentZip")
	private String CurrentZip="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "FamilyAddress",name="FamilyAddress")
	private String FamilyAddress="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "FamilyPhone",name="FamilyPhone")
	private String FamilyPhone="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "FamilyZip",name="FamilyZip")
	private String FamilyZip="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "SOSName",name="SOSName")
	private String SOSName="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "SOSAddress",name="SOSAddress")
	private String SOSAddress="";//手机号


	@JsonProperty
	@ApiModelProperty(value = "SOSPhone",name="SOSPhone")
	private String SOSPhone="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "OnboardDate",name="OnboardDate")
	private int OnboardDate;//手机号

	@JsonProperty
	@ApiModelProperty(value = "ContractBeginDate",name="ContractBeginDate")
	private int ContractBeginDate;//手机号

	@JsonProperty
	@ApiModelProperty(value = "ContractEndDate",name="ContractEndDate")
	private int ContractEndDate;//手机号

	@JsonProperty
	@ApiModelProperty(value = "Email",name="Email")
	private String Email="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "Type",name="Type")
	private byte Type;//手机号

	@JsonProperty
	@ApiModelProperty(value = "Memo",name="Memo")
	private String Memo="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "CreatedDate",name="CreatedDate")
	private long CreatedDate;//手机号

	@JsonProperty
	@ApiModelProperty(value = "PrecedUpateDate",name="PrecedUpateDate")
	private long PrecedUpateDate;//手机号


	@JsonProperty
	@ApiModelProperty(value = "LastUpdateDate",name="LastUpdateDate")
	private long LastUpdateDate;//手机号

	@JsonProperty
	@ApiModelProperty(value = "0 在职 1 离职 3 回收站 ",name="IsDeleted")
	private int IsDeleted=0;//0 在职 1 离职 3 回收站


	@JsonProperty
	@ApiModelProperty(value = "Creator",name="Creator")
	private String Creator="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "Mender",name="Mender")
	private String Mender="";//手机号

	@JsonProperty
	@ApiModelProperty(value = "Inspector",name="Inspector")
	private String Inspector="";//手机号


	@JsonProperty
	@ApiModelProperty(value = "验证状态（0 解锁 1 上锁）",name="Validate")
	private int Validate;//验证状态（0 解锁 1 上锁）

	@JsonProperty
	@ApiModelProperty(value = "ValidateDate",name="ValidateDate")
	private long ValidateDate;//手机号

	@JsonProperty
	@ApiModelProperty(value = "ContractNo",name="ContractNo")
	private String ContractNo="";//手机号
	
	
	
	



}
