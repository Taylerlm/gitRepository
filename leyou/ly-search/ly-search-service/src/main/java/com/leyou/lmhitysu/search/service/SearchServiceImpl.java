package com.leyou.lmhitysu.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.search.clients.BrandClient;
import com.leyou.lmhitysu.search.clients.CategoryClients;
import com.leyou.lmhitysu.search.clients.GoodsClient;
import com.leyou.lmhitysu.search.clients.SpecClient;
import com.leyou.lmhitysu.search.model.Goods;
import com.leyou.lmhitysu.search.model.SearchRequest;
import com.leyou.lmhitysu.search.model.SearchResult;
import com.leyou.lmhitysu.search.repository.GoodsRepository;
import com.lmhitysu.leyou.item.model.*;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.UnmappedTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service("searchService")
public class SearchServiceImpl implements ISearchService{
    @Autowired
    private CategoryClients categoryClients;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private ObjectMapper mapper = new ObjectMapper();
    /**
     * 将spu数据sku数据以及详情转换为elasticsearch索引对应的model
     * @param spu
     * @throws Exception
     */
    @Override
    public Goods buildGoods(Spu spu) throws Exception{
        //获取分类名作
        ResponseEntity<List<String>> categoryNamesByIds = categoryClients.findCategoryNamesByIds(Arrays.asList(spu.getCid1(),
                spu.getCid2(), spu.getCid3()));
        List<String> categoriesNames = null;
        if(categoryNamesByIds.hasBody() && categoryNamesByIds.getStatusCode()!= HttpStatus.NOT_FOUND){
            categoriesNames = categoryNamesByIds.getBody();
        }
        //获取sku信息以及spu详情以及ｓｐｕ的规格参数
        ResponseEntity<List<Sku>> skuList = goodsClient.getSkuList(spu.getId());
        List<Sku> skus = null;
        if(skuList.getStatusCode()!=HttpStatus.NOT_FOUND){
            skus=skuList.getBody();
        }
        ResponseEntity<SpuDetail> spuDetail = goodsClient.getSpuDetail(spu.getId());
        SpuDetail detail = null;
        if(spuDetail.getStatusCode()!=HttpStatus.NOT_FOUND){
            detail = spuDetail.getBody();
        }
        ResponseEntity<String> specificationByCategoryId = specClient.getSpecificationByCategoryId(spu.getCid3());
        String spec = null;
        if(specificationByCategoryId.getStatusCode()!=HttpStatus.NOT_FOUND){
            spec=specificationByCategoryId.getBody();
        }
        List<Map<String,Object>> param = mapper.readValue(spec,new TypeReference<List<Map<String,Object>>>(){});
        // 处理规格参数
        List<Map<String, Object>> genericSpecs = mapper.readValue(detail.getSpecifications(), new TypeReference<List<Map<String,Object>>>() {
        });
        Map<String, Object> specialSpecs = mapper.readValue(detail.getSpecTemplate(), new TypeReference<Map<String, Object>>() {
        });
        //将规格信息转换为将数据直接转换为Ｌｉｓｔ<param>
        List<Map<String,Object>> listSpec = new ArrayList<>();
        Iterator<Map<String, Object>> iterator = param.iterator();
        while(iterator.hasNext()){
            Map<String, Object> next = iterator.next();
            List<Map<String,Object>> params = (List<Map<String,Object>>)next.get("params");
            Iterator<Map<String, Object>> iteratorParam = params.iterator();
            while (iteratorParam.hasNext()){
                listSpec.add(iteratorParam.next());
            }
        }
        //将ｓｐｕ规格信息转换为将数据直接转换为Ｌｉｓｔ<param>
        List<Map<String,Object>> listSpecSpu = new ArrayList<>();
        Iterator<Map<String, Object>> iteratorSpu = genericSpecs.iterator();
        while(iteratorSpu.hasNext()){
            Map<String, Object> next = iteratorSpu.next();
            List<Map<String,Object>> params = (List<Map<String,Object>>)next.get("params");
            Iterator<Map<String, Object>> iteratorParam = params.iterator();
            while (iteratorParam.hasNext()){
                listSpecSpu.add(iteratorParam.next());
            }
        }
        Map<String, Object> searchSpec = new HashMap<>();
        // 获取可搜索的规格参数 全局规格参数以及特有规格参数都有可能作为搜索的规格参数
        outer:
        for(int i=0;i<listSpec.size();i++){
            Boolean isSearchable = (Boolean)listSpec.get(i).get("searchable");
            if(isSearchable){
                Boolean isGlobal = (Boolean)listSpec.get(i).get("global");
                if(isGlobal){
                    for(int j = 0;j<listSpecSpu.size();j++){
                        Map<String,Object> specSpu = listSpecSpu.get(j);
                        if(listSpec.get(i).get("k").toString().equals(specSpu.get("k").toString())){
                            Object value = specSpu.get("v");
                            if(listSpec.get(i).get("numerical")!=null && (Boolean)listSpec.get(i).get("numerical")){
                                value = chooseSegment(value,listSpec.get(i));
                            }
                            searchSpec.put(listSpec.get(i).get("k").toString(),specSpu.get("v"));
                            break;
                        }
                    }
                }else{
                    searchSpec.put(listSpec.get(i).get("k").toString(),specialSpecs.get("k"));
                }
            }
        }
        //处理ｓｋｕ信息
        // 处理sku，仅封装id、价格、标题、图片，并获得价格集合
        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuListGoods = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            skuListGoods.add(skuMap);
        });
        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setAll(spu.getTitle() + " " + StringUtils.join(categoriesNames, " "));
        goods.setPrice(prices);
        goods.setSkus(mapper.writeValueAsString(skuListGoods));
        goods.setSpecs(searchSpec);
        return goods;
    }

    /**
     * 搜索数据
     * @param searchRequest
     * @return
     */
    @Override
    public PageResult<Goods> searchGoods(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        if(StringUtils.isEmpty(key)){
            return null;
        }
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 1、对key进行全文检索查询
        MatchQueryBuilder basicQuery = QueryBuilders.matchQuery("all",key).operator(Operator.AND);
        queryBuilder.withQuery(basicQuery);
        // 2、通过sourceFilter设置返回的结果字段,我们只需要id、skus、subTitle
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"}, null));
        //分页
        int page = searchRequest.getPageInfo().getCurrentPageNumber();
        int size = searchRequest.getPageInfo().getCurrentPageRows();
        queryBuilder.withPageable(PageRequest.of(page-1,size));
        //4.聚合
        String categoryAggName = "category";
        String brandAggName = "brand";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        //５　获取查询结果
        AggregatedPage<Goods> result = (AggregatedPage<Goods>) goodsRepository.search(queryBuilder.build());

        Integer totalPages = result.getTotalPages();
        //6.获取聚合结果
        List<Category> categories = getCategoryAggResult(result.getAggregation(categoryAggName));
        List<Brand> brands = getBrandAggResult(result.getAggregation(brandAggName));
        //7根据分类结果判断是否需要对规格参数进行聚合
        List<Map<String,Object>> specs = null;
        //这里应该是size　== 1 时才查询规格参数
        if(categories != null && categories.size()>0){
            specs = getSpec(categories.get(0).getId(),basicQuery);
        }
        return new SearchResult(result.getTotalElements(),Long.parseLong(totalPages.toString()),result.getContent(),categories,
                brands,specs);
    }

    @Override
    public void createIndex(Long id) throws Exception {
        Spu spu = this.goodsClient.getSpuById(id).getBody();
        // 构建商品
        Goods goods = this.buildGoods(spu);

        // 保存数据到索引库
        this.goodsRepository.save(goods);
    }

    @Override
    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }

    //spu的规格参数中要是该规格是numerical,在设置类型的specification时要指定片段
    private Object chooseSegment(Object value,Map<String,Object> param){
        return value;
    }
    //获取规格参数信息
    private List<Map<String,Object>> getSpec(Long cid,MatchQueryBuilder basicQuery) {
        try{
            ResponseEntity<String> specParam = this.specClient.getSpecificationByCategoryId(cid);
            //查询所有的规格参数
            List<Map<String, Object>> specs = mapper.readValue(specParam.getBody(), new TypeReference<List<Map<String,Object>>>() {});
            //最后返回的list
            List<Map<String,Object>> result = new ArrayList<>();
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(basicQuery);
            //将规格信息转换为将数据直接转换为Ｌｉｓｔ<param>
            List<Map<String,Object>> listSpec = new ArrayList<>();
            Iterator<Map<String, Object>> iterator = specs.iterator();
            while(iterator.hasNext()){
                Map<String, Object> next = iterator.next();
                List<Map<String,Object>> params = (List<Map<String,Object>>)next.get("params");
                Iterator<Map<String, Object>> iteratorParam = params.iterator();
                while (iteratorParam.hasNext()){
                    listSpec.add(iteratorParam.next());
                }
            }
            //设置聚合参数
            for(Map<String,Object> map : listSpec){
                String k = map.get("k").toString();
                queryBuilder.addAggregation(AggregationBuilders.terms(k).field("specs"+k+".keyword"));
            }
            //查询
            Map<String,Aggregation> aggs = this.elasticsearchTemplate.query(queryBuilder.build(), SearchResponse::getAggregations).asMap();
            for(Map<String,Object> map : listSpec){
                Map<String,Object> spec = new HashMap<>();
                String key = map.get("k").toString();
                spec.put("k",key);
                UnmappedTerms terms = (UnmappedTerms)aggs.get(key);
                spec.put("options","");
                //terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString()
                result.add(spec);
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }
    //获取根据分类聚合得到的信息
    //聚合信息异常往外抛出
    public List<Category> getCategoryAggResult(Aggregation aggregation){

        List<Category> categoryList = new ArrayList<>();
        LongTerms terms = (LongTerms) aggregation;
        List<Long> cids = new ArrayList<>();
        for(LongTerms.Bucket bucket : terms.getBuckets()){
            cids.add(bucket.getKeyAsNumber().longValue());
        }
        ResponseEntity<List<String>> categoryNamesByIds = categoryClients.findCategoryNamesByIds(cids);
        if(categoryNamesByIds != null && categoryNamesByIds.getBody() != null){
            List<String> names = categoryNamesByIds.getBody();
            for(int i =0 ,length = names.size();i<length;i++){
                Category category = new Category();
                category.setId(cids.get(i));
                category.setName(names.get(i));
                categoryList.add(category);
            }
        }
        return categoryList;
    }
    private List<Brand> getBrandAggResult(Aggregation aggregation){
        List<Brand> brands = new ArrayList<>();
        LongTerms terms = (LongTerms) aggregation;
        List<Long> ids = new ArrayList<>();
        for(LongTerms.Bucket bucket : terms.getBuckets()){
            ids.add(bucket.getKeyAsNumber().longValue());
        }
        ResponseEntity<List<Brand>> brandList = brandClient.getBrandsByIds(ids);
        if(brandList != null && brandList.getBody() != null){
            brands = brandList.getBody();
        }
        return brands;
    }
}
