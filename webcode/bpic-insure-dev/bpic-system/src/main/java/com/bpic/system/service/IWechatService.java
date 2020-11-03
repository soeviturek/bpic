package com.bpic.system.service;

import com.bpic.system.domain.WechatUser;

public interface IWechatService {

    int insertWechatUser(WechatUser wechatUser);
    WechatUser selectByOpenId(String openId);
    int updateByCellphone(WechatUser wechatUser);
    int updateByOpenId(WechatUser wechatUser);
}
