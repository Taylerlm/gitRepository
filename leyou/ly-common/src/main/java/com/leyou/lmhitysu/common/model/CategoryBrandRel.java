package com.leyou.lmhitysu.common.model;

import java.io.Serializable;

public class CategoryBrandRel implements Serializable {
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    private Long categoryId;
    private Long brandId;
}
