package com.leyou.lmhitysu.item.specification.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.lmhitysu.common.Exception.LyException;
import com.leyou.lmhitysu.common.Exception.MyException;
import com.leyou.lmhitysu.common.model.Specification;
import com.leyou.lmhitysu.item.specification.dao.ISpecificationMapper;
import com.lmhitysu.leyou.item.model.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("specificationService")
public class SpecificationServiceImpl implements ISpecificationService {
    @Autowired
    private ISpecificationMapper specificationMapper;
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public String getSpecificationByCategoryId(Long categoryId) {
        if(null == categoryId){
            throw new LyException("ly-item-service-"+ MyException.MY_EXCEPTION_NULL.getCode(),"查询规格信息中分类ｉｄ为空");
        }
        Map map = new HashMap<>();
        map.put("categoryId",categoryId);
        List<Specification> byProperty = this.specificationMapper.findByProperty(map);
        if(null != byProperty && byProperty.size()>0 && byProperty.size() == 1){
            return byProperty.get(0).getSpecifications();
        }
        return null;
    }

    @Override
    public List<SpecGroup> querySpecsByCid(Long id) {
        try {
            Map map = new HashMap<>();
            map.put("categoryId",id);
            List<SpecGroup> list = null;
            List<Specification> byProperty = this.specificationMapper.findByProperty(map);
            if(byProperty != null && byProperty.size()>0 ){
                Specification specification = byProperty.get(0);
                String specifications = specification.getSpecifications();
                list = mapper.readValue(specifications, new TypeReference<List<SpecGroup>>() {
                });
            }
            for(SpecGroup specGroup:list){
                specGroup.setCid(id);
            }
            return list;
        }catch (Exception e){
            return null;
        }

    }
}
