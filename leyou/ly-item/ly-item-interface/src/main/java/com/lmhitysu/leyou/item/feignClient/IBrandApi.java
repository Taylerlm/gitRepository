package com.lmhitysu.leyou.item.feignClient;

import com.leyou.lmhitysu.common.PageResult;
import com.lmhitysu.leyou.item.model.Brand;
import com.lmhitysu.leyou.item.model.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("brand")
public interface IBrandApi {
    @GetMapping(value = "page")
    @ResponseBody
    public PageResult<Brand> findBrandPages(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key
    );
    @PostMapping(value = "save")
    @ResponseBody
    public Map findBrandPages(Brand brand, @RequestParam("cids") List<Long> cids);
    @GetMapping(value = "bid/{bid}")
    public ResponseEntity<List<Category>> getCategoriesById (@PathVariable("bid") Long bid);

    @GetMapping(value = "cid/{cid}")
    public ResponseEntity<List<Brand>> getBrandByCid (@PathVariable("cid") Long cid);

    @RequestMapping(value = "name")
    public ResponseEntity<String> findBrandNameById (@RequestParam("id") Long id);

    @RequestMapping(value = "getBrandsByIds")
    public ResponseEntity<List<Brand>> getBrandsByIds(@RequestParam("ids") List<Long> ids);

}
