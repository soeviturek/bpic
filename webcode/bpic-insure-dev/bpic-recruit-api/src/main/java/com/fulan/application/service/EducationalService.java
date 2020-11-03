package com.fulan.application.service;

import com.bpic.common.utils.reponse.Response;
import com.fulan.api.personnel.domain.Educational;


import java.util.List;

public interface EducationalService {
	/**
	 * 在线增员-教育经历
	 * @param account
	 * @return
	 */
	Response<String> save(List<Educational> educationalList);
	/**
	 * 人才详情-教育经历
	 * @param personnelId
	 * @return
	 */
	Response<List<Educational>> getEducational(Long personnelId);
	/**
	 * 根据personnelId删除所有教育经历信息
	 * @param personnelId
	 * @return
	 */
	Response<String> deleteByPersonnelId(Long personnelId);
}
