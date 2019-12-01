import com.leyou.lmhitysu.auth.AuthApplication;
import com.leyou.lmhitysu.auth.entity.UserInfo;
import com.leyou.lmhitysu.auth.utils.JwtUtils;
import com.leyou.lmhitysu.auth.utils.RsaUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthApplication.class)
public class ATest {
    private static final String pubKeyPath = "/home/liming/RsaAugorithm/rsa.pub";

    private static final String priKeyPath = "/home/liming/RsaAugorithm/rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtil.generateKey(pubKeyPath, priKeyPath, "234");
    }
    @Test
    public void testReadFile() throws Exception{
        File path = new File(pubKeyPath);
        /*File path = new File("/home/liming");
        if(path.isDirectory()){
            System.out.println("sdfsfsdfsdfsdfsd");
        }*/
        if(!path.exists()){
            path.createNewFile();
        }
    }

    @Test
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtil.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtil.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTUzMzI4MjQ3N30.EPo35Vyg1IwZAtXvAx2TCWuOPnRwPclRNAM4ody5CHk8RF55wdfKKJxjeGh4H3zgruRed9mEOQzWy79iF1nGAnvbkraGlD6iM-9zDW8M1G9if4MX579Mv1x57lFewzEo-zKnPdFJgGlAPtNWDPv4iKvbKOk1-U7NUtRmMsF1Wcg";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
