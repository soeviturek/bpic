package com.fulan.api.personnel.vo;

import com.fulan.api.personnel.domain.Educational;
import com.fulan.api.personnel.domain.Personnel;
import com.fulan.api.personnel.domain.WorkExperience;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PersonnelVo1 implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7868362327445883063L;

	/**
	 * 
	 */

	private Personnel personnel;

	List<WorkExperience> workExperienceList;

	List<Educational> educationalList;
	
}
