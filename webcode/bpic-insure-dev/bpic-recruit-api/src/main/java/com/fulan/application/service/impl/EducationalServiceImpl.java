package com.fulan.application.service.impl;

import com.bpic.common.utils.id.IdGenerator;
import com.bpic.common.utils.reponse.Response;
import com.fulan.api.personnel.domain.Educational;
import com.fulan.application.mapper.EducationalMapper;

import com.fulan.application.service.EducationalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("EducationalServiceImpl")
public class EducationalServiceImpl implements EducationalService {
	
	@Autowired
    EducationalMapper educationalMapper;
	
	@Autowired
	IdGenerator idGenerator;
	/**
	 * 在线增员-插入教育经历
	 */
	@Override
	@Transactional
	public Response<String> save(List<Educational> educationalList) {
		Response<String> resp=new Response<String>(Response.SUCCESS, "添加教育经历成功");
		//删除指定个人的所有教育经历信息
		deleteByPersonnelId(educationalList.get(0).getPersonnelId());
		for(Educational educational:educationalList){
			Long ids = idGenerator.generate();//主键id
			educational.setId(ids);
			educationalMapper.insert(educational);
		}
		resp.setData(educationalList.get(0).getPersonnelId().toString());
		return resp;
	}
	
	/**
	 * 人才详情-查看教育经历
	 */
	@Override
	public Response<List<Educational>> getEducational(Long personnelId) {
		Response<List<Educational>> resp = new Response<List<Educational>>(Response.SUCCESS, "添加账号成功");
		List<Educational> list = educationalMapper.selectByPersonnelId(personnelId);
		resp.setData(list);
		return resp;
	}
	
	/**
	 * 根据personnelId删除所有教育经历信息
	 */
	@Override
	@Transactional
	public Response<String> deleteByPersonnelId(Long personnelId) {
		Response<String> resp = new Response<String>(Response.SUCCESS, "删除指定个人所有教育经历信息成功");
		educationalMapper.deleteByPersonnelId(personnelId);
		return resp;
	}

}
