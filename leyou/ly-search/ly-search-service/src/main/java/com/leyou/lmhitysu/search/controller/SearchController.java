package com.leyou.lmhitysu.search.controller;

import com.leyou.lmhitysu.common.PageResult;
import com.leyou.lmhitysu.search.model.Goods;
import com.leyou.lmhitysu.search.model.SearchRequest;
import com.leyou.lmhitysu.search.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "elastic")
public class SearchController {
    @Autowired
    private ISearchService searchService;

    @RequestMapping(value = "page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest){
        PageResult<Goods> list = this.searchService.searchGoods(searchRequest);
        if(list == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
}
