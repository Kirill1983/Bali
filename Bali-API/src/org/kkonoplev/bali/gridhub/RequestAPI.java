package org.kkonoplev.bali.gridhub;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.log4j.Logger;

public class RequestAPI {

	private static final Logger log = Logger.getLogger(RequestAPI.class);

	public static String getURL(String fullurl) throws Exception{
		
		 URL url = new URL(fullurl);
		 log.info(fullurl);
	     URLConnection urlcon = url.openConnection();
	     BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                urlcon.getInputStream()));
	     StringBuffer buf = new StringBuffer();
	     String line;

	     while ((line = in.readLine()) != null) 
	            buf.append(line);
	     in.close();
	     
	     return buf.toString();
		
	}
	
	public static void postURL(String fullurl, Map<String,String> arguments) throws Exception{
		
		 URL url = new URL(fullurl);
	     URLConnection urlcon = url.openConnection();
	     
	     HttpURLConnection http = (HttpURLConnection)urlcon;
	     http.setRequestMethod("POST"); // PUT is another valid option
	     http.setDoOutput(true);
	     
	     StringJoiner sj = new StringJoiner("&");
	     for(Map.Entry<String,String> entry : arguments.entrySet())
	         sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" 
	              + URLEncoder.encode(entry.getValue(), "UTF-8"));
	     byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
	     int length = out.length;
	     
	     http.setFixedLengthStreamingMode(length);
	     http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	     http.connect();
	     
	     try(OutputStream os = http.getOutputStream()) {
	         os.write(out);
	     }
		
	}
	
}
