package com.bpic.system.domain;

import lombok.Data;


@Data
public class UserSign {
   private Integer id;
   private String user_mob;
   private String sign_date;
   private Integer is_signed;
   private Integer maxs;
   private String update_time;
}
