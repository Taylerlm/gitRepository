package com.leyou.lmhitysu.goods.service;

import com.leyou.lmhitysu.common.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

@Service("goodsHtmlService")
public class GoodsHtmlServiceImpl implements IGoodsHtmlService {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private TemplateEngine templateEngine;
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlServiceImpl.class);
    @Override
    public void createHtml(Long spuId) {
        PrintWriter writer = null;
        try {
            Map<String, Object> supMap = this.goodsService.loadModel(spuId);
            //创建themleaf上下文对象
            Context context = new Context();
            //数据放入上下文对象
            context.setVariables(supMap);
            //创建输出流
            File file = new File("/home/liming/thymeleaf/item/"+spuId+".html");
            writer = new PrintWriter(file);
            templateEngine.process("item",context,writer);
        }catch (Exception e){
            LOGGER.error("页面静态化出错",e);
        }finally{
            if(writer != null){
                writer.close();
            }
        }
    }

    @Override
    public void asyncExcute(Long spuId) {
        ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });
    }

    @Override
    public void deleteHtml(Long spuId) {
        File file = new File("/home/liming/thymeleaf/item/"+spuId+".html");
        file.deleteOnExit();
    }


}
