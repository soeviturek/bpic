package com.bpic.common.utils.http;


import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * describe:
 * remark:
 * author: 韩盟凯
 * Date: 2017-12-7 9:07
 */
public class HttpClient {

    private static Logger log =  LogManager.getLogger(HttpClient.class);

    /**
     * 发送post请求
     * @param url 地址
     * @param param 参数
     * @return 返回值
     * @throws IOException 异常
     */
    public static String sendPost(String url, String param) throws IOException {
        log.info("----------------------------------------------开始请求-----------------------------");
        String result = "";
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("contentType", "UTF-8");
        conn.setConnectTimeout(300000);
        conn.setReadTimeout(300000);
        // 获取URLConnection对象对应的输出流
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        log.info("request===>>>"+param);
        out.write(param);
        // flush输出流的缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
        String line;
        log.info("=============================");
        log.info("Contents of post request");
        log.info("=============================");
        while ((line = in.readLine()) != null) {
            result += line;
        }
        log.info("=============================");
        log.info("Contents of post request ends");
        log.info("=============================");
        //关闭输出流、输入流
        out.close();
        in.close();
        log.info("response===>>>"+result);
        log.info("----------------------------------------------请求结束-----------------------------");
        return result;
    }

    /**
     * 微信请求方法
     * @param jsonData
     * @param POST_URL
     * @return
     * @throws IOException
     */
    public static String readContentFromPost(String jsonData, String POST_URL) throws IOException {
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(POST_URL);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        // http正文内，因此需要设为true
        connection.setDoOutput(true);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // Set the post method. Default is GET
        connection.setRequestMethod("POST");
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        log.info("----------------------------------------------开始请求-----------------------------");
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        connection.setConnectTimeout(120000);
        connection.setReadTimeout(120000);
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        String content = "jsonData=" + URLEncoder.encode(jsonData, "utf-8");
        out.writeBytes(content);
        out.flush();
        out.close(); // flush and close
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        String line;
        log.info("=============================");
        log.info("Contents of post request");
        log.info("=============================");
        String jsonString = "";
        while ((line = reader.readLine()) != null) {
            jsonString = jsonString + line;
        }
        log.info("=============================");
        log.info("Contents of post request ends");
        log.info("=============================");
        reader.close();
        connection.disconnect();
        log.info("----------------------------------------------开始结束-----------------------------");
        return jsonString;
    }

    public static String doPostString(String url,String content){

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(600000).setConnectionRequestTimeout(600000)
                .setSocketTimeout(600000).build();
        post.setConfig(requestConfig);
        String result = "";
        try {
            StringEntity s = new StringEntity(content, "utf-8");
            s.setContentEncoding("UTF-8");
            s.setContentType("text/plain");//发送json数据需要设置contentType
            System.out.print(s);
            post.setEntity(s);
            HttpResponse res = httpclient.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                result = EntityUtils.toString(res.getEntity());// 返回json格式：
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String sendGet(String requestUrl) {
        String result = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();){
            log.info("==================sendGet请求开始requestUrl:{}=========start=============", requestUrl);
            HttpGet httpGet = new HttpGet(requestUrl);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            log.info("==================sendGet请求返回结果result:{}============================", result);
        } catch (IOException e) {
            log.error("================sendGet请求结束异常:{}===================================", e.getMessage());
            e.printStackTrace();
        }
        log.info("==================sendGet请求结束=============end==============");
        return result;
    }


    public static void main(String[] args) {
        try {
            String res = sendPost("","channelCode=wc-001");
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
