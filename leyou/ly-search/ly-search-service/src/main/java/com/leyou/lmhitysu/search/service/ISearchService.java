package com.leyou.lmhitysu.search.service;

import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.search.model.Goods;
import com.leyou.lmhitysu.search.model.SearchRequest;
import com.lmhitysu.leyou.item.model.Spu;

import java.io.IOException;
import java.util.List;

public interface ISearchService {
    public Goods buildGoods(Spu spu) throws Exception;
    public PageResult<Goods> searchGoods(SearchRequest searchRequest);
    public void createIndex(Long id) throws Exception;
    public void deleteIndex(Long id) ;
}
