package com.bpic.system.domain;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WechatUser {
    private Integer id;
    private String open_id;
    private String nick_name;
    private String sex;
    private String province;
    private String city;
    private String country;
    private String head_img_url;
    private String privilege;
    private String cellphone;
    private String union_id;
    private String update_time;
}
