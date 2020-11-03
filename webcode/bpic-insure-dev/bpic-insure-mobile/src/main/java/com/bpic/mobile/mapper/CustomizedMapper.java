package com.bpic.mobile.mapper;

import com.bpic.common.core.domain.entity.ShareInformation;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomizedMapper {

    Integer selectClickCount(String telPhone);

    void insertData(ShareInformation s);

    void updateTelPhone(ShareInformation s);

    void deleteData(ShareInformation s);

//    String selectSharedId(ShareInformation s);
}
