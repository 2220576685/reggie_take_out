package com.itheima;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.junit.jupiter.api.Test;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/6 0006 19:44
 */

public class SendSms {
    @Test
    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5tD7GQDivRVzckMNEtBB", "o9P0ac4kxh7lPKG0PT9kr2Z0r12qmv" +
                "");//自己账号的AccessKey信息
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");//短信服务的服务接入地址
        request.setSysVersion("2017-05-25");//API的版本号
        request.setSysAction("SendSms");//API的名称
        request.putQueryParameter("PhoneNumbers", "18727294556");//接收短信的手机号码
        request.putQueryParameter("SignName", "传智健康");//短信签名名称
        request.putQueryParameter("TemplateCode", "SMS_175485149");//短信模板ID
        request.putQueryParameter("TemplateParam", "{\"code\":\"6666\"}");//短信模板变量对应的实际值
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}