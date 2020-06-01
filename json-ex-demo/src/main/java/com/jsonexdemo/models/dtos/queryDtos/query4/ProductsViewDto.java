package com.jsonexdemo.models.dtos.queryDtos.query4;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ProductsViewDto {
    @Expose
    private String name;
    @Expose
    private BigDecimal price;

    public ProductsViewDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
