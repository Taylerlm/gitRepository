package com.leyou.lmhitysu.common;

import java.util.List;

public class PageResult<T> {
    private Long total;//总条数
    private Long totalPages;//总页数
    private List<T> currentItems;//当前页数据
    public PageResult(){

    }
    public PageResult(Long total,List<T> currentItems){
        this.total = total;
        this.currentItems = currentItems;
    }
    public PageResult(Long total,List<T> currentItems,Long totalPages){
        this.total = total;
        this.currentItems = currentItems;
        this.totalPages = totalPages;
    }
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getCurrentItems() {
        return currentItems;
    }

    public void setCurrentItems(List<T> currentItems) {
        this.currentItems = currentItems;
    }
}
