package com.leyou.lmhitysu.common;

public class PageInfo {
    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_PAGE_ROWS = 20;
    private Integer currentPageNumber;
    private Integer currentPageRows;

    public PageInfo(Integer currentPageNumber, Integer currentPageRows) {
        this.currentPageNumber = currentPageNumber;
        this.currentPageRows = currentPageRows;
    }
    public PageInfo(){
        this.currentPageNumber=DEFAULT_PAGE;
        this.currentPageRows=DEFAULT_PAGE_ROWS;
    }


    public Integer getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(Integer currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    public Integer getCurrentPageRows() {
        return currentPageRows;
    }

    public void setCurrentPageRows(Integer currentPageRows) {
        this.currentPageRows = currentPageRows;
    }


}
