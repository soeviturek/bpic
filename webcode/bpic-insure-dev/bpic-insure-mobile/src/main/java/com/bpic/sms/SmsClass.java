package com.bpic.sms;

import com.bpic.weixin.DES;
import com.bpic.weixin.MD5Util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 发短信工具类
 */
public class SmsClass {

    private ArrayList<String> returnResult = new ArrayList<String>();

    private String dateStr = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分SS秒").format(new Date());

    public static final String DATA_TEMPLATE1 = "yyyy-MM-dd";
    public static final String DATA_TEMPLATE3 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_TEMPLATE4 = "HH:mm:ss";
    public static final String DATA_TEMPLATE2 = "yyyyMMddHHmmss";

    public ArrayList<String> sendSingleMsg(SmsBean smsBean,String uri) {
        try {
            /********* 拼接报文XML部分 Start *********/
            StringBuilder xml = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> ");
            xml.append("<messages>");

            //账号
            if (smsBean.getUserID() != null) {
                xml.append("<userId>");
                xml.append(smsBean.getUserID());
                xml.append("</userId>");
            } else {
                xml.append("<userId/>");
            }

            //密码
            if (smsBean.getPassword() != null) {
                xml.append("<password>");
                xml.append(MD5Util.md5(smsBean.getPassword()));
                xml.append("</password>");
            } else {
                xml.append("<password/>");
            }

            //业务类型
            if (smsBean.getBusinessType() != null) {
                xml.append("<businessType>");
                xml.append(smsBean.getBusinessType());
                xml.append("</businessType>");
            } else {
                xml.append("<businessType/>");
            }

            Date now = new Date();
            String date = getFormaterDateStr(now);
            String time = getFormaterDateStr(now, DATA_TEMPLATE4);
            String sendTime = date + "T" + time;

            //创建时间
            xml.append("<sendTime>");
            xml.append(sendTime);
            xml.append("</sendTime>");

            //短信查询唯一索引(后续查询短信结果可以用这个值查询)
            if (smsBean.getSeqNo() != null) {
                xml.append("<sequenceNumber>");
                xml.append(smsBean.getSeqNo());
                xml.append("</sequenceNumber>");
            } else {
                xml.append("<sequenceNumber/>");
            }

            //机构代码
            if (smsBean.getOrganization() != null) {
                xml.append("<organization>");
                xml.append(smsBean.getOrganization());
                xml.append("</organization>");
            } else {
                xml.append("<organization/>");
            }

            /********* 短信内容追加部分 Start *********/
            xml.append("<contents>");
            xml.append("<content>");

            //电话号码
            if (smsBean.getRecvMobile() != null) {
                xml.append("<recvMobile>");
                xml.append(smsBean.getRecvMobile());
                xml.append("</recvMobile>");
            } else {
                xml.append("<recvMobile/>");
            }

            //短信内容
            if (smsBean.getKeyWord() != null) {
                xml.append("<messageContents>");
                xml.append(smsBean.getKeyWord());
                xml.append("</messageContents>");
            } else {
                xml.append("<messageContents/>");
            }

            xml.append("</content>");
            xml.append("</contents>");
            /********* 短信内容追加部分 End *********/

            xml.append("</messages>");
            /********* 拼接报文XML部分 End *********/

            String xmlStr = xml.toString();
            //把短信内容进行DES加密后传输
            xmlStr = DES.encrypt(xmlStr);

            byte[] xmlbyte = xmlStr.getBytes(StandardCharsets.UTF_8);

            //构建URL
            URL url = new URL(uri);

            /********* HTTP传输部分 Start *********/

            /********* HTTP写报文部分 Start *********/
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-type", "application/xml");
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(xmlbyte);
            outStream.flush();
            /********* HTTP写报文部分 End *********/

            /********* HTTP读取返回值部分 Start *********/
            BufferedReader in = new BufferedReader(new InputStreamReader(conn
                    .getInputStream()));
            String lines = "";
            while (null != (lines = in.readLine())) {
                //将返回结果装到集合中
                returnResult.add(lines);
            }
            in.close();
            outStream.close();
            /********* HTTP读取返回值部分 End *********/

        } catch (ConnectException ex) {
            // 服务器空响应
            returnResult.clear();
            returnResult.add("009");
        } catch (Exception ex) {
            //其他异常
            returnResult.clear();
            returnResult.add("010");
        }
        return returnResult;
    }

    public static String getFormaterDateStr(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATA_TEMPLATE1);
            return sdf.format(date);
        }
        return null;
    }

    public static String getFormaterDateStr(Date date, String dataTemplate) 	{
        if (date != null) {
            if (dataTemplate == null || "".equals(dataTemplate))
                dataTemplate = DATA_TEMPLATE1;
            SimpleDateFormat sdf = new SimpleDateFormat(dataTemplate);
            return sdf.format(date);
        }
        return null;
    }
}

