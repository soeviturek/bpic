package com.bpic.system.domain;

import lombok.Data;

import java.util.List;

@Data
public class AdviceFeedback {
    /**主键 */
    private Integer id;
    /**意见类型 ：1.功能异常  2.体验优化 3.其他  4.新功能建议   */
    private String advice_type;
    /** 建议详情 */
    private String advice_info;
    /** 截图 */
    private List<String> picture;
    /** 联系人 */
    private String cellphone;
    /** 更新时间 */
    private String update_time;
}
