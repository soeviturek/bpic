package com.bpic.mobile.service.impl;

import com.bpic.common.core.domain.entity.SalesmanPerformance;
import com.bpic.common.core.domain.entity.ShareInformation;
import com.bpic.mobile.domain.vo.SalesmanPerformanceVo;
import com.bpic.mobile.domain.vo.VolumeVo;
import com.bpic.mobile.mapper.SalesmanPerformanceMapper;
import com.bpic.mobile.service.SalesmanPerformanceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SalesmanPerformanceServiceImpl implements SalesmanPerformanceService {

    @Autowired
    SalesmanPerformanceMapper salesmanPerformanceMapper;


    @Override
    public int addSalesmanPolicyInfo(SalesmanPerformance salesmanPerformance) {
        return 0;
    }

    @Override
    public List<SalesmanPerformance> querySalesmanPolicyList(String salesmanNo, String name, String mark, String priceSorted, String timeSorted,String isSales) {
        return salesmanPerformanceMapper.querySalesmanPolicyList(salesmanNo, name,mark,priceSorted,timeSorted,isSales);
    }

    @Override
    public VolumeVo queryVolume(String salesmanNo, String isSales,String type) {
        return salesmanPerformanceMapper.queryVolume(salesmanNo,isSales,type);
    }

    @Override
    public List<SalesmanPerformanceVo> queryVolumeById(String id) {
        return salesmanPerformanceMapper.queryVolumeById(id);
    }

    @Override
    public List<SalesmanPerformance> queryByShareId(String tel,String name,String mark,String priceSorted,
                                                    String timeSorted) {
        return salesmanPerformanceMapper.queryByShareId(tel, name, mark,priceSorted,
                 timeSorted);
    }


}
