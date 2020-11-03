package com.fulan.application.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fulan.api.flow.vo.FlowItemInterviewActionVo;
import com.fulan.api.personnel.domain.InterviewAction;
import com.fulan.application.mapper.InterviewActionMapper;
import com.fulan.application.service.InterviewActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 面试执行情况 服务实现类
 * </p>
 *
 * @author chenzhuang123
 * @since 2018-01-24
 */
@Service("InterviewActionServiceImpl")
public class InterviewActionServiceImpl extends ServiceImpl<InterviewActionMapper, InterviewAction> implements InterviewActionService {

	@Autowired
    InterviewActionMapper interviewActionMapper;
	@Override
	@Transactional
	public int insertInterviewAction(InterviewAction interviewAction) {
		return interviewActionMapper.insert(interviewAction);
	}
	@Override
	public InterviewAction getInterviewAction(InterviewAction interviewAction) {
		return interviewActionMapper.selectOne(interviewAction);
	}
	@Override
	public List<FlowItemInterviewActionVo> getAllInterviewAction(InterviewAction interviewAction) {
		return interviewActionMapper.selectByPersonnelId(interviewAction);
	}

	@Override
	public Integer getInterviewActionResult(InterviewAction interviewAction) {
		Integer statu = -1;
		InterviewAction interviewAction1 = null;
		if(interviewAction != null){
			 interviewAction1 = interviewActionMapper.getInterviewActionResult(interviewAction);
		}
		//面试不存在
		if(interviewAction1 == null){
			return statu;
		}
		//面试没有进行
		if(interviewAction1.getProcessingStatus() == null){
			statu = 0;
		}else{
			//面试已完成
			statu = Integer.valueOf(interviewAction1.getProcessingStatus());
		}
		return  statu;
	}

}
