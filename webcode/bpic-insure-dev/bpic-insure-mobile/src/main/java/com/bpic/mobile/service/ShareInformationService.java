package com.bpic.mobile.service;

import com.bpic.common.core.domain.entity.ShareInformation;

import java.util.List;

public interface ShareInformationService {
    List<ShareInformation> selectByTel(String telPhone,int status);

    void refreshClick(ShareInformation shareInformation);

    Integer selectClick(ShareInformation shareInformation);

    ShareInformation selectByProductId(String productId);


    void insertShareInformation(ShareInformation shareInformation);

    Integer selectTotal(String telPhone);

    /**
     * 自定义
     */
    Integer selectTotalAllReplicate(String telPhone);

    void deleteByTel(String telPhone);

    List<ShareInformation> selectByEmpNo(String salesmanNo);

    List<ShareInformation> selectBySupEmpNo(String salesmanNo);

    void insertByProductId(ShareInformation shareInfor);

    void updateIsShare(String id,String isShare);
}
