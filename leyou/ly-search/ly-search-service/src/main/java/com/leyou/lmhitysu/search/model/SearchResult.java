package com.leyou.lmhitysu.search.model;

import com.leyou.lmhitysu.common.PageResult;
import com.lmhitysu.leyou.item.model.Brand;
import com.lmhitysu.leyou.item.model.Category;

import java.util.List;
import java.util.Map;

public class SearchResult extends PageResult {
    private List<Category> categoryList;

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

    private List<Map<String,Object>> specs;//规格参数过滤条件
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    private List<Brand> brandList;
    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories,
                        List<Brand> brands,List<Map<String,Object>> specs){
        super(total,items,totalPage);
        this.categoryList = categories;
        this.brandList = brands;
        this.specs = specs;
    }
}
