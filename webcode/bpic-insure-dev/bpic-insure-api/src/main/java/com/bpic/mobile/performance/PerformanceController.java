package com.bpic.mobile.performance;


import com.alibaba.fastjson.JSON;
import com.bpic.common.core.controller.BaseController;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.SalesmanPerformance;
import com.bpic.common.core.domain.entity.ShareInformation;
import com.bpic.common.core.domain.entity.TempCdeSales;
import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.core.page.TableDataInfo;
import com.bpic.common.utils.StringUtils;
import com.bpic.framework.web.service.SysLoginService;
import com.bpic.framework.web.service.TokenService;
import com.bpic.mobile.mapper.SalesmanPerformanceMapper;
import com.bpic.mobile.service.SalesmanPerformanceService;
import com.bpic.mobile.domain.vo.VolumeVo;
import com.bpic.mobile.service.ShareInformationService;
import com.bpic.system.domain.vo.SysLoginUserVo;
import com.bpic.system.service.SysLoginUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 业务员业绩清单controller
 * */
@RestController
@RequestMapping("api/performance")
public class PerformanceController extends BaseController {

    public static final Logger log = LoggerFactory.getLogger(PerformanceController.class);

    @Resource
    SalesmanPerformanceService salesmanPerformanceService;

    @Autowired
    private ShareInformationService shareInformationService;

    @Autowired
    private SalesmanPerformanceMapper salesmanPerformanceMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginUserService sysLoginUserService;


    @PostMapping("query")
    public AjaxResult queryPerformance(@RequestBody HashMap<String, String> param){
        AjaxResult ajaxResult = null;
        if (param == null ){
            return ajaxResult = AjaxResult.error("参数不能为空!");
        }
        if(StringUtils.isEmpty(param.get("salesmanNo"))){
            LoginUser account = tokenService.getLoginUser();
            String tel = "13636396841";
            List<SalesmanPerformance> list = salesmanPerformanceService.queryByShareId(tel,param.get("name"), param.get("mark"), param.get("priceSorted"), param.get("timeSorted"));
            Map<String,Object> listMap=new HashMap<>();
            listMap.put("lists",list);
            return ajaxResult = AjaxResult.success("请求成功!", listMap);
        }

        log.info("============请求业务员清单信息======start=======salesmanNo:{}=======name:{}", param.get("salesmanNo"), param.get("name"));
        try{
            List<Map<String,Object>> mapList  = new ArrayList<>();
            Map<String,Object> map= new HashMap<>();
            if(param.get("isSales").equals("0")) {
                startPage();
                //0--本人 1--下线
                List<SalesmanPerformance> list = salesmanPerformanceService.querySalesmanPolicyList(param.get("salesmanNo"), param.get("name"), param.get("mark"), param.get("priceSorted"), param.get("timeSorted"), param.get("isSales"));
                log.info("============请求业务员清单信息======end=======result:{}=============", JSON.toJSONString(list));
                Map<String,Object> listMap=new HashMap<>();
                listMap.put("lists",list);
                return ajaxResult = AjaxResult.success("请求成功!", listMap);
            }else {
                startPage();
                List<SalesmanPerformance> list = salesmanPerformanceService.querySalesmanPolicyList(param.get("salesmanNo"), param.get("name"), param.get("mark"), param.get("priceSorted"), param.get("timeSorted"), param.get("isSales"));
                List<String> list1=new ArrayList<>();
                for (SalesmanPerformance salesmanPerformance : list) {
                    list1.add(salesmanPerformance.getOfflineName());
                }
                List<String> stringList = list1.stream().distinct().collect(Collectors.toList());
                Map<String,Object> listMap= null;
                for (String s : stringList) {
                    List<SalesmanPerformance> performanceList=new ArrayList<>();
                    for (SalesmanPerformance salesmanPerformance : list) {
                        if(s.equals(salesmanPerformance.getOfflineName())){
                            performanceList.add(salesmanPerformance);
                        }
                    }
                    listMap = new HashMap<>();
                    listMap.put("userIcon",s);
                    listMap.put("userList",performanceList);
                    mapList.add(listMap);
                }
                map.put("lists",mapList);
                TableDataInfo dataTable = getDataTable(list);
                map.put("total",dataTable.getTotal());
                return ajaxResult = AjaxResult.success("请求成功!", map);
            }

        }catch (Exception e){
            log.info("============请求业务员清单信息异常:{}======", e.getMessage());
            e.printStackTrace();
            return ajaxResult = AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询成交单数和金额
     * @param param
     * @return
     */

    @PostMapping("queryVolume")
    public AjaxResult queryVolume(@RequestBody HashMap<String, String> param){
        AjaxResult ajaxResult = null;
        if(StringUtils.isEmpty(param.get("salesmanNo"))){
            LoginUser account = tokenService.getLoginUser();
            String tel = "13636396841";
            VolumeVo volumeVo=new VolumeVo();
            VolumeVo vo = salesmanPerformanceMapper.queryVolumeByTel(tel, param.get("type"));
            int mark = salesmanPerformanceMapper.queryCountByTelMarkn(tel);
            int mark2 = salesmanPerformanceMapper.queryCountByTelMarky(tel);
            volumeVo.setMark(String.valueOf(mark));
            volumeVo.setMark2(String.valueOf(mark2));
            volumeVo.setAmount(vo.getAmount());
            log.info("============请求成交单数和金额信息======end=======result:{}=============", vo);
            return ajaxResult = AjaxResult.success("请求成功!", volumeVo);
        }
        if (param == null || StringUtils.isEmpty(param.get("salesmanNo")) || StringUtils.isEmpty(param.get("isSales"))
                || StringUtils.isEmpty(param.get("type"))){
            return ajaxResult = AjaxResult.error("salesmanNo或isSales参数不能为空!");
        }
        log.info("============请求成交单数和金额信息======start=======salesmanNo:{}=======isSales:{}", param.get("salesmanNo"), param.get("isSales"));
        try{
            VolumeVo volumeVo=new VolumeVo();
            VolumeVo vo = salesmanPerformanceService.queryVolume(param.get("salesmanNo"), param.get("isSales"),param.get("type"));
            int mark = salesmanPerformanceMapper.queryCountByMarkn(param.get("salesmanNo"), param.get("isSales"));
            int mark2 = salesmanPerformanceMapper.queryCountByMarky(param.get("salesmanNo"), param.get("isSales"));
            volumeVo.setMark(String.valueOf(mark));
            volumeVo.setMark2(String.valueOf(mark2));
            volumeVo.setAmount(vo.getAmount());
            log.info("============请求成交单数和金额信息======end=======result:{}=============", vo);
            return ajaxResult = AjaxResult.success("请求成功!", volumeVo);
        }catch (Exception e){
            log.info("============请求成交单数和金额信息:{}======", e.getMessage());
            e.printStackTrace();
            return ajaxResult = AjaxResult.error(e.getMessage());
        }
    }

}
