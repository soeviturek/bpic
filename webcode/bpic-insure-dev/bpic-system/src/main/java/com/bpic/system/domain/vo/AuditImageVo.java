package com.bpic.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AuditImageVo {
    /** 手机号 */
    private String c_tel_mob;

    /** 正面照片 path */
    private String front_path;

    /** 反面照 path */
    private String back_path;
    /** 审核状态 */
    private String audit_status;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private String create_time;

    private String advice;


}
