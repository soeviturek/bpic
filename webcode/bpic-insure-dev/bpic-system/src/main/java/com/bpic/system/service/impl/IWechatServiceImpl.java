package com.bpic.system.service.impl;

import com.bpic.system.domain.WechatUser;
import com.bpic.system.mapper.WechatMapper;
import com.bpic.system.service.IWechatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
@Service
@Transactional
public class IWechatServiceImpl implements IWechatService {

    @Resource
    WechatMapper wechatMapper;

    @Override
    public int insertWechatUser(WechatUser wechatUser) {
        return wechatMapper.insertWechatUser(wechatUser);
    }

    @Override
    public WechatUser selectByOpenId(String openId) {
        return wechatMapper.selectByOpenId(openId);
    }

    @Override
    public int updateByCellphone(WechatUser wechatUser) {
        return wechatMapper.updateByCellphone(wechatUser);
    }

    @Override
    public int updateByOpenId(WechatUser wechatUser) {
        return wechatMapper.updateByOpenId(wechatUser);
    }


}
