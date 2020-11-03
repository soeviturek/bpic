package com.bpic.system.domain.vo;

import lombok.Data;

@Data
public class UserSignVo {
    private Integer id;
    private String user_mob;
    private String sign_date;
    private Integer is_signed;
    private Integer maxs;
    private String update_time;
    private Integer rownum;
}
