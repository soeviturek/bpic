package com.bpic.system.domain.vo;

import com.bpic.common.core.domain.entity.ConfigSales;
import com.bpic.common.core.domain.entity.Product;
import com.bpic.common.core.domain.entity.ProductConfig;
import com.bpic.common.core.domain.entity.TempCdeSales;
import lombok.Data;

import java.util.List;
@Data
public class ProductSalesConfigVo {
    private List<TempCdeSales> sales;
    private ProductConfig productConfig;
    private List<Product> product;
    private String flage;
}
