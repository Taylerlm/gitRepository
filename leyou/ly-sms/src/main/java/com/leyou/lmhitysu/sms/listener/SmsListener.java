package com.leyou.lmhitysu.sms.listener;

import com.aliyuncs.CommonResponse;
import com.leyou.lmhitysu.sms.config.SmsProperties;
import com.leyou.lmhitysu.sms.config.SmsUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
//@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {
    private static final String SIGN_NAME = "天才商城";
    private static final String TEMPLATE_CODE = "SMS_171540803";
    @Autowired
    private SmsUtils smsUtils;
    //@Autowired
    //private SmsProperties smsProperties;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.sms.queue", durable = "true"),
            exchange = @Exchange(value = "ly.sms.exchange",
                    ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    public void listenSms(Map<String,String> msg) throws Exception{
        if(msg == null){
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");
        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)){
            return;
        }
        CommonResponse commonResponse = smsUtils.sendSms(phone, code, SIGN_NAME, TEMPLATE_CODE);
        if(commonResponse == null){
            throw new Exception("SMS-ERROR-001短信发送异常");
        }
    }
}
