package com.lmhitysu.leyou.item.feignClient;

import com.lmhitysu.leyou.item.model.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@RequestMapping("category")
public interface ICategoryApi {
    @GetMapping(value = "list")
    @ResponseBody
    public List<Category> findCategoryById(@RequestParam(value = "pid",defaultValue = "0") Long pid);

    @RequestMapping(value = "name")
    public ResponseEntity<List<String>> findCategoryNamesByIds(@RequestParam(value = "cids") List<Long> ids);
}
