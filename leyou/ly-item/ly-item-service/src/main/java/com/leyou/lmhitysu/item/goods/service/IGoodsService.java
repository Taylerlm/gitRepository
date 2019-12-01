package com.leyou.lmhitysu.item.goods.service;

import com.leyou.lmhitysu.common.PageResult;
import com.lmhitysu.leyou.item.model.Sku;
import com.lmhitysu.leyou.item.model.Spu;
import com.lmhitysu.leyou.item.model.SpuDetail;
import com.lmhitysu.leyou.item.model.SpuVo;

import java.util.List;
import java.util.Map;

public interface IGoodsService {
    PageResult<SpuVo> querySpuByPageAndSort(Map param);

    void save(SpuVo spu);

    SpuDetail getSpuDetail(Long id);

    List<Sku> getSkuList(Long id);

    Spu getSpuById(Long id);

    Sku getSkuById(Long id);
}