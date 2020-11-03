package com.fulan.application.service;

import com.baomidou.mybatisplus.plugins.Page;

import com.fulan.api.personnel.domain.Personnel;
import com.fulan.api.personnel.vo.*;


import java.util.List;

public interface AppPersonnelService {

	
	int getAgentPersonnelCount(String id);
	
	int getAgentPersonnelCountByViewer(String id);
	/**
	 * 代理人人才库
	 * @param personnelId
	 * @return
	 */
	List<AgentPersonnelInfo> getAgentPersonnel(Page<AgentPersonnelInfo> page, FilterVo filterVo);
	
	/**
	 * 合同签署
	 */
//	Response<List<Attachment>> signedContract(IntegrationVO integrationVO);
	
	/**
	 * 需要本人面试或面试过的人才（未审批）
	 * @param 查询条件
	 * @return
	 */
	List<AgentPersonnelInfo> getAgentPersonnelByViewer(Page<AgentPersonnelInfo> page, FilterVo filterVo);
	
	/**
	 * 需要本人面试或面试过的人才（已审批）
	 * @param 查询条件
	 * @return
	 */
	List<AgentPersonnelInfo> getApprovedAgentPersonnelByViewer(Page<AgentPersonnelInfo> page, FilterVo filterVo);

	AgentPersonnelInfo getPersonnelInfoForViewer(String personnelId) throws Exception;
	
	void savePersonnelInfo(EnterVo enterVo) throws Exception;
	
	PersonnelBaseVo getPersonnelBaseInfo(String personnelId) throws Exception;
	
	void updatePersonnelBaseInfo(PersonnelBaseVo personnelBaseVo) throws Exception;
	
	void updatePersonnel(Personnel personnel) throws Exception;

	void updatePersonnelInform(PersonnelInformVo personnelInformVo) throws Exception;
	
	PersonnelInformVo getInform(Personnel personnel) ;

	Personnel getPersonnelByPresonnel(Personnel personnel);

	Boolean clearBankInfo(Personnel personnel);

//	检查人员是否可以更新--关闭功能
    Boolean checkCanUpdateByPersonnelId(String personnelId);
}

