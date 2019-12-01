package com.leyou.lmhitysu.sms.config;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

//@EnableConfigurationProperties(SmsProperties.class)
@Component
public class SmsUtils {
    //@Autowired
    //private SmsProperties prop;

    private static final String ALIYUNCS_DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String ALIYUNCS_ACTION = "SendSms";
    private static final String ALIYUNCS_VERSION = "2017-05-25";
    private static final String ACCESSKEY_ID = "LTAIQtWBkmZMno91";
    private static final String ACCESSKEY_SCERET = "ZsnBoU9wJw0pyiJuUPzbDp0c5dKgac";
    public  CommonResponse sendSms(String phoneNum, String code, String signName, String template){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEY_ID, ACCESSKEY_SCERET);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonResponse response = null;
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(ALIYUNCS_DOMAIN);
        request.setVersion(ALIYUNCS_VERSION);
        request.setAction(ALIYUNCS_ACTION);
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", template);
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
             response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return response;
    }
}
