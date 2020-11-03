package com.bpic.mobile.service.impl;

import com.bpic.common.core.domain.entity.ShareInformation;
import com.bpic.mobile.mapper.CustomizedMapper;
import com.bpic.mobile.service.CustomizedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomizedServiceImpl implements CustomizedService {

    @Autowired
    private CustomizedMapper customizedMapper;

    /**
     * 自定义2
     * @param telPhone
     * @return
     */
    @Override
    public Integer selectClickCount(String telPhone) {
        return customizedMapper.selectClickCount(telPhone);
    }

    @Override
    public void insertData(ShareInformation s) {
        customizedMapper.insertData(s);
    }

    @Override
    public void updateTelPhone(ShareInformation s) {
        customizedMapper.updateTelPhone(s);
    }

    @Override
    public void deleteData(ShareInformation s) {
        customizedMapper.deleteData(s);
    }

//    @Override
//    public String selectShareId(ShareInformation s) {
//        return null;
//    }

}
