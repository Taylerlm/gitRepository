package com.leyou.lmhitysu.item.brand.service;

import com.leyou.lmhitysu.common.PageResult;
import com.lmhitysu.leyou.item.model.Brand;
import com.lmhitysu.leyou.item.model.Category;

import java.util.List;
import java.util.Map;

public interface IBrandService {
    public PageResult findBrandPages(Map params);

    public Map save(Brand brand, List<Long> cids);

    public List<Category> getCategoriesById (Long bid);
    public List<Brand> getBrandByCid (Long cid);

    String  findBrandNameById(Long id);

    public List<Brand> findBrandsByIds (List<Long> ids);
}
