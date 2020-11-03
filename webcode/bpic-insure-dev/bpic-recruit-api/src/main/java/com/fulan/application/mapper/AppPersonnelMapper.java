package com.fulan.application.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fulan.api.personnel.domain.Personnel;
import com.fulan.api.personnel.vo.AgentPersonnelInfo;
import com.fulan.api.personnel.vo.EnterVo;
import com.fulan.api.personnel.vo.FilterVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AppPersonnelMapper extends BaseMapper<Personnel>{
	
	int getAgentPersonnelCount(String id);
	
	int getAgentPersonnelCountByViewer(String id);
   
    List<AgentPersonnelInfo> getAgentPersonnel(Page<AgentPersonnelInfo> page, FilterVo filterVo);
    
    List<AgentPersonnelInfo> getAgentPersonnelByViewer(Page<AgentPersonnelInfo> page, FilterVo filterVo);
  
    List<AgentPersonnelInfo> getApprovedAgentPersonnelByViewer(Page<AgentPersonnelInfo> page, FilterVo filterVo);
    
    void updatePersonnelInfo(EnterVo enterVo);
    
    void updateInterviewInfo(EnterVo enterVo);
    
    void insertInterviceInfo(EnterVo enterVo);
    
    void updatePersonnelBaseInfo(Personnel Personnel);
    
    AgentPersonnelInfo getPersonnelInfoForViewer(String personnelId);
    
    Personnel getPersonnelBaseInfo(String personnelId);

    void clearBankInfo(Personnel personnel);
}