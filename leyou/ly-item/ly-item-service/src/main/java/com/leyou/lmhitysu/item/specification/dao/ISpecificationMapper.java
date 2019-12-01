package com.leyou.lmhitysu.item.specification.dao;

import com.leyou.lmhitysu.common.model.Specification;
import com.lmhitysu.leyou.item.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ISpecificationMapper {
    public List<Specification> findByProperty(Map spec);
}
