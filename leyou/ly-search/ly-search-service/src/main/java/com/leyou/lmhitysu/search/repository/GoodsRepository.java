package com.leyou.lmhitysu.search.repository;

import com.leyou.lmhitysu.search.model.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
