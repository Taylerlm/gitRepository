package com.leyou.lmhitysu.goods.service;

import com.leyou.lmhitysu.goods.clients.BrandClient;
import com.leyou.lmhitysu.goods.clients.CategoryClients;
import com.leyou.lmhitysu.goods.clients.GoodsClient;
import com.leyou.lmhitysu.goods.clients.SpecClient;
import com.lmhitysu.leyou.item.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service("goodsService")
public class GoodsServiceImpl implements IGoodsService {
    @Autowired
    private CategoryClients categoryClients;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecClient specClient;
    private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);
    @Override
    public Map<String, Object> loadModel(Long id) {
        try {
            // 查询spu
            Spu spu = this.goodsClient.getSpuById(id).getBody();

            // 查询spu详情
            SpuDetail spuDetail = this.goodsClient.getSpuDetail(id).getBody();

            // 查询sku
            List<Sku> skuList = this.goodsClient.getSkuList(id).getBody();

            // 查询品牌
            List<Brand> brands = this.brandClient.getBrandsByIds(Arrays.asList(spu.getBrandId())).getBody();

            // 查询分类
            List<Category> categories = getCategories(spu);

            // 查询组内参数
            List<SpecGroup> specGroups = this.specClient.querySpecsByCid(spu.getCid3()).getBody();

            // 查询所有特有规格参数
           // List<SpecParam> specParams = this.specClient.querySpecParam(null, spu.getCid3(), null, false);
            // 处理规格参数
           /* Map<Long, String> paramMap = new HashMap<>();
            specParams.forEach(param->{
                paramMap.put(param.getId(), param.getName());
            });*/

            Map<String, Object> map = new HashMap<>();
            map.put("spu", spu);
            map.put("spuDetail", spuDetail);
            map.put("skus", skuList);
            map.put("brand", brands.get(0));
            map.put("categories", categories);
            map.put("groups", specGroups);
            map.put("params", null);
            return map;
        } catch (Exception e) {
            logger.error("加载商品数据出错,spuId:{}", id, e);
        }
        return null;
    }
    private List<Category> getCategories(Spu spu) {
        try {
            List<String> names = this.categoryClients.findCategoryNamesByIds(
                    Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())).getBody();
            Category c1 = new Category();
            c1.setName(names.get(0));
            c1.setId(spu.getCid1());

            Category c2 = new Category();
            c2.setName(names.get(1));
            c2.setId(spu.getCid2());

            Category c3 = new Category();
            c3.setName(names.get(2));
            c3.setId(spu.getCid3());

            return Arrays.asList(c1, c2, c3);
        } catch (Exception e) {
            logger.error("查询商品分类出错，spuId：{}", spu.getId(), e);
        }
        return null;
    }
}
