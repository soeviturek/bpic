package com.bpic.mobile.mapper;

import com.bpic.common.core.domain.entity.ShareInformation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareInformationMapper {
    List<ShareInformation> selectByTel(@Param("telPhone")String telPhone,@Param("status") int status);

    void refreshClick(ShareInformation shareInformation);

    Integer selectClick(ShareInformation shareInformation);

    ShareInformation selectByProductId(String productId);

    void insertProductId(String productId);

    void insertShareInformation(ShareInformation shareInformation);

    Integer selectTotal(String telPhone);

    /**
     * 自定义
     * @param telPhone
     */
    Integer selectTotalAllReplicate(String telPhone);


    void deleteByTel(String telPhone);

    List<ShareInformation> selectByEmpNo(String salesmanNo);

    List<ShareInformation> selectBySupEmpNo(String salesmanNo);

    void insertByProductId(ShareInformation shareInfor);

    String selectVolume(@Param("id")String id);

    void updateIsShare(@Param("id") String id,@Param("isShare") String  isShare);
}
