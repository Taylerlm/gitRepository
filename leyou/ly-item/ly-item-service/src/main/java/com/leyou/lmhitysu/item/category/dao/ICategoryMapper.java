package com.leyou.lmhitysu.item.category.dao;

import com.lmhitysu.leyou.item.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ICategoryMapper {
    public List<Category> findCategoryById(Category category);
    public List<Category> findByProperty(Map category);
    public List<String> qryCategoryName(Map map);
}
