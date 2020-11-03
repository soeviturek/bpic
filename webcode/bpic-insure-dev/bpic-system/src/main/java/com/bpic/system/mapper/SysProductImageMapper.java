package com.bpic.system.mapper;

import com.bpic.common.core.domain.entity.ProductImage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysProductImageMapper {
    List<ProductImage> selectByConditions(@Param("productCode") String productCode,@Param("productName") String productName);

    void updateProductImage(@Param("id") String id,@Param("url") String url);

    void insertProductImage(ProductImage productImage);

    ProductImage selectByProductCode(@Param("id") String id);
}
