package com.fulan.api.personnel.vo;

import com.fulan.api.personnel.domain.FlowItem;
import lombok.Data;

import java.util.List;

/**
 * @author 陈磊
 * @date: 2019/9/18
 * Time:           21:11
 * Version:        1.0.0
 * Description:    PersonnelFlow类用于——显示个人流程节点
 */
@Data
public class PersonnelFlowVo {

    List<FlowItem> flowList;

    List<FlowItemActionPersonnerVo> flowItemPersonner;

}
