package com.fulan.application.util;


import com.bpic.system.domain.vo.TempCdeSalesVo;
import com.fulan.api.personnel.vo.EmployeeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeUtil {

	private static Logger logger = LoggerFactory.getLogger(EmployeeUtil.class);
	

	/*
	 * 获取初审面试官
	 * */
	public EmployeeVo getFirstTrialEmployee(TempCdeSalesVo employeeVo, String position, Object obj){

		return null;
	}
	/*
	 * 获取复审面试官
	 * */
	public  EmployeeVo getSecondTrialEmployee(TempCdeSalesVo employeeVo,String position,Object obj){

		return null;
	}


	/*
	 * 证件类型转换
	 * */
	public static byte getIdCardCode(String idCard){
		byte result = 0;
		switch (idCard) {
		case "111":
			result = 0;
			break;
		case "114":
			result = 1;
			break;
		case "818":
			result = 4;
			break;
		case "414":
			result = 5;
			break;
		case "516":
			result = 6;
			break;
		default:
			result = 9;
			break;
		}
		return 	result ;
	}
	
	/*
	 * 学历转换
	 * */
	public static String getEduCode(String edu){
		String result = "0";
		switch (edu) {
		case "小学":
			result = "0";
			break;
		case "初中":
			result = "1";
			break;
		case "高中":
			result = "2";
			break;
		case "中专":
			result = "3";
			break;
		case "大专":
			result = "4";
			break;
		case "本科":
			result = "5";
			break;
		case "硕士":
			result = "6";
			break;
		case "博士":
			result = "7";
			break;
		default:
			result = "4";
			break;
		}
		return 	result ;
	}
}
