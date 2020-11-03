package com.bpic.mobile.mapper;


import com.bpic.common.core.domain.entity.SalesmanPerformance;
import com.bpic.common.core.domain.entity.ShareInformation;
import com.bpic.mobile.domain.vo.SalesmanPerformanceVo;
import com.bpic.mobile.domain.vo.VolumeVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 业务员清单信息Mapper对象
 * */

@Repository
public interface SalesmanPerformanceMapper{

    int addSalesmanPolicyInfo(SalesmanPerformance salesmanPerformance);

    List<SalesmanPerformance> querySalesmanPolicyList(@Param("salesmanNo") String salesmanNo, @Param("name") String name
                                                        ,@Param("mark")String mark, @Param("priceSorted")String priceSorted,
                                                      @Param("timeSorted")String timeSorted,@Param("isSales")String isSales);

    VolumeVo queryVolume(@Param("salesmanNo") String salesmanNo,@Param("isSales") String isSales,@Param("type") String type);

    VolumeVo queryVolumeByTel(@Param("telPhone") String telPhone,@Param("type") String type);

    int queryCountByMarky(@Param("salesmanNo") String salesmanNo,@Param("isSales") String isSales);

    int queryCountByMarkn(@Param("salesmanNo") String salesmanNo,@Param("isSales") String isSales);

    int queryCountByTelMarkn(@Param("telPhone") String telPhone);

    int queryCountByTelMarky(@Param("telPhone") String telPhone);

    List<SalesmanPerformanceVo> queryVolumeById(@Param("id") String id);

    VolumeVo queryVolumeByTelCount(@Param("telPhone") String telPhone);

    VolumeVo queryVolumeByEmpNoCount(@Param("salesmanNo") String salesmanNo);

    List<SalesmanPerformance> queryByShareId(@Param("telPhone") String telPhone ,@Param("name") String name,@Param("mark")String mark, @Param("priceSorted")String priceSorted,
                                             @Param("timeSorted")String timeSorted);
}
