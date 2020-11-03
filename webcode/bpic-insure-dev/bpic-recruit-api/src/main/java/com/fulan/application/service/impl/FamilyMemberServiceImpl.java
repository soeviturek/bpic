package com.fulan.application.service.impl;

import com.bpic.common.utils.id.IdGenerator;
import com.bpic.common.utils.reponse.Response;
import com.fulan.api.personnel.domain.FamilyMember;
import com.fulan.application.mapper.FamilyMemberMapper;

import com.fulan.application.service.FamilyMemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("FamilyMemberServiceImpl")
public class FamilyMemberServiceImpl implements FamilyMemberService {

	@Autowired
    FamilyMemberMapper familyMemberMapper;

	@Autowired
	IdGenerator idGenerator;

	@Override
	@Transactional
	public Response<String> save(List<FamilyMember> familyMemberList) {
		Response<String> resp=new Response<String>(Response.SUCCESS, "添加家庭信息成功");
		//删除指定个人的所有家庭信息
		deleteByPersonnelId(familyMemberList.get(0).getPersonnelId());
		for(FamilyMember familyMember:familyMemberList){
			Long ids = idGenerator.generate();
			familyMember.setId(ids);
			familyMemberMapper.insert(familyMember);
		}
		resp.setData(familyMemberList.get(0).getPersonnelId().toString());
		return resp;
	}
	/**
	 * 人才详情-查看家庭信息
	 */
	@Override
	public Response<List<FamilyMember>> getFamilyMember(FamilyMember familyMember) {
		Response<List<FamilyMember>> resp = new Response<List<FamilyMember>>(Response.SUCCESS, "人才详情-查看家庭信息成功");
		List<FamilyMember> list = familyMemberMapper.selectByPersonnelId(familyMember);
		resp.setData(list);
		return resp;
	}
	/**
	 * 根据personnelId删除所有家庭信息
	 */
	@Override
	@Transactional
	public Response<String> deleteByPersonnelId(Long personnelId) {
		Response<String> resp = new Response<String>(Response.SUCCESS, "删除某个人的所有家庭信息成功");
		familyMemberMapper.deleteByPersonnelId(personnelId);
		return resp;
	}

}
