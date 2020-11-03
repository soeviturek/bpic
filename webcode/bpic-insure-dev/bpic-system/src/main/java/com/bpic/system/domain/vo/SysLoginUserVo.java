package com.bpic.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SysLoginUserVo {
    private String c_emp_cde2;
    private String  c_emp_cnm;
    private String user_name;
    private String c_dpt_cde;
    private String c_title_cde;
    private String c_emp_cde;
    private String del_flag;
    public String getC_emp_cde() {
        return c_emp_cde;
    }
    private String dept;
    private String cphone;
    private String sex;
    private String c_tel_mob;
    private String c_passwd;
    private String status;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date develop_time;
    private String img;
}
