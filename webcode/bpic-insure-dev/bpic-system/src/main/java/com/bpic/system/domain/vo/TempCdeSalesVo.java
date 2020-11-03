package com.bpic.system.domain.vo;

import com.bpic.common.core.domain.entity.SysRole;
import com.bpic.common.core.domain.entity.TdepartmentSales;

import java.util.Date;
import java.util.List;


public class TempCdeSalesVo {

    private static final long serialVersionUID = 1L;

    //创建时间
    private Date t_crt_tm;

    //修改人员
    private String	c_upd_cde;

    //修改时间
    private Date	t_upd_tm;

    //员工编码
    private String	c_emp_cde;

    //员工姓名
    private String	c_emp_cnm;

    //所属部门
    private String	c_dpt_cde;

    //密码
    private String	c_passwd;

    //启用状态
    private String	c_state;

    //手机号码
    private String	c_tel_mob;

    //公司名称
    private String c_dpt_cnm;

    //上级部门
    private String c_snr_dpt;

    //部门简称
    private String c_dpt_abr;

    //备注
    private String	c_remark;

    //职称
    private String	c_title_cde;

    //性别
    private String	c_sex;

    //邮编
    private String	c_zip_cde;

    private String c_salegrp_cde;

    public String getC_salegrp_cde() {
        return c_salegrp_cde;
    }

    public void setC_salegrp_cde(String c_salegrp_cde) {
        this.c_salegrp_cde = c_salegrp_cde;
    }

    public String getC_remark() {
        return c_remark;
    }

    public void setC_remark(String c_remark) {
        this.c_remark = c_remark;
    }

    public String getC_title_cde() {
        return c_title_cde;
    }

    public void setC_title_cde(String c_title_cde) {
        this.c_title_cde = c_title_cde;
    }

    public String getC_sex() {
        return c_sex;
    }

    public void setC_sex(String c_sex) {
        this.c_sex = c_sex;
    }

    public String getC_zip_cde() {
        return c_zip_cde;
    }

    public void setC_zip_cde(String c_zip_cde) {
        this.c_zip_cde = c_zip_cde;
    }

    public String getC_dpt_abr() {
        return c_dpt_abr;
    }

    public void setC_dpt_abr(String c_dpt_abr) {
        this.c_dpt_abr = c_dpt_abr;
    }

    public String getC_snr_dpt() {
        return c_snr_dpt;
    }

    public void setC_snr_dpt(String c_snr_dpt) {
        this.c_snr_dpt = c_snr_dpt;
    }

    //组级列表
    private String parentDeptCode;

    public String getParentDeptCode() {
        return parentDeptCode;
    }

    public void setParentDeptCode(String parentDeptCode) {
        this.parentDeptCode = parentDeptCode;
    }

    public String getC_dpt_cnm() {
        return c_dpt_cnm;
    }

    public void setC_dpt_cnm(String c_dpt_cnm) {
        this.c_dpt_cnm = c_dpt_cnm;
    }

    public String getC_state() {
        return c_state;
    }

    public void setC_state(String c_state) {
        this.c_state = c_state;
    }

    /** 角色对象 */
    private List<SysRole> roles;

    /** 角色组 */
    private Long[] roleIds;

    private TdepartmentSales dept;

    public TdepartmentSales getDept() {
        return dept;
    }

    public void setDept(TdepartmentSales dept) {
        this.dept = dept;
    }

    public Date getT_crt_tm() {
        return t_crt_tm;
    }

    public void setT_crt_tm(Date t_crt_tm) {
        this.t_crt_tm = t_crt_tm;
    }

    public String getC_upd_cde() {
        return c_upd_cde;
    }

    public void setC_upd_cde(String c_upd_cde) {
        this.c_upd_cde = c_upd_cde;
    }

    public Date getT_upd_tm() {
        return t_upd_tm;
    }

    public void setT_upd_tm(Date t_upd_tm) {
        this.t_upd_tm = t_upd_tm;
    }

    public String getC_emp_cde() {
        return c_emp_cde;
    }

    public void setC_emp_cde(String c_emp_cde) {
        this.c_emp_cde = c_emp_cde;
    }

    public String getC_emp_cnm() {
        return c_emp_cnm;
    }

    public void setC_emp_cnm(String c_emp_cnm) {
        this.c_emp_cnm = c_emp_cnm;
    }

    public String getC_dpt_cde() {
        return c_dpt_cde;
    }

    public void setC_dpt_cde(String c_dpt_cde) {
        this.c_dpt_cde = c_dpt_cde;
    }

    public String getC_passwd() {
        return c_passwd;
    }

    public void setC_passwd(String c_passwd) {
        this.c_passwd = c_passwd;
    }

    public String getC_tel_mob() {
        return c_tel_mob;
    }

    public void setC_tel_mob(String c_tel_mob) {
        this.c_tel_mob = c_tel_mob;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }
}
