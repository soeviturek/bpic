package com.bpic.system.service;

import com.bpic.common.core.domain.entity.ProductImage;

import java.util.List;
import java.util.Map;

public interface ISysProductImageService {
    List<ProductImage> selectByConditions(Map<String, String> map);

    void updateProductImage(String id, String url);

    void insertProductImage(ProductImage productImage);

    ProductImage selectByProductCode(String id);
}
