package com.leyou.lmhitysu.item.specification.service;

import com.lmhitysu.leyou.item.model.SpecGroup;

import java.util.List;

public interface ISpecificationService {
    public String getSpecificationByCategoryId(Long categoryId);
    public List<SpecGroup> querySpecsByCid(Long id);
}
