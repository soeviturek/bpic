package com.bpic.system.service;

import com.bpic.system.domain.AdviceFeedback;

import java.util.List;

public interface AdviceFeedbackService {
    /**
     * 保存意见反馈
     * @param adviceFeedback
     */
    void save(AdviceFeedback adviceFeedback);

    /**
     * 查询单个反馈
     * @param cellphone
     * @return
     */
    AdviceFeedback queryOne(String cellphone);

    /**
     * 查询意见反馈列表
     * @param advice_type
     * @return
     */
    List<AdviceFeedback> queryList(String advice_type);
}
