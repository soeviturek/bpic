package com.bpic.mobile.service.impl;

import com.bpic.common.core.domain.entity.ShareInformation;
import com.bpic.mobile.mapper.ShareInformationMapper;
import com.bpic.mobile.service.ShareInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareInformationServiceImpl implements ShareInformationService {

    @Autowired
    private ShareInformationMapper shareInformationMapper;

    @Override
    public List<ShareInformation> selectByTel(String telPhone,int status) {
        return shareInformationMapper.selectByTel(telPhone,status);
    }

    @Override
    public void refreshClick(ShareInformation shareInformation) {
        shareInformationMapper.refreshClick(shareInformation);
    }

    @Override
    public Integer selectClick(ShareInformation shareInformation) {
        return shareInformationMapper.selectClick(shareInformation);
    }

    @Override
    public ShareInformation selectByProductId(String productId) {
        return shareInformationMapper.selectByProductId(productId);
    }

    @Override
    public void insertShareInformation(ShareInformation shareInformation) {
        shareInformationMapper.insertShareInformation(shareInformation);
    }

    @Override
    public Integer selectTotal(String telPhone) {
        return shareInformationMapper.selectTotal(telPhone);
    }

    /**
     * 自定义
     * @param telPhone
     * @return
     */
    @Override
    public Integer selectTotalAllReplicate(String telPhone) {
        return shareInformationMapper.selectTotalAllReplicate(telPhone);
    }

    @Override
    public void deleteByTel(String telPhone) {
        shareInformationMapper.deleteByTel(telPhone);
    }

    @Override
    public List<ShareInformation> selectByEmpNo(String salesmanNo) {
        return shareInformationMapper.selectByEmpNo(salesmanNo);
    }

    @Override
    public List<ShareInformation> selectBySupEmpNo(String salesmanNo) {
        return shareInformationMapper.selectBySupEmpNo(salesmanNo);
    }

    @Override
    public void insertByProductId(ShareInformation shareInfor) {
        shareInformationMapper.insertByProductId(shareInfor);
    }

    @Override
    public void updateIsShare(String id,String isShare) {
        if(isShare.equals("1")){
            isShare="0";
        }else if(isShare.equals("0")){
            isShare="1";
        }
        shareInformationMapper.updateIsShare(id,isShare);
    }
}
