package com.fulan.application.mapper;

import com.fulan.api.personnel.domain.FamilyMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FamilyMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FamilyMember record);

    int insertSelective(FamilyMember record);

    FamilyMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FamilyMember record);

    int updateByPrimaryKey(FamilyMember record);
    
    List<FamilyMember> selectByPersonnelId(FamilyMember familyMember);
    
    int deleteByPersonnelId(Long personnelId);

	List<FamilyMember> selecPersonnelById(Long idl);
}