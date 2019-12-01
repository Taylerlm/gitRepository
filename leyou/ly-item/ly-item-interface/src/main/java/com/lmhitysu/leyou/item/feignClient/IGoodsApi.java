package com.lmhitysu.leyou.item.feignClient;

import com.leyou.lmhitysu.common.PageResult;
import com.lmhitysu.leyou.item.model.Sku;
import com.lmhitysu.leyou.item.model.Spu;
import com.lmhitysu.leyou.item.model.SpuDetail;
import com.lmhitysu.leyou.item.model.SpuVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("goods")
public interface IGoodsApi {
    @GetMapping(value = "spu/page")
    public ResponseEntity<PageResult<SpuVo>> querySpuByPage(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
                                                            @RequestParam(value = "sortBy", required = false) String sortBy,
                                                            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                            @RequestParam(value = "key", required = false) String key,
                                                            @RequestParam(value = "saleable") Boolean saleable
    );
    @RequestMapping(value = "save")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuVo spu);

    @RequestMapping(value = "spuDetail/{spuId}")
    public ResponseEntity<SpuDetail> getSpuDetail(@PathVariable(value = "spuId") Long id);

    @RequestMapping(value = "skus/{spuId}")
    public ResponseEntity<List<Sku>> getSkuList(@PathVariable(value = "spuId") Long id);

    @RequestMapping(value = "spu/{id}")
    public ResponseEntity<Spu> getSpuById(@PathVariable(value = "id") Long id);
    @RequestMapping(value = "sku/{id}")
    public ResponseEntity<Sku> getSkuById(@PathVariable(value = "id") Long id);
}
