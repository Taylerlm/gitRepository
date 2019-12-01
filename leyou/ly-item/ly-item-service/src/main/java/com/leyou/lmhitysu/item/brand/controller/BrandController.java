package com.leyou.lmhitysu.item.brand.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.item.brand.service.IBrandService;
import com.lmhitysu.leyou.item.model.Brand;
import com.lmhitysu.leyou.item.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "brand")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @GetMapping(value = "page")
    @ResponseBody
    public PageResult<Brand> findBrandPages(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key
    ){
        Map params = new HashMap();
        params.put("page",page);
        params.put("rows",rows);
        params.put("sortBy",sortBy);
        params.put("desc",desc);
        params.put("key",key);
        return this.brandService.findBrandPages(params);
    }

    @PostMapping(value = "save")
    @ResponseBody
    public Map findBrandPages(Brand brand,@RequestParam("cids") List<Long> cids){
        return this.brandService.save(brand,cids);
    }
    @GetMapping(value = "bid/{bid}")
    public ResponseEntity<List<Category>> getCategoriesById (@PathVariable("bid") Long bid){
        List<Category> categories = this.brandService.getCategoriesById(bid);
        if(null == categories){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok(categories);
        }
    }
    @GetMapping(value = "cid/{cid}")
    public ResponseEntity<List<Brand>> getBrandByCid (@PathVariable("cid") Long cid){
        List<Brand> brands = this.brandService.getBrandByCid(cid);
        if(null == brands){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok(brands);
        }
    }
    @RequestMapping(value = "name")
    public ResponseEntity<String> findBrandNameById (@RequestParam("id") Long id){
        String brandName = this.brandService.findBrandNameById(id);
        if(null == brandName){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok(brandName);
        }
    }
    @RequestMapping(value = "getBrandsByIds")
    public ResponseEntity<List<Brand>> getBrandsByIds(@RequestParam("ids") List<Long> ids){
        List<Brand> list = this.brandService.findBrandsByIds(ids);
        if(null == list){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok(list);
        }
    }
}
