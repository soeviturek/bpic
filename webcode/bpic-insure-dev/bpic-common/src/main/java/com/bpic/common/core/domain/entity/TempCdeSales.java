package com.bpic.common.core.domain.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_emp_cde_sales")
public class TempCdeSales {

    private static final long serialVersionUID = 1L;
    //创建人员
    private String	c_crt_cde;

    //创建时间
    private Date	t_crt_tm;

    //修改人员
    private String	c_upd_cde;

    //修改时间
    private Date	t_upd_tm;

    //员工编码
    private String	c_emp_cde;

    //员工姓名
    private String	c_emp_cnm;

    //出生日期
    private Date	t_birthday;

    //性别
    private String	c_sex;

    //民族
    private String	c_ntn_cde;

    //教育程度
    private String	c_edu_cde;

    //婚姻状况
    private String	c_mrg_cde;

    //证件类别
    private String	c_ctfct_typ;

    //证件号码
    private String	c_ctfct_no;

    //户口所在地
    private String	c_regist;

    //入职时间
    private Date	t_ent_tm;

    //所属部门
    private String	c_dpt_cde;

    //员工英文名
    private String	c_emp_enm;

    //专业
    private String	c_major_cde;

    //职称
    private String	c_title_cde;

    //爱好
    private String	c_hobby;

    //党派
    private String	c_prty_typ;

    //住址
    private String	c_home_addr;

    //邮编
    private String	c_zip_cde;

    //电话
    private String	c_tel;

    //转正时间
    private Date	t_reg_tm;

    //离司时间
    private Date	t_lev_tm;

    //个人档案
    private String	c_emp_doc;

    //备注
    private String	c_remark;

    //密码
    private String	c_passwd;

    /** 盐加密 */
    @TableField(exist=false)
    private String salt;

    //是否操作员
    private String	c_passwd_mrk;

    //团队
    private String	c_salegrp_cde;

    //_r_o_w__i_d
    private String	row_id;

    //学位
    private String	c_dre_cde;

    //目前状态
    private String	c_status;

    //担保人一
    private String	c_grnt_cde1;

    //担保人二
    private String	c_grnt_cde2;

    //倒签单标志
    private String	c_dq_mk;

    //-(未用)
    private String	c_sim;

    //人员属性
    private String	c_emp_type;

    //上次密码修改时间
    private Date	t_passwd_upd_tm;

    //政治面貌
    private String	c_poli_face;

    //手机号码
    private String	c_tel_mob;

    //代理资格证书编号
    private String	c_qlft_no;

    //代理资格证书有效期
    private Date	t_qlft_end_tm;

    //展业证号
    private String	c_bsns_no;

    //展业证有效期
    private Date	t_bsns_end_tm;

    //银行卡卡号
    private String	c_card_no;

    //启用状态
    private String	c_state;

    //类型代码
    private String	c_type_cde;

    //级别代码
    private String	c_level_cde;

    //任职机构
    private String	c_dpt_cde2;

    //任职起期
    private Date	t_duty_strt_tm;

    //任职止期
    private Date	t_duty_end_tm;

    //合同类型
    private String	c_pact_cde;

    //
    private String	c_send_state;

    //
    private String	c_emp_pro;
    @TableField(exist = false)
    private TdepartmentSales dept;

    @TableField(exist = false)
    private List<String> parentDeptCode;

    public TdepartmentSales getDept() {
        return dept;
    }

    public void setDept(TdepartmentSales dept) {
        this.dept = dept;
    }

    @TableField(exist = false)
    private String currentUser;
    /** 角色对象 */
    @TableField(exist = false)
    private List<SysRole> roles;

    /** 角色组 */
    @TableField(exist = false)
    private Long[] roleIds;

    public boolean isAdmin()
    {
        return isAdmin(this.c_emp_cde);
    }

    public static boolean isAdmin(String userId)
    {
        return userId != null ;
    }

    public TempCdeSales(String c_tel_mob,String c_passwd,String c_emp_cde) {
        this.c_tel_mob = c_tel_mob;
        this.c_passwd = c_passwd;
        this.c_emp_cde = c_emp_cde;
    }
}
