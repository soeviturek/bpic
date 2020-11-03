package com.bpic.mobile.service;


import com.bpic.common.core.domain.entity.SalesmanPerformance;
import com.bpic.common.core.domain.entity.ShareInformation;
import com.bpic.mobile.domain.vo.SalesmanPerformanceVo;
import com.bpic.mobile.domain.vo.VolumeVo;

import java.util.List;

/**
 * 业务员业绩清单service
 * */
public interface SalesmanPerformanceService {

    int addSalesmanPolicyInfo(SalesmanPerformance salesmanPerformance);

    List<SalesmanPerformance> querySalesmanPolicyList(String salesmanNo, String name, String mark, String priceSorted, String timeSorted,String isSales);

    VolumeVo queryVolume(String salesmanNo, String isSales,String type);

    List<SalesmanPerformanceVo> queryVolumeById(String id);

    List<SalesmanPerformance> queryByShareId(String tel,String name,String mark,String priceSorted,
                                             String timeSorted);

}
