package com.lmhitysu.leyou.item.model;

import java.util.List;
import java.util.Map;

public class SpecGroup {
    private Long id;

    private Long cid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Map<String, Object>> getParams() {
        return params;
    }

    public void setParams(List<Map<String, Object>> params) {
        this.params = params;
    }

    private String group;

    private List<Map<String,Object>> params; // 该组下的所有规格参数集合
}
