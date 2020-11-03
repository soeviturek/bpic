package com.fulan.api.personnel.vo;

import java.util.List;

import com.fulan.api.personnel.domain.PersonnelInform;
import com.fulan.api.personnel.domain.PersonnelInformInfo;

import lombok.Data;
/**
 * 我的增员  vo类
 * @author chenzhuang
 *
 */
@Data
public class PersonnelInformVo{
	
	private List<PersonnelInformInfo> personnelInformInfoList;
	
	private PersonnelInform personnelInform;
	
	
	
}
