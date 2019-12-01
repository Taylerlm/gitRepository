package com.leyou.lmhitysu.item.specification.controller;

import com.leyou.lmhitysu.item.specification.service.ISpecificationService;
import com.lmhitysu.leyou.item.model.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "spec")
public class SpecificationController {
    @Autowired
    private ISpecificationService specificationService;
    @GetMapping("{id}")
    public ResponseEntity<String> getSpecificationByCategoryId(@PathVariable("id") Long categoryId){
        String specification = specificationService.getSpecificationByCategoryId(categoryId);
        if(null == specification){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(specification);
    }
    // 查询规格参数组，及组内参数
    @GetMapping("group/{cid}")
    ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> list = this.specificationService.querySpecsByCid(cid);
        if(list != null){
            return ResponseEntity.ok(list);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
