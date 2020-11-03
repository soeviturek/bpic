package com.bpic.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bpic.common.constant.HttpStatus;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.*;
import com.bpic.common.core.redis.RedisCache;
import com.bpic.common.utils.StringUtils;
import com.bpic.system.domain.vo.ProductConfigVo;
import com.bpic.system.domain.vo.SalesVo;
import com.bpic.system.domain.vo.SysLoginUserVo;
import com.bpic.system.domain.vo.TempCdeSalesVo;
import com.bpic.system.mapper.SysDeptMapper;
import com.bpic.system.mapper.SysProductConfigMapper;
import com.bpic.system.service.ISysDeptService;
import com.bpic.system.service.ISysProductConfigService;
import com.bpic.system.service.ISysUserService;
import com.bpic.system.service.SysLoginUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysProductConfigServiceImpl implements ISysProductConfigService {
    @Autowired
    private SysProductConfigMapper productConfigMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SysLoginUserService sysLoginUserService;

    @Autowired
    private ISysDeptService sysDeptService;


    public static final Logger log = LoggerFactory.getLogger(SysProductConfigServiceImpl.class);

    @Override
    public List<ProductConfig> getProductConfigList(ProductConfigVo productConfig) {
        return productConfigMapper.getProductConfigList(productConfig);
    }

    @Override
    public ProductConfig findProductConfigById(String configCode) {
        return productConfigMapper.findProductConfigById(configCode);
    }

    @Override
    public List<Product> findProductById(String productCode,String productName, String configCode) {
        return productConfigMapper.findProductById(productCode,productName,configCode);
    }

    @Override
    public List<SalesVo> findSales(String sales, String salesName, String configCode) {
        List<SalesVo> salesVoList = productConfigMapper.findSales(sales, salesName, configCode);
        return salesVoList;
    }

    @Override
    public AjaxResult insertOrUpdate(ProductConfig config, List<TempCdeSales> salesList, ProductConfig productConfig, List<Product> productList) {
        if (config != null) {
            productConfigMapper.deleteProductConfig(productConfig.getConfigCode());
//                productConfigMapper.deleteSales(productConfig.getConfigCode());
            productConfigMapper.deleteProduct(productConfig.getConfigCode());
        }
        productConfigMapper.insertProductConfig(productConfig);
//            if(!salesList.isEmpty()){
//                List<ConfigSales> configSalesList=new ArrayList<>();
//                for (TempCdeSales sales : salesList) {
//                    ConfigSales configSales=new ConfigSales();
//                    configSales.setConfigCode(productConfig.getConfigCode());
//                    configSales.setSales(sales.getC_emp_cde());
//                    StringBuffer sb=new StringBuffer();
//                    for (String code : sales.getParentDeptCode()) {
//                        sb.append(code).append(",");
//                    }
//                    configSales.setDeptName(sb.toString());
//                    configSales.setDeptCode(sales.getC_dpt_cde());
//                    configSalesList.add(configSales);
//                }
//                productConfigMapper.insertSales(configSalesList);
//            }
        if (!productList.isEmpty()) {
            for (Product product : productList) {
                product.setConfigCode(productConfig.getConfigCode());
            }
            productConfigMapper.insertProduct(productList);
        }
        return AjaxResult.success("添加配置成功");
    }

    @Override
    public void updateStatus(String configCode) {
        productConfigMapper.updateStatus(configCode);
    }

    @Override
    public void deleteProductConfig(Long configCode) {
        productConfigMapper.deleteProductConfig(configCode);
        productConfigMapper.deleteSales(configCode);
        productConfigMapper.deleteProduct(configCode);
    }

    @Override
    public Set<String> selectByTel(String telPhone) {
        SysLoginUserVo sysLoginUserVo = sysLoginUserService.selectByCellphone(telPhone);
        List<Product> productList=new ArrayList<>();
        if(sysLoginUserVo!=null&&!StringUtils.isEmpty(sysLoginUserVo.getC_emp_cde2())){
            productList = productConfigMapper.selectProductList(sysLoginUserVo.getC_emp_cde2());
        }else {
            productList = productConfigMapper.selectProductList(sysLoginUserVo.getC_emp_cde());
        }
        List<String> productCodeList=new ArrayList<>();
        Set<String> result = new HashSet<>();
        if(!productList.isEmpty()){
            for (Product product : productList) {
                String productCode = product.getProductCode();
                productCodeList.add(productCode);
            }
            result = new HashSet<>(productCodeList);
        }
        return result;
    }


    @Override
    public List<TempCdeSalesVo> selectSales(String sales, String salesName,JSONArray sale) {
        List<TempCdeSalesVo> tempCdeSalesVoList = productConfigMapper.selectSales(sales,salesName,sale);
        List<TdepartmentSales> tdepartmentSales = sysDeptMapper.selectOwnDeptList();
        for (TempCdeSalesVo tempCdeSalesVo : tempCdeSalesVoList) {
            String parentDeptName = sysUserService.getParentDeptName(tdepartmentSales, new StringBuffer(), tempCdeSalesVo.getC_dpt_cde());
            tempCdeSalesVo.setParentDeptCode(parentDeptName);
        }
        return tempCdeSalesVoList;
    }

    @Override
    public void deleteSales(String sales, String configCode) {
        Integer idByCode = productConfigMapper.findIdByCode(sales, configCode);
        productConfigMapper.deleteById(idByCode);
    }

    @Override
    @Transactional
    public void insertSales(String configCode, String deptId, String flage, List<String> userCode) {
        List<String> list = null;
        List<String> deptCodeList = sysDeptService.selectDeptCodeList(deptId);
        List<TempCdeSalesVo> tempCdeSalesVoList = null;
        if(flage.equals("0")){
            if(userCode.size()==0 || userCode==null){
                list = deptCodeList;
                tempCdeSalesVoList = sysUserService.selectByList(list);
            }else {
                list = userCode;
                tempCdeSalesVoList = sysUserService.selectByUserList(list);
            }
        }else if(flage.equals("1")){
            //去除所选的人员
            List<TempCdeSalesVo> tempCdeSalesVoList1 = sysUserService.selectByList(deptCodeList);
            for (String s : userCode) {
                for (TempCdeSalesVo tempCdeSalesVo : tempCdeSalesVoList1) {
                    if (tempCdeSalesVo.getC_emp_cde().equals(s)) {
                        tempCdeSalesVoList1.remove(tempCdeSalesVo);
                        break;
                    }
                }
            }
//            List<TempCdeSalesVo> tempCdeSalesVoList2 = sysUserService.selectByUserList(userCode);
//            tempCdeSalesVoList1.removeAll(tempCdeSalesVoList2);
            tempCdeSalesVoList=tempCdeSalesVoList1;
        }
        List<TdepartmentSales> tdepartmentSales = sysDeptMapper.selectOwnDeptList();
        List<ConfigSales> configSalesList=new ArrayList<>();
        for(TempCdeSalesVo tempCdeSalesVo:tempCdeSalesVoList){
           ConfigSales configSales=new ConfigSales();
            String parentDeptName = sysUserService.getParentDeptName(tdepartmentSales, new StringBuffer(), tempCdeSalesVo.getC_dpt_cde());
            configSales.setDeptCode(tempCdeSalesVo.getC_dpt_cde());
            configSales.setDeptName(parentDeptName);
            configSales.setConfigCode(Long.parseLong(configCode));
            configSales.setSales(tempCdeSalesVo.getC_emp_cde());
            configSalesList.add(configSales);
        }
        if(!configSalesList.isEmpty()){
            productConfigMapper.deleteSales(Long.parseLong(configCode));
            productConfigMapper.insertSales(configSalesList);
        }
    }

    @Override
    public Set<Product> selectByNum(String saleNum) {
        List<Product> productList = productConfigMapper.selectProductList(saleNum);
        Set<Product> result = new HashSet<>();
        if(!productList.isEmpty()){
            result = new HashSet<>(productList);
        }
       return result;
    }

    @Scheduled(cron = "0 0 9 1/1 * ? ")
    public void scheduled() throws ParseException {
        log.info("产品配置过期时间定时任务==========start================开飞机了");
        List<ProductConfig> productConfig = productConfigMapper.findProductConfig();
        List<Long> productConfigList =new ArrayList<>();
        for (ProductConfig config : productConfig) {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parse = simpleDateFormat.parse(config.getAuthEndTime());
            if (parse.before(new Date())&&config.getStatus()==1) {
               productConfigList.add(config.getConfigCode());
            }
        }
        log.info("参数打印============================,{}",productConfigList);
        if(!productConfigList.isEmpty()){
            try {
                productConfigMapper.updateStatusByList(productConfigList);
            }catch (Exception e){
                log.error("产品配置过期时间定时任务出错==========error================坠机....");
            }
        }
        log.info("产品配置过期时间定时任务==========end================飞机降落");
    }
}
