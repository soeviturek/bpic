package com.bpic.system.mapper;

import com.bpic.system.domain.WechatUser;

public interface WechatMapper {

    int insertWechatUser(WechatUser wechatUser);
    WechatUser selectByOpenId(String openId);
    int updateByCellphone(WechatUser wechatUser);
    int updateByOpenId(WechatUser wechatUser);
}
