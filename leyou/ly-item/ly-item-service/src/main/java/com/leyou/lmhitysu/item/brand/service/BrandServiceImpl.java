package com.leyou.lmhitysu.item.brand.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.lmhitysu.common.Exception.LyException;
import com.leyou.lmhitysu.common.Exception.MyException;
import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.common.model.CategoryBrandRel;
import com.leyou.lmhitysu.item.brand.dao.IBrandMapper;
import com.leyou.lmhitysu.item.category.dao.ICategoryMapper;
import com.lmhitysu.leyou.item.model.Brand;
import com.lmhitysu.leyou.item.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class BrandServiceImpl implements IBrandService {
    private static final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);
    @Autowired
    private IBrandMapper brandMapper;
    @Autowired
    private ICategoryMapper categoryMapper;
    @Override
    public PageResult findBrandPages(Map params) {
        //开始分页
        PageHelper.startPage((int)params.get("page"),(int)params.get("rows"));
        Page<Brand> brandPages = brandMapper.findBrandPages(params);
        return new PageResult<Brand>(brandPages.getTotal(),brandPages);
    }

    @Override
    @Transactional
    public Map save(Brand brand, List<Long> cids) {
        Map result = new HashMap();
        try{

            List<Map<String,Object>> categoryBrandList = new ArrayList<>();
            this.brandMapper.saveBrand(brand);
            System.out.println(brand.getId());
            Long brand_id = brand.getId();
            if(null != brand.getId()){
                for(Long cid : cids){
                    Map categoryBrand = new HashMap();
                    categoryBrand.put("category_id",cid);
                    categoryBrand.put("brand_id",brand.getId());
                    categoryBrandList.add(categoryBrand);
                }
                this.brandMapper.saveCategoryBrand(categoryBrandList);
            }else{
                throw new LyException(MyException.MY_EXCEPTION_NULL);
            }

            result.put("code",200);
            result.put("msg","success");

        }catch(Exception e){
            logger.error(e.getMessage());
            result.put("code",500);
            result.put("msg","failure");
            throw new LyException(MyException.MY_EXCEPTION_INSERT);
            //throw new LyException(MyException.MY_EXCEPTION_INSERT);

        }
        return result;
    }

    @Override
    public List<Category> getCategoriesById(Long bid) {
        if(null == bid){
            throw new LyException("ly-item-brand"+MyException.MY_EXCEPTION_NULL,"品牌管理中修改品牌信息时，所传的品牌ｉｄ为空");
        }
        Map map = new HashMap();
        map.put("brandId",bid);
        List<CategoryBrandRel> list = this.brandMapper.findCategoryByBrandIdCategoryId(map);
        List<Long> categories = null;
        if(list != null && list.size()>0){
            categories = new ArrayList<>();
            for(CategoryBrandRel categoryBrandRel : list){
                categories.add(categoryBrandRel.getCategoryId());
            }
        }
        List<Category> categoryList = null;
        if(categories != null){
            Map mapC = new HashMap();
            mapC.put("categoryIds",categories);
            categoryList = categoryMapper.findByProperty(mapC);
        }
        return categoryList;
    }

    @Override
    public List<Brand> getBrandByCid(Long cid) {
        return this.brandMapper.findBrandByCid(cid);
    }

    @Override
    public String findBrandNameById(Long id) {
        Map param = new HashMap();
        param.put("id",id);
        List<Brand> list = this.brandMapper.findByProperty(param);
        if(list != null){
            return list.get(0).getName();
        }
        return null;
    }

    @Override
    public List<Brand> findBrandsByIds(List<Long> ids) {
        Map param = new HashMap();
        param.put("ids",ids);
        List<Brand> list = this.brandMapper.findByProperty(param);
        return list;
    }
}
