package com.bpic.system.mapper;

import com.alibaba.fastjson.JSONArray;
import com.bpic.common.core.domain.entity.ConfigSales;
import com.bpic.common.core.domain.entity.Product;
import com.bpic.common.core.domain.entity.ProductConfig;
import com.bpic.system.domain.vo.ProductConfigVo;
import com.bpic.system.domain.vo.SalesVo;
import com.bpic.system.domain.vo.TempCdeSalesVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysProductConfigMapper {

    /**
     * 查询产品配置集合
     */
    List<ProductConfig> getProductConfigList(ProductConfigVo productConfig);

    /**
     * 查询产品配置信息
     * @param configCode
     * @return
     */
    ProductConfig findProductConfigById(String configCode);

    List<Product> findProductById(@Param("productCode") String productCode,@Param("productName") String productName, @Param("configCode") String configCode);

    List<SalesVo> findSales(@Param("sales") String sales,@Param("salesName") String salesName,@Param("configCode") String configCode);

    void insertSales(@Param("salesList") List<ConfigSales> salesList);

    void insertProductConfig(ProductConfig productConfig);

    void insertProduct(@Param("productList") List<Product> productList);

    void deleteProductConfig(Long configCode);

    void deleteSales(Long configCode);

    void deleteProduct(Long configCode);

    void updateStatus(String configCode);

    List<Product> selectProductList(@Param("saleCode") String saleCode);

    List<TempCdeSalesVo> selectSales(@Param("sales") String sales,@Param("salesName") String salesName,@Param("split") JSONArray split);

    Integer findIdByCode(@Param("sales")String sales,@Param("configCode") String configCode);

    void deleteById(@Param("id")int id);

    List<ProductConfig> findProductConfig();

    void updateStatusByList(@Param("list")List<Long> productConfigList);
}
