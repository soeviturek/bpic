package com.bpic.system.service;

import com.alibaba.fastjson.JSONArray;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.ConfigSales;
import com.bpic.common.core.domain.entity.Product;
import com.bpic.common.core.domain.entity.ProductConfig;
import com.bpic.common.core.domain.entity.TempCdeSales;
import com.bpic.system.domain.vo.ProductConfigVo;
import com.bpic.system.domain.vo.SalesVo;
import com.bpic.system.domain.vo.TempCdeSalesVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ISysProductConfigService {
    List<ProductConfig> getProductConfigList(ProductConfigVo productConfig);

    ProductConfig findProductConfigById(String configCode);

    List<Product> findProductById(String productCode,String productName,String configCode);

    List<SalesVo> findSales(String sales, String salesName, String configCode);

    AjaxResult insertOrUpdate(ProductConfig config, List<TempCdeSales> salesList, ProductConfig productConfig, List<Product> productList);

    void updateStatus(String configCode);

    void deleteProductConfig(Long configCode);

    Set<String> selectByTel(String telPhone);

    List<TempCdeSalesVo> selectSales(String sales, String salesName,JSONArray sale);

    void deleteSales(String sales, String configCode);

    void insertSales(String configCode, String deptId, String flage, List<String> userCode);

    Set<Product> selectByNum(String saleNum);
}
