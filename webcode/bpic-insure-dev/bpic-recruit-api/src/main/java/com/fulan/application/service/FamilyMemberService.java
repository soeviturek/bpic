package com.fulan.application.service;

import com.bpic.common.utils.reponse.Response;
import com.fulan.api.personnel.domain.FamilyMember;


import java.util.List;

public interface FamilyMemberService {
	
	/**
	 * 在线增员-家庭成员
	 * @param account
	 * @return
	 */
	Response<String> save(List<FamilyMember> familyMemberList);
	/**
	 * 人才详情-家庭信息
	 * @param personnelId
	 * @return
	 */
	Response<List<FamilyMember>> getFamilyMember(FamilyMember familyMember);
	/**
	 * 根据personnelId删除所有家庭成员信息
	 * @param personnelId
	 * @return
	 */
	Response<String> deleteByPersonnelId(Long personnelId);
}
