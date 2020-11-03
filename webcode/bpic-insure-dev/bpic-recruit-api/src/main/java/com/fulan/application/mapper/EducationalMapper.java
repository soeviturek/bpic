package com.fulan.application.mapper;

import com.fulan.api.personnel.domain.Educational;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EducationalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Educational record);

    int insertSelective(Educational record);

    Educational selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Educational record);

    int updateByPrimaryKey(Educational record);
    
    List<Educational> selectByPersonnelId(Long personnelId);
    
    int deleteByPersonnelId(Long personnelId);
    
}