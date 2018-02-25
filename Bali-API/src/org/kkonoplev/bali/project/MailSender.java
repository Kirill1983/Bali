package org.kkonoplev.bali.project;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender
{
    public static void sendHTMLEmail( String fromEmailId, String toEmailId, String host, String hostUserName,
            String hostPassword, String mailSubject, String mailBody )
    {
    	
    	// Get system properties.
        Properties props = System.getProperties();
        // Setup mail server
        props.put( "mail.transport.protocol", "smtp" );
        props.put( "mail.smtp.host", host );
        props.put( "mail.smtp.auth", "false" );
        props.put("mail.smtp.starttls.enable", "true");
        

        final String hostUName = hostUserName;
        final String hPassword = hostPassword;

        Authenticator authenticator = new Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication( hostUName, hPassword );
            }
        };

        // Get the default Session object.
        Session session = Session.getDefaultInstance( props, authenticator );
        session.setDebug(true);

        try
        {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage( session );

            // Set From: header field of the header.
            message.setFrom( new InternetAddress( fromEmailId ) );

            // Set To: header field of the header.
            message.addRecipient( Message.RecipientType.TO, new InternetAddress( toEmailId ) );

            // Set Subject: header field
            message.setSubject( mailSubject );

            // Send the actual HTML message, as big as you like
            message.setContent( mailBody, "text/html" );

            // Send message
            Transport.send( message, message.getAllRecipients() );
            System.out.println( "Sent message successfully...." );
        }
        catch( Exception mex )
        {
            mex.printStackTrace();
        }

    }

    public static void main( String[] args )
    {

        String to = "kirill.konoplev@trs-it.ru";
        String from = "balitest@trs-it.ru";
        String host = "172.23.10.20";
        String user = null; //"balitest";
        String password = null; //"1234rewq";
        String subject = "Test Email";
        String body = "Hi there. This is a test email!";
        MailSender.sendHTMLEmail( from, to, host, user, password, subject, body );
        
    }
}