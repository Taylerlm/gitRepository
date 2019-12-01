package com.leyou.lmhitysu.item.category.controller;

import com.leyou.lmhitysu.item.category.service.ICategoryService;
import com.lmhitysu.leyou.item.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "category")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping(value = "list")
    @ResponseBody
    public List<Category> findCategoryById(@RequestParam(value = "pid",defaultValue = "0") Long pid){
        Category category = new Category();
        category.setParentId(pid);
        return this.categoryService.findCategoryById(category);
    }
    @RequestMapping (value = "name")
    public ResponseEntity<List<String>> findCategoryNamesByIds(@RequestParam(value = "cids") List<Long> ids){
        List<String> list = categoryService.findCategoryNamesByIds(ids);
        if(null == list){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return ResponseEntity.ok(list);
        }
    }
}
