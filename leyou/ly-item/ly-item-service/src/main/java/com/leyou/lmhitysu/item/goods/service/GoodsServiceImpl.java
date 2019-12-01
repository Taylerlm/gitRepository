package com.leyou.lmhitysu.item.goods.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageInterceptor;
import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.item.brand.dao.IBrandMapper;
import com.leyou.lmhitysu.item.category.dao.ICategoryMapper;
import com.leyou.lmhitysu.item.goods.dao.IGoodsMapper;
import com.leyou.lmhitysu.item.goods.service.IGoodsService;
import com.lmhitysu.leyou.item.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.ListUI;
import java.util.*;

@Service("goodsService")
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private IGoodsMapper goodsMapper;
    @Autowired
    private ICategoryMapper categoryMapper;
    @Autowired
    private IBrandMapper brandMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);
    @Override
    public PageResult<SpuVo> querySpuByPageAndSort(Map param) {
        //分页查询可以通过PageHelper.startPage
        PageHelper.startPage((int)param.get("page"),(int)param.get("rows"));
        Page<Spu> pageSpu = this.goodsMapper.findByProperty(param);
        List<SpuVo> spuVoList = new ArrayList<>();
        if(pageSpu != null && pageSpu.size() > 0){
            //查询该商品的分类名称和品牌名称
            //分类的所有的名称都要有
            for(Spu spu:pageSpu){
                SpuVo spuVo = new SpuVo();
                //复制属性
                BeanUtils.copyProperties(spu,spuVo);
                //查询分类名称
                List<Long> cids = new ArrayList<>();
                cids.add(spu.getCid1());
                cids.add(spu.getCid2());
                cids.add(spu.getCid3());
                Map categoryMap = new HashMap();
                categoryMap.put("categoryIds",cids);
                List<String> byProperty = this.categoryMapper.qryCategoryName(categoryMap);

                spuVo.setCname(StringUtils.join(byProperty,"/"));
                //查询品牌名称
                Map brandMap = new HashMap();
                brandMap.put("id",spu.getBrandId());
                List<Brand> brandList = this.brandMapper.findByProperty(brandMap);
                if(null != brandList){
                    spuVo.setBname(brandList.get(0).getName());
                }
                spuVoList.add(spuVo);
            }

        }else{
            return null;
        }

        return new PageResult<>(pageSpu.getTotal(),spuVoList);
    }
    @Transactional
    @Override
    public void save(SpuVo spu) {
        //这里要一个表一个表的插入，事物也要控制好
        spu.setSaleable(new Integer(1));
        spu.setValid(new Integer(1));
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        this.goodsMapper.saveSpu(spu);
        spu.getSpuDetail().setSpuId(spu.getId());
        this.goodsMapper.saveSpuDetail(spu.getSpuDetail());
        saveSkuAndStock(spu.getSkuList(),spu.getId());
        sendMessage(spu.getId(),"insert");
    }

    @Override
    public SpuDetail getSpuDetail(Long id) {
        Map map = new HashMap();
        map.put("id",id);
        List<SpuDetail> listDetail = this.goodsMapper.findSpuDetailByProperty(map);
        if(null != listDetail && listDetail.size()>0){
            return listDetail.get(0);
        }
        return null;
    }

    @Override
    public List<Sku> getSkuList(Long id) {
        Map map = new HashMap();
        map.put("spuId",id);
        List<Sku> skus = this.goodsMapper.findSkuByProperty(map);
        if(null != skus && skus.size()>0){
            for(Sku sku:skus){
                map.put("skuId",sku.getId());
                List<Stock> skuStockByProperty = this.goodsMapper.findSkuStockByProperty(map);
                sku.setStock(skuStockByProperty.get(0).getStock());
            }
            return skus;
        }
        return null;
    }

    @Override
    public Spu getSpuById(Long id) {
        Map param = new HashMap();
        param.put("id",id);
        List<Spu> byProperty = this.goodsMapper.findByProperty(param);
        if(byProperty != null && byProperty.size()>0){
            return byProperty.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Sku getSkuById(Long id) {
        Map param = new HashMap();
        param.put("id",id);
        List<Sku> skuByProperty = this.goodsMapper.findSkuByProperty(param);
        if(skuByProperty != null && skuByProperty.size()>0){
            return skuByProperty.get(0);
        }
        return null;
    }

    private void saveSkuAndStock(List<Sku> skus, Long spuId){
        for(Sku sku:skus){
            if(!sku.getEnable()){
                continue;
            }
            sku.setSpuId(spuId);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.goodsMapper.saveSku(sku);
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.goodsMapper.saveSkuStock(stock);
        }
    }
    private void sendMessage(Long id, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            logger.error("{}商品消息发送异常，商品id：{}", type, id, e);
        }
    }
}
