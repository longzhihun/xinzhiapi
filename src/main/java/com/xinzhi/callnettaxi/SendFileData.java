package com.xinzhi.callnettaxi;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
@Component
public class SendFileData {
    final static String url = "https://wycs.bjjtw.gov.cn/receive/app/common/binapi/bjjjkjyxgs";
    public static String post(String strURL, Map<String, String> textMap, Map<String, String>
            fileMap) {
        String BOUNDARY = "---------------------------123821742118716"; //boundary 就是 request 头和上传文件内容的分隔符
        try
        {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection(); connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setUseCaches(false); connection.setInstanceFollowRedirects(true); connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("contentType", "utf-8");
            connection.setRequestProperty("Accept", "text/html");
            connection.setRequestProperty("Content-Encoding", "gzip");
            connection.setRequestProperty("Content-Type", "multipart/form-data; charset=UTF-8; boundary=" + BOUNDARY); // 设置发送数据的格式 connection.connect();
            OutputStream out = new DataOutputStream(connection.getOutputStream()); if(textMap != null)
        {StringBuffer strBuf = new StringBuffer();
            Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator(); while(iter.hasNext())
        {
            Map.Entry<String, String> entry = iter.next(); String inputName = (String)entry.getKey(); String inputValue = (String)entry.getValue(); if(inputValue == null)
        {
            continue;
        } strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");

            strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
            strBuf.append(inputValue); }
            out.write(strBuf.toString().getBytes("UTF-8")); }
            if(fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator(); while(iter.hasNext())
                {
                    Map.Entry<String, String> entry = iter.next(); String inputName = (String)entry.getKey(); String inputValue = (String)entry.getValue();
                    if(inputValue == null){ continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    StringBuffer strBuf = new StringBuffer(); strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"; filename=\"" + filename+ "\"\r\n"); strBuf.append("Content-Type:" + "multipart/form-data" + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes("UTF-8"));
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024]; while((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes); }
                    in.close(); }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes(); out.write(endData);
            out.flush();
            out.close();

            int length = (int)connection.getContentLength();// 获取长度
            InputStream is = null;
            int responseCode = connection.getResponseCode();
            if(responseCode==200){
                is = connection.getInputStream(); }else{
                is = connection.getErrorStream(); }
            if(length != -1)

            {
                byte[] data = new byte[length]; byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen; }
                String result = new String(data, "UTF-8"); // utf-8 编码
                return result; }
        }
        catch(IOException e) {
            e.printStackTrace(); }
        return "error"; //
    }
    public static void main(String[] args) {
        String filepath = "/Users/jlguo/Downloads/BJJJKJYXGS_PTBA_REQ_20170425164050216.json";
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("fileName", "luxin.json");
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("binFile", filepath);
       System.out.println(post(url, textMap, fileMap));
    }
}