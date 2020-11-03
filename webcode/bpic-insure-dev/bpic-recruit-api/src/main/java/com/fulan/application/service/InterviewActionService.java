package com.fulan.application.service;

import com.fulan.api.flow.vo.FlowItemInterviewActionVo;
import com.fulan.api.personnel.domain.InterviewAction;

import java.util.List;

/**
 * <p>
 * 面试执行情况 服务类
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
public interface InterviewActionService {
	int insertInterviewAction(InterviewAction interviewAction);
	
	InterviewAction getInterviewAction(InterviewAction interviewAction);
	
	public List<FlowItemInterviewActionVo> getAllInterviewAction(InterviewAction interviewAction);

	Integer getInterviewActionResult(InterviewAction interviewAction);
}
