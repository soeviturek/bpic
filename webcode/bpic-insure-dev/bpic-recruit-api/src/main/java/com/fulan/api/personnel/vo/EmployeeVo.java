package com.fulan.api.personnel.vo;

import lombok.Data;
@Data
public class EmployeeVo extends Employee{
	
	
	
	private String duty;
	private String dutyName;
	private String superEmployeeID;
	private String superPresentCode;
	
}
