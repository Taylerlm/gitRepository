package com.leyou.lmhitysu.item.goods.dao;

import com.github.pagehelper.Page;
import com.lmhitysu.leyou.item.model.Sku;
import com.lmhitysu.leyou.item.model.Spu;
import com.lmhitysu.leyou.item.model.SpuDetail;
import com.lmhitysu.leyou.item.model.Stock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IGoodsMapper {
    public Page<Spu> findByProperty(Map map);
    public void saveSpu(Spu spu);
    public void saveSpuDetail(SpuDetail spu);
    public void saveSku(Sku sku);
    public void saveSkuStock(Stock stock);
    List<SpuDetail> findSpuDetailByProperty (Map id);
    List<Sku> findSkuByProperty(Map map);
    List<Stock> findSkuStockByProperty(Map map);

}
