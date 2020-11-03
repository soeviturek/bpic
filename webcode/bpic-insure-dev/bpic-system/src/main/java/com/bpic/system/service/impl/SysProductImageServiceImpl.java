package com.bpic.system.service.impl;

import com.bpic.common.core.domain.entity.ProductImage;
import com.bpic.system.mapper.SysProductImageMapper;
import com.bpic.system.service.ISysProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysProductImageServiceImpl implements ISysProductImageService {

    @Autowired
    private SysProductImageMapper sysProductImageMapper;

    @Override
    public List<ProductImage> selectByConditions(Map<String, String> map) {
        return  sysProductImageMapper.selectByConditions(map.get("productCode"),map.get("productName"));
    }

    @Override
    public void updateProductImage(String id, String url) {
        sysProductImageMapper.updateProductImage(id,url);
    }

    @Override
    public void insertProductImage(ProductImage productImage) {
        sysProductImageMapper.insertProductImage(productImage);
    }

    @Override
    public ProductImage selectByProductCode(String id) {
        return sysProductImageMapper.selectByProductCode(id);
    }
}
