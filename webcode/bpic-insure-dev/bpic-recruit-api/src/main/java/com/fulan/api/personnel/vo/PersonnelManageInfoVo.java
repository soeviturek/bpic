package com.fulan.api.personnel.vo;

import java.util.List;


import com.fulan.api.personnel.domain.Educational;
import com.fulan.api.personnel.domain.FamilyMember;
import com.fulan.api.personnel.domain.Personnel;

import com.fulan.api.personnel.domain.WorkExperience;

import lombok.Data;

/**
 * 后台人才个人信息详情 vo
 * @author kang
 *
 */
@Data
public class PersonnelManageInfoVo {
	private Personnel personnel;
	private List<FamilyMember> familyMember;
	private List<Educational> educational;
	private List<WorkExperience> workExperience;

	
	
}
