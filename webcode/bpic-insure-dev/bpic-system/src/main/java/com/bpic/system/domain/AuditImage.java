package com.bpic.system.domain;

import lombok.Data;

import java.util.Date;
@Data
public class AuditImage {
    /** 主键 */
    private String id;

    /** 手机号 */
    private String c_tel_mob;

    /** 正面照片 path */
    private String front_path;

    /** 反面照 path */
    private String back_path;

    /** 审核状态  已审核 未审核*/
    private String audit_status;
    /**
     *删除标志（0代表存在 2代表删除）
     */
    private String	del_flag;
    /**
     *创建者
     */
    private String	create_by;
    /**
     *创建时间
     */
    private Date create_time;
    /**
     *更新者
     */
    private String	update_by;
    /**
     *更新时间
     */
    private Date update_time;

    /**
     * 审核意见
     */
    private String advice;
}
