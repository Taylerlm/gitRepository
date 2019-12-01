package com.lmhitysu.leyou.item.feignClient;

import com.lmhitysu.leyou.item.model.SpecGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("spec")
public interface ISpecApi {
    @GetMapping("{id}")
    public ResponseEntity<String> getSpecificationByCategoryId(@PathVariable("id") Long categoryId);
    // 查询规格参数组，及组内参数
    @GetMapping("group/{cid}")
    ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid);
}
