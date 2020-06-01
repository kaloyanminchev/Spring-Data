package com.jsonexdemo.services;

import com.jsonexdemo.models.dtos.queryDtos.query1.ProductsInRangeViewDto;
import com.jsonexdemo.models.dtos.seedDtos.ProductSeedDto;

import java.util.List;

public interface ProductService {
    void seedProducts(ProductSeedDto[] productSeedDtos);

    List<ProductsInRangeViewDto> getAllProductsInRange();
}
