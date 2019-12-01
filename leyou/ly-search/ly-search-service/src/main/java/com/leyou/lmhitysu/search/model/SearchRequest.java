package com.leyou.lmhitysu.search.model;

import com.leyou.lmhitysu.common.PageInfo;

public class SearchRequest {
    private PageInfo pageInfo;
    private String key;
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
