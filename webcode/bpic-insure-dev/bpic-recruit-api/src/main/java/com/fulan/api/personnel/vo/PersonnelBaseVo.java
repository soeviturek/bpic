package com.fulan.api.personnel.vo;

import java.util.List;

import com.fulan.api.personnel.domain.Educational;
import com.fulan.api.personnel.domain.FamilyMember;
import com.fulan.api.personnel.domain.Personnel;
import com.fulan.api.personnel.domain.WorkExperience;

import lombok.Data;
/**
 * 我的增员  vo类
 * @author chenzhuang
 *
 */
@Data
public class PersonnelBaseVo{
	
	Personnel personnel;
	
	List<FamilyMember> familyMemberList;
	
	List<Educational> educationalList;

	//工作经历
	List<WorkExperience> workExperienceList;

	//同业经历
	List<WorkExperience> InsuranceWorkExperience;
	
	
}
