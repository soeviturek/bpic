package com.bpic.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
@Component
public class SendSmsUtil {

    @Value("${sms.sendurl}")
    private String sendurl;

    @Value("${sms.acount}")
    private String acount;

    @Value("${sms.password}")
    private String spassword;

    @Value("${sms.businessType}")
    private String businessType;

    @Value("${sms.organization}")
    private String organization;

    private static volatile AtomicInteger i = new AtomicInteger(0) ;

    /**
     * @param cellphone 收件人手机号
     * @param context 短信内容
     * @return
     */
    public  ArrayList<String> sendSms(String cellphone,String context){
        SmsClass smsClass = new SmsClass();
        SmsBean smsBean =  new SmsBean();
        smsBean.setKeyWord(context);
        smsBean.setUserID(acount);
        smsBean.setPassword(spassword);
        smsBean.setBusinessType(businessType);
        smsBean.setOrganization(organization);
        smsBean.setRecvMobile(cellphone);
        String format = new SimpleDateFormat("yyyyMMdd").format(new Date());
        i.incrementAndGet();
        String sixInt = sixInt();
        smsBean.setSeqNo(acount + format+ sixInt);
        ArrayList<String> result = smsClass.sendSingleMsg(smsBean,sendurl);
        return result;
    }
    public static String sixInt(){
        AtomicInteger i1  = new AtomicInteger(9);
        AtomicInteger i2  = new AtomicInteger(99);
        AtomicInteger i3  = new AtomicInteger(999);
        AtomicInteger i4  = new AtomicInteger(9999);
        AtomicInteger i5  = new AtomicInteger(99999);
        if (i.get() <= i1.get()){
            return "00000"+ i;
        }else if (i.get() <= i2.get()){
            return "0000"+ i;
        }else if(i.get() <= i3.get()){
            return "000"+ i;
        }else if(i.get() <= i4.get()){
            return "00"+ i;
        }else if (i.get() <= i5.get()){
            return "00"+ i;
        }else {
            return  String.valueOf(i);
        }
    }

}
