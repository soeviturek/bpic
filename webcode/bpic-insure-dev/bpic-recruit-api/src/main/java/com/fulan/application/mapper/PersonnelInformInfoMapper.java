package com.fulan.application.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fulan.api.personnel.domain.PersonnelInformInfo;
import com.fulan.api.personnel.vo.PersonnelInformVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface PersonnelInformInfoMapper extends BaseMapper<PersonnelInformInfo>{
	PersonnelInformVo getInform(Map<String, Object> map);
	

    int insertSelective(PersonnelInformInfo record);
}