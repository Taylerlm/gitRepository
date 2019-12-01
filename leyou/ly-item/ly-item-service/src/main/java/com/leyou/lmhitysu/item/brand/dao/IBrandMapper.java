package com.leyou.lmhitysu.item.brand.dao;

import com.github.pagehelper.Page;
import com.leyou.lmhitysu.common.model.CategoryBrandRel;
import com.lmhitysu.leyou.item.model.Brand;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IBrandMapper {
    public Page<Brand> findBrandPages(Map req);

    public void saveBrand(Brand brand);

    public void saveCategoryBrand(List<Map<String,Object>> categoryBrandList);

    public List<Brand> findByProperty(Map map);

    public List<CategoryBrandRel> findCategoryByBrandIdCategoryId (Map map);

    public List<Brand> findBrandByCid (Long cid);

}
