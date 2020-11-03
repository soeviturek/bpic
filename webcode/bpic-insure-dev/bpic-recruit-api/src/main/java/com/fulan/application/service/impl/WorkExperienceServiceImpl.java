package com.fulan.application.service.impl;

import com.bpic.common.utils.id.IdGenerator;
import com.bpic.common.utils.reponse.Response;
import com.fulan.api.personnel.domain.Personnel;
import com.fulan.api.personnel.domain.WorkExperience;
import com.fulan.application.mapper.PersonnelMapper;
import com.fulan.application.mapper.WorkExperienceMapper;

import com.fulan.application.service.WorkExperienceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("WorkExperienceServiceImpl")
public class WorkExperienceServiceImpl implements WorkExperienceService {
	
	@Autowired
    WorkExperienceMapper workExperienceMapper;
	
	@Autowired
	IdGenerator idGenerator;
	
	@Autowired
    PersonnelMapper personnelMapper;
	
	
	/**
	 * 人才详情-查找工作经历
	 */
	@Override
	public Response<Map<String,Object>> getWorkExperience(Long personnelId) {
		Response<Map<String,Object>> resp = new Response<Map<String,Object>>(Response.SUCCESS, "人才详情-查找工作经历成功");
		List<WorkExperience> list = workExperienceMapper.selectByPersonnelId(personnelId);
		//根据personnelId查找个人信息
		Personnel personnel = personnelMapper.selectByPersonnelId(personnelId);
		String isInsuranceCompany = "";
		if(personnel.getIsInsuranceCompany()!=null&&!"".equals(personnel.getIsInsuranceCompany())){
			isInsuranceCompany = personnel.getIsInsuranceCompany().toString();//是否有保险公司经历
		}
		String isOverSix = "";
		if(personnel.getIsOverSix()!=null&&!"".equals(personnel.getIsInsuranceCompany())){		
			isOverSix = personnel.getIsOverSix().toString();//离职是否满6个月
		}
		String nativeWorkTime = "";
		if(personnel.getNativeWorkTime()!=null&&!"".equals(personnel.getNativeWorkTime())){
		  nativeWorkTime = personnel.getNativeWorkTime();//本地工作时长
		}
		String originalCompany = "";
		if(personnel.getOriginalCompany()!=null&&!"".equals(personnel.getOriginalCompany())){
			originalCompany = personnel.getOriginalCompany();//原保险机构名称
		}
		String isQuit = "";
		if(personnel.getIsQuit()!=null){
			isQuit = String.valueOf(personnel.getIsQuit());//是否离职(1:是，0，否)
		}
		String departureDate = null;
		if(personnel.getDepartureDate()!=null&&!"".equals(personnel.getDepartureDate())){
			departureDate  = new SimpleDateFormat("yyyy-M-dd").format(personnel.getDepartureDate());//离职日期
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("workExperience", list);
		map.put("isInsuranceCompany", isInsuranceCompany);
		map.put("isOverSix", isOverSix);
		map.put("nativeWorkTime", nativeWorkTime);
		map.put("originalCompany", originalCompany);
		map.put("isQuit", isQuit);
		map.put("departureDate", departureDate);
		resp.setData(map);
		return resp;
	}
	/**
	 * 根据personnelId删除指定个人的所有工作经历信息
	 */
	@Override
	@Transactional
	public Response<String> deleteByPersonnelId(Long personnelId) {
		Response<String> resp = new Response<String>(Response.SUCCESS, "删除指定个人工作经历信息成功");
		workExperienceMapper.deleteByPersonnelId(personnelId);
		return resp;
	}

	@Override
	@Transactional
	public Response<String> save(List<WorkExperience> workExperienceList, Personnel personnel) {
		Response<String> resp=new Response<String>(Response.SUCCESS, "添加工作经历成功");
		//删除指定个人的所有工作经历信息
		deleteByPersonnelId(personnel.getId());
		//根据personnelId更新相应字段
		personnelMapper.updateByPersonnelId(personnel);
		for(WorkExperience workExperience : workExperienceList){
			Long ids = idGenerator.generate();
			workExperience.setId(ids);
			workExperienceMapper.insert(workExperience);
		}
		resp.setData(personnel.getId().toString());
		return resp;
	}

	@Override
	public List<WorkExperience> getWorkExperienceById(Long personnelId) {
		return workExperienceMapper.selectByPersonnelId(personnelId);
	}

	@Override
	public Response<Map<String, Object>> getPersonnelWorkExperience(Long personnelId) {
		Response<Map<String,Object>> resp = new Response<Map<String,Object>>(Response.SUCCESS, "获取个人工作经历成功");
		List<WorkExperience> list = workExperienceMapper.selectByPersonnelId(personnelId);
		//同业工作经历
		List<WorkExperience> InsuranceWorkExperiencelist = new ArrayList();
		//工作经历
		List<WorkExperience> WorkExperiencelist = new ArrayList();
		for (WorkExperience workExperience:
		list) {
			if(workExperience.getIsInsuranceWork().equals("1")){
				InsuranceWorkExperiencelist.add(workExperience);
			}
			if(workExperience.getIsInsuranceWork().equals("0")){
				WorkExperiencelist.add(workExperience);
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("InsuranceWorkExperience", InsuranceWorkExperiencelist);
		map.put("WorkExperience", WorkExperiencelist);
		resp.setData(map);
		return resp;
	}

}
