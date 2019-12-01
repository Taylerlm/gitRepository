package com.leyou.lmhitysu.item.category.service;

import com.leyou.lmhitysu.item.category.dao.ICategoryMapper;
import com.lmhitysu.leyou.item.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "categoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryMapper categoryMapper;
    public List<Category> findCategoryById(Category category){
      return categoryMapper.findCategoryById(category);
    }

    @Override
    public List<String> findCategoryNamesByIds(List<Long> cids) {
        Map map = new HashMap();
        map.put("categoryIds",cids);
        return this.categoryMapper.qryCategoryName(map);
    }
}
