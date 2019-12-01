package com.leyou.lmhitysu.goods.service;

public interface IGoodsHtmlService {
    public void createHtml (Long spuId);
    public void asyncExcute(Long spuId);
    public void deleteHtml(Long id);
}
