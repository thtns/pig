package com.pig4cloud.pig.capi.service.atripartite.chaboshi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {
  private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
  
  public static String sendGet(String url, String param) throws IOException {
    String result = "";
    BufferedReader in = null;
    try {
      String urlNameString = url + "?" + param;
      URL realUrl = new URL(urlNameString);
      URLConnection connection = realUrl.openConnection();
      connection.setRequestProperty("accept", "*/*");
      connection.setRequestProperty("connection", "Keep-Alive");
      connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      connection.setRequestProperty("Accept-Charset", "utf-8");
      connection.setRequestProperty("contentType", "utf-8");
      connection.connect();
      in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
      String line;
      while ((line = in.readLine()) != null)
        result = result + line; 
    } finally {
      try {
        if (in != null)
          in.close(); 
      } catch (Exception e2) {
        logger.error("", e2);
      } 
    } 
    return result;
  }
  
  public static String sendPost(String url, String param) throws IOException {
    PrintWriter out = null;
    BufferedReader in = null;
    String result = "";
    try {
      URL realUrl = new URL(url);
      URLConnection conn = realUrl.openConnection();
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      conn.setRequestProperty("Accept-Charset", "utf-8");
      conn.setRequestProperty("contentType", "utf-8");
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setConnectTimeout(60000);
      conn.setReadTimeout(60000);
      out = new PrintWriter(conn.getOutputStream());
      out.print(param);
      out.flush();
      in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
      String line;
      while ((line = in.readLine()) != null)
        result = result + line; 
    } finally {
      try {
        if (out != null)
          out.close(); 
        if (in != null)
          in.close(); 
      } catch (IOException ex) {
        logger.error("", ex);
      } 
    } 
    return result;
  }
}
