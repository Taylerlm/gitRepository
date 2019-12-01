package com.leyou.lmhitysu.item.goods.controller;

import com.leyou.lmhitysu.common.Exception.LyException;
import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.item.goods.service.IGoodsService;
import com.lmhitysu.leyou.item.model.Sku;
import com.lmhitysu.leyou.item.model.Spu;
import com.lmhitysu.leyou.item.model.SpuDetail;
import com.lmhitysu.leyou.item.model.SpuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "goods")
public class GoodsController {
    @Autowired
    private IGoodsService goodsService;

    @GetMapping(value = "spu/page")
    public ResponseEntity<PageResult<SpuVo>> querySpuByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                          @RequestParam(value = "rows",defaultValue = "5") Integer rows,
                                                          @RequestParam(value = "sortBy", required = false) String sortBy,
                                                          @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                          @RequestParam(value = "key", required = false) String key,
                                                          @RequestParam(value = "saleable") Boolean saleable
                               ){
        Map param = new HashMap<>();
        param.put("page",page);
        param.put("rows",rows);
        param.put("sortBy",sortBy);
        param.put("desc",desc);
        param.put("key", StringUtils.isEmpty(key)?null:key);
        if(saleable != null){
            param.put("saleable",saleable == true?1:0);
        }
        PageResult<SpuVo> result = this.goodsService.querySpuByPageAndSort(param);
        if(result == null || result.getCurrentItems() == null){
            return ResponseEntity.ok(null);
        }else{
            return ResponseEntity.ok(result);
        }
    }
    @RequestMapping(value = "save")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuVo spu){
        try{
            this.goodsService.save(spu);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (LyException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @RequestMapping(value = "spuDetail/{spuId}")
    public ResponseEntity<SpuDetail> getSpuDetail(@PathVariable(value = "spuId") Long id){
            SpuDetail spuDetail = this.goodsService.getSpuDetail(id);
            if(spuDetail != null){
                return ResponseEntity.ok(spuDetail);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @RequestMapping(value = "skus/{spuId}")
    public ResponseEntity<List<Sku>> getSkuList(@PathVariable(value = "spuId") Long id){
        List<Sku> list = this.goodsService.getSkuList(id);
        if(list != null){
            return ResponseEntity.ok(list);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @RequestMapping(value = "spu/{id}")
    public ResponseEntity<Spu> getSpuById(@PathVariable(value = "id") Long id){
        Spu list = this.goodsService.getSpuById(id);
        if(list != null){
            return ResponseEntity.ok(list);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @RequestMapping(value = "sku/{id}")
    public ResponseEntity<Sku> getSkuById(@PathVariable(value = "id") Long id){
        Sku sku = this.goodsService.getSkuById(id);
        if(sku != null){
            return ResponseEntity.ok(sku);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
