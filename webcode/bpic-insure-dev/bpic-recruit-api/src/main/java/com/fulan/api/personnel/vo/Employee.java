package com.fulan.api.personnel.vo;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author kang123
 * @since 2020-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Employee extends Model<Employee> {

	private static final long serialVersionUID = 1L;

	private String EmployeeID;
	private String PresentCode;
	private String BranchID;
	private String DepartmentID;
	private String EmployeeName;
	private String EmployeeNameEN;
	private String IntroducerID;
	private Integer CertificateType;
	private String CertificateCode;
	private String PhotoPath;
	private String Sex;
	private Integer NationID;
	private String Native;
	private String ParentFamilyAddress;
	private Integer BloodType;
	private Integer Birthday;
	private Integer EducationType;
	private Integer Married;
	private Integer Height;
	private Integer Weight;
	private String MobilePhone;
	private String CurrentAddress;
	private String CurrentPhone;
	private String CurrentZip;
	private String FamilyAddress;
	private String FamilyPhone;
	private String FamilyZip;
	private String SOSName;
	private String SOSAddress;
	private String SOSPhone;
	private Integer OnboardDate;
	private Date ContractBeginDate;
	private Date ContractEndDate;
	private String Email;
	private Integer Type;
	private String Memo;
	private Long CreatedDate;
	private Long PrecedUpdateDate;
	private Long LastUpdateDate;
	private Long ValidateDate;
	private String Creator;
	private String Mender;
	private String Inspector;
	private Integer Validate;
	private Integer IsDeleted;
	private String ContractNo;
	private Float PhotoSize;
	private Integer id;
	
	
	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
