package com.bpic.common.core.domain.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TdepartmentSales {
    private static final long serialVersionUID = 1L;
    //创建人
    private String	c_crt_cde;

    //创建时间
    private  Date	t_crt_tm;

    //修改人
    private String	c_upd_cde;

    //修改时间
    private Date t_upd_tm;

    //机构编码
    private String	c_dpt_cde;

    //机构名称
    private String	c_dpt_cnm;

    //机构简称
    private String	c_dpt_abr;

    //机构级别
    private String	n_dpt_levl;

    //成立时间
    private Date	t_fnd_tm;

    //机构地址
    private String	c_dpt_caddr;

    //邮政编码
    private String	c_zip_cde;

    //英文名称
    private String	c_dpt_enm;

    //英文地址
    private String	c_dpt_eaddr;

    //电话
    private String	c_tel;

    //传真
    private String	c_fax;

    //上级部门
    private String	c_snr_dpt;

    //联系人
    private String	c_ctct_cde;

    //立案前需报案标志
    private String	c_rpt_msk;

    //报案电话
    private String	c_rpt_tel;

    //失效时间
    private Date	t_adb_tm;

    //部门标志
    private String	c_department_mrk;

    //咨询电话
    private String	c_cons_tel;

    //保险许可证
    private String	c_insprmt_no;

    //税务登记号
    private String	c_taxrgst_no;

    //工登记号
    private String	c_bnsrgst_no;

    //报警标志
    private String	c_alarm_mrk;

    //_r_o_w__i_d
    private String	row_id;

    //机构内码
    private String	c_inter_cde;

    //报案地址
    private String	c_rpt_addr;

    //车牌号简称
    private String	c_lcn_abr;

    //机构流水号
    private String	c_dpt_serno;

    //机构类型
    private String	c_dpt_typ;

    //纳税人识别号
    private String	c_taxpayer_id;

    //终止时间
    private Date	t_end_tm;

    //启用状态
    private String	c_state;

    //级别代码
    private String	c_level_code;

    //类型代码
    private String	c_type_code;

    //任职起期
    private Date	t_duty_strt_tm;

    //任职止期
    private Date	t_duty_end_tm;

    //
    private String	c_mmb_no;

    /** 子部门 */
    private List<TdepartmentSales> children = new ArrayList<TdepartmentSales>();
}
