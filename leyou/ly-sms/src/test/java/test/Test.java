package test;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.leyou.lmhitysu.sms.SmsApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmsApplication.class)
public class Test {
    private static final String ALIYUNCS_DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String ALIYUNCS_ACTION = "SendSms";
    private static final String ALIYUNCS_VERSION = "2017-05-25";
    private static final String ACCESSKEY_ID = "LTAIQtWBkmZMno91";
    private static final String ACCESSKEY_SCERET = "ZsnBoU9wJw0pyiJuUPzbDp0c5dKgac";
    @org.junit.Test
    public void sendSms(){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESSKEY_ID, ACCESSKEY_SCERET);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonResponse response = null;
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(ALIYUNCS_DOMAIN);
        request.setVersion(ALIYUNCS_VERSION);
        request.setAction(ALIYUNCS_ACTION);
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", "17615405079");
        request.putQueryParameter("SignName", "天才商城");
        request.putQueryParameter("TemplateCode", "SMS_171540803");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+"345012"+"\"}");
        try {
            response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        //return response;
    }
}
