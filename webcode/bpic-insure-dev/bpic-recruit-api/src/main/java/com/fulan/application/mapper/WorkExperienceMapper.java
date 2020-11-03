package com.fulan.application.mapper;

import com.fulan.api.personnel.domain.InterviewAction;
import com.fulan.api.personnel.domain.WorkExperience;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkExperienceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WorkExperience record);

    int insertInfo(WorkExperience record);

    int insertSelective(WorkExperience record);

    WorkExperience selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WorkExperience record);

    int updateByPrimaryKey(WorkExperience record);
    
    int updateByPersonnelId(WorkExperience record);
    
    List<WorkExperience> selectByPersonnelId(Long personnelId);
    
    int deleteByPersonnelId(Long personnelId);

    int backFlow(InterviewAction interviewAction2);
}