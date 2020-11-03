package com.bpic.system.domain.vo;

public class SelectSalesVo {
    private String status;
    private String c_emp_cde;
    private String c_dpt_cde;
    private String c_snr_dpt;
    //部门简称
    private String c_dpt_abr;

    public String getC_dpt_abr() {
        return c_dpt_abr;
    }

    public void setC_dpt_abr(String c_dpt_abr) {
        this.c_dpt_abr = c_dpt_abr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getC_emp_cde() {
        return c_emp_cde;
    }

    public void setC_emp_cde(String c_emp_cde) {
        this.c_emp_cde = c_emp_cde;
    }

    public String getC_dpt_cde() {
        return c_dpt_cde;
    }

    public void setC_dpt_cde(String c_dpt_cde) {
        this.c_dpt_cde = c_dpt_cde;
    }

    public String getC_snr_dpt() {
        return c_snr_dpt;
    }

    public void setC_snr_dpt(String c_snr_dpt) {
        this.c_snr_dpt = c_snr_dpt;
    }
}
