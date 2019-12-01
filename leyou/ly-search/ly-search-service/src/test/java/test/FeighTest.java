package test;

import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.search.LySearchApplication;
import com.leyou.lmhitysu.search.clients.CategoryClients;
import com.leyou.lmhitysu.search.clients.GoodsClient;
import com.leyou.lmhitysu.search.model.Goods;
import com.leyou.lmhitysu.search.repository.GoodsRepository;
import com.leyou.lmhitysu.search.service.ISearchService;
import com.leyou.lmhitysu.search.service.SearchServiceImpl;
import com.lmhitysu.leyou.item.model.SpuVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchApplication.class)
public class FeighTest {
    @Autowired
    private CategoryClients categoryClient;
    @Autowired
    private ISearchService searchService;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private GoodsRepository goodsRepository;
    @Test
    public void testQueryCategories() {
        ResponseEntity<List<String>> res = this.categoryClient.findCategoryNamesByIds(Arrays.asList(1L, 2L, 3L));
        List<String> body = res.getBody();
        body.forEach(System.out::println);
    }
    @Test
    public void testBuildGoods(){
        ResponseEntity<PageResult<SpuVo>> pageResultResponseEntity = goodsClient.querySpuByPage(1,
                10, null, false, null, true);
        try {
            searchService.buildGoods(pageResultResponseEntity.getBody().getCurrentItems().get(0));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void loadData(){

        Integer page = 1;
        Integer rows = 100;
        Integer size = 100;
        do{
            ResponseEntity<PageResult<SpuVo>> pageResultResponseEntity = goodsClient.querySpuByPage(page,
                    rows, null, false, null, true);
            List<SpuVo> list = null;
            if(pageResultResponseEntity.getStatusCode()!= HttpStatus.NOT_FOUND){
                list = pageResultResponseEntity.getBody().getCurrentItems();
            }
            size = list.size();
            List<Goods> goodsList = new ArrayList<>();
            for(SpuVo spuVo:list){
                try{
                    Goods goods = searchService.buildGoods(spuVo);
                    goodsList.add(goods);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            goodsRepository.saveAll(goodsList);
            page++;
        }while (size ==100);
    }

}
