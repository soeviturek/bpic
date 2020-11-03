package com.bpic.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class SysLoginUser {
    /**
     *员工编码
     */
    private String	c_emp_cde;
    /**
     *上线员工姓名
     */
    private String	c_emp_cnm;
    /**
     *上线员工所属部门
     */
    private String	c_dpt_cde;
    /**
     *上线员工手机号
     */
    private String	cphone;
    /**
     *用户账号
     */
    private String	user_name;
    /**
     *手机号码
     */
    private String	c_tel_mob;
    /**
     * 上级员工号
     */
    private String	c_emp_cde2;
    /**
     *用户性别（0男 1女 2未知）
     */
    private String	sex;
    /**
     *密码
     */
    private String	c_passwd;
    /**
     *帐号状态（0正常 1停用）
     */
    private String	status;
    /**
     * 职称信息
     */
    private String c_title_cde;
    /**
     *删除标志（0代表存在 2代表删除）
     */
    private String	del_flag;
    /**
     *最后登陆IP
     */
    private String	login_ip;
    /**
     *最后登陆时间
     */
    private Date login_date;
    /**
     *创建者
     */
    private String	create_by;
    /**
     *创建时间
     */
    private Date	create_time;
    /**
     *更新者
     */
    private String	update_by;
    /**
     *更新时间
     */
    private Date	update_time;
    /**
     *备注
     */
    private String	remark;
    /**
     *发展日期
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date  develop_time;
    /**
     * 教育程度
     */
    private String c_edu_cde;

    /** 角色对象 */
    private List<SysRole> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 头像 */
    private String img;
}
