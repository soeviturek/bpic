package com.fulan.application.service;

import com.baomidou.mybatisplus.plugins.Page;

import com.bpic.common.utils.reponse.Response;
import com.fulan.api.personnel.domain.Check;
import com.fulan.api.personnel.domain.InterviewAction;
import com.fulan.api.personnel.domain.Personnel;
import com.fulan.api.personnel.domain.PresentRecord;
import com.fulan.api.personnel.vo.*;



import java.util.List;
import java.util.Map;

public interface PersonnelService {
	
	
	String checkResult(Personnel personnel);

	Response<String> submitPersonnel(Personnel personnel);
	// 后台根据人才主键查询详情
//	PersonnelManageInfoVo PersonnelCheck(String paperId);

	Response<String> saveInfo1(PersonnelVo1 personnelVo1);

	/**
	 * APP发起招募
	 * @param account
	 * @return
	 */
	Response<Personnel> add(Personnel personnelExpVo);

	/**
	 * APP招募录入
	 * @param account
	 * @return
	 */
	Response<String> saveInfo(Personnel personnel);

	/**
	 * 人才详情-根据personnelId查询个人信息
	 * @param personnelId
	 * @return
	 */
	Response<Map<String, Object>> getPersonnel(Long personnelId);



	Response<String> insertSpecific(InterviewActionVo interviewActionVo);

	Response<String> insertSpecificNew(InterviewActionVo interviewActionVo);



	Response<String> updatePersonnerStaus(String id, String personnelStatus);

	Response<String> updateSpecific(InterviewActionVo interviewActionVo);



	Response<String> updateInterviewActionByIdCode(String id, String nowItem, String status);
	


	Personnel selectPersonnerById(String id);

	PersonnelFlowItemVo selectPersonner(String id);

	Response<String> updateBatchPersonnelisSend(String ids);


	List<PersonnelResultVo> personnelList(PersonnelSearchVo personnelSearchVo);


	//PersonnelFlowVo personnelFlow(String personnelId);


    Check getImgCheck(String personnelId);

	int checkUrl(long personnelId);

    Response<String> checkDataResult(String personnelId, String checkOpinion, String s, String c_emp_cnm, String c_emp_cde);
}

