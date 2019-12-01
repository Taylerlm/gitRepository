package com.leyou.lmhitysu.item.category.service;

import com.lmhitysu.leyou.item.model.Category;

import java.util.List;

public interface ICategoryService {
    public List<Category> findCategoryById(Category category);
    public List<String> findCategoryNamesByIds(List<Long> cids);
}
