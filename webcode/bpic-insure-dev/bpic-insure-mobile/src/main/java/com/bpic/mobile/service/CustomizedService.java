package com.bpic.mobile.service;


import com.bpic.common.core.domain.entity.ShareInformation;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomizedService {


    Integer selectClickCount(String telPhone);

    void insertData(ShareInformation s);

    void updateTelPhone(ShareInformation s);

    void deleteData(ShareInformation s);

//    String selectShareId(ShareInformation s);
}
