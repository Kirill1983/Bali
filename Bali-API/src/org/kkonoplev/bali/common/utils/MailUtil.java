/* 
 * Copyright � 2011 Konoplev Kirill
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kkonoplev.bali.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

/**
 * 
 *
 * @author kkono.
 */
public class MailUtil {
    
    public static final String X_MAILER = "Mail Subsystem";
    private static final Logger log = Logger.getLogger(MailUtil.class);

    /* 
    public static final String X_MAILER = "WebTester mail subsystem";
     */

    /**  */
    public static final String DEFAULT_HOST = "smtphub.uk.mail.db.com";

    /**
     * 
     *
     * @param from    
     * @param to       
     * @param cc      
     * @param subject  
     * @param html     
     * @param host     
     * @param login   
     * @param password 
     * @param debug    true - 
     * @throws MessagingException 
     * @throws IOException        
     */
    public static void sendHTML(String from, String to, String cc, String subject, String html, String host, final String login, final String password, boolean debug) throws MessagingException, IOException {
        // 
        Properties props = new Properties();
        //props.put("mail.smtp.starttls.enable", "true");
        
        if (host != null) 
            props.put("mail.smtp.host", host);
        
        props.put("mail.smtp.starttls.enable", "true");
        
        
        Session session;

        if (login != null && password != null) {
            props.put("mail.smtp.auth", "true");
            session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login, password);
                }
            });
        } else {
            props.put("mail.smtp.auth", "false");
            session = Session.getInstance(props);
        }

        session.setDebug(debug);
        Message msg = new MimeMessage(session);
        
        if (from != null) 
            msg.setFrom(new InternetAddress(from));
        else
            throw new MessagingException("Address 'from' can't be null!");
        

        if (to != null) 
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        else 
            throw new MessagingException("Address 'to' can't be null!");
        

        if (cc != null) 
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
        

        if (subject != null) 
            msg.setSubject(subject);
        

        //msg.setDataHandler(new DataHandler(new ByteArrayDataSource(html, "text/html")));
        //msg.setHeader("X-Mailer", X_MAILER);
        msg.setSentDate(new Date());

        //Transport.send(msg);
        msg.setContent( html, "text/html" );

        Transport.send( msg, msg.getAllRecipients() );

        /*
        MimeMessage message = new MimeMessage(session);

        msg.saveChanges();
        Transport transport = session.getTransport("smtp");
        transport.connect(host, null, null);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
        */
    }

    /**
     * 
     *
     * @param from  
     * @param to      
     * @param cc      
     * @param subject
     * @param html   
     * @param debug  
     * @throws MessagingException
     * @throws IOException        
     */
    public static void sendHTML(String from, String to, String cc, String subject, String html, boolean debug) throws MessagingException, IOException {
        sendHTML(from, to, cc, subject, html, DEFAULT_HOST, null, null, debug);
    }
    
    /**
     * 
     *
     * @param from  
     * @param to      
     * @param cc      
     * @param subject
     * @param html   
     * @param debug  
     * @throws MessagingException
     * @throws IOException        
     */
    public static void sendUrlHTML(String from, String to, String cc, String subject, String url, boolean debug){
    	try {
    		String html = getHtmlContent(url);
    		sendHTML(from, to, cc, subject, html, DEFAULT_HOST, null, null, debug);
    	} catch (Exception e){
    		log.warn(e,e);
    	}
    }
    
    
	public static String getHtmlContent(String baseUrl) throws Exception {
		
        URL url = new URL(baseUrl);
        URLConnection conn;
        conn = url.openConnection();
        conn.setDoInput(true);

        // Get the response 
        StringBuffer answer = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
        String line; 
        String prefix = "<input type=hidden name=\"sessionid\" value=\"";
        String id = "";
        while ((line = rd.readLine()) != null) { 
        	answer.append(line);
        } 
        rd.close(); 
        
        return answer.toString();
    }

}