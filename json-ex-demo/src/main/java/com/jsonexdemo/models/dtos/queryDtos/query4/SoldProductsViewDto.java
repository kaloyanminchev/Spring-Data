package com.jsonexdemo.models.dtos.queryDtos.query4;

import com.google.gson.annotations.Expose;

import java.util.List;

public class SoldProductsViewDto {
    @Expose
    private int count;
    @Expose
    private List<ProductsViewDto> products;

    public SoldProductsViewDto() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ProductsViewDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsViewDto> products) {
        this.products = products;
    }
}
