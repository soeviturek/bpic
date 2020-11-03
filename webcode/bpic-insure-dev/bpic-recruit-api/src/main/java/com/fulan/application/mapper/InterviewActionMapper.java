package com.fulan.application.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fulan.api.flow.vo.FlowItemInterviewActionVo;
import com.fulan.api.personnel.domain.InterviewAction;
import com.fulan.api.personnel.vo.InterviewActionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 面试执行情况 Mapper 接口
 * </p>
 *
 * @author chenzhuang123
 * @since 2018-01-24
 */
@Mapper
public interface InterviewActionMapper extends BaseMapper<InterviewAction> {
	 int updateByPrimaryPersonnel(InterviewAction interviewAction);

	 int insertInterviewAction(InterviewActionVo interviewActionVo);

	 void updateNowPersonnerStaus(InterviewActionVo interviewActionVo);
	
	 List<FlowItemInterviewActionVo> selectByPersonnelId(InterviewAction interviewAction);
	 
	 List<InterviewAction> selectByPersonnelIdAndFlowItemId(InterviewAction interviewAction);

	int updateSpecific(InterviewActionVo interviewActionVo);

	int updateInterviewActionByIdCode(@Param("id") String id, @Param("nowItem") String nowItem, @Param("status") String status, @Param("endTime") Date endTime);

	InterviewAction getFirstInterviewAction(InterviewAction interviewAction);

	int deleteByPersonnelID(@Param("id") String id, @Param("nowItem") String nowItem);

    void completeStudy(InterviewAction updataInterviewAction);

    InterviewAction getInterviewActionResult(InterviewAction interviewAction);

	InterviewAction getNowInterviewAction(@Param("personnelId") Long personnelId);

	int backInterviewAction(InterviewAction interviewAction2);

	int updateInterviewAction(InterviewAction interviewAction2);
}
