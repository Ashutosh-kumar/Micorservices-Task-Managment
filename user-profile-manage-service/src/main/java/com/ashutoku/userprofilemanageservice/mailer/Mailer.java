package com.ashutoku.userprofilemanageservice.mailer;

import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Mailer{
	
	private static final Logger LOG = LoggerFactory.getLogger(Mailer.class);
	
    public static void send(String from,String password,String to,String sub,String msg){  
          //Get properties object    
          Properties props = new Properties();    
          props.put("mail.smtp.host", "smtp.gmail.com");    
          props.put("mail.smtp.socketFactory.port", "465");    
          props.put("mail.smtp.socketFactory.class",    
                    "javax.net.ssl.SSLSocketFactory");    
          props.put("mail.smtp.auth", "true");    
          props.put("mail.smtp.port", "465");    
          //get Session   
          Session session = Session.getDefaultInstance(props,    
           new javax.mail.Authenticator() {    
           protected PasswordAuthentication getPasswordAuthentication() {    
           return new PasswordAuthentication(from,password);  
           }    
          });    
          //compose message    
          try {    
           MimeMessage message = new MimeMessage(session);    
           message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
           message.setSubject(sub);    
           message.setText(msg);    
           //send message  
           Transport.send(message);    
           LOG.info("message sent successfully");    
          } catch (MessagingException e) {throw new RuntimeException(e);}    
             
    } 
    
  //  public static void sendEmailNotification() {    
   //     //from,password,to,subject,message  
   //     Mailer.send("ashutosh.kp@gmail.com","xxxxxxx","ashutoshkprasad29@gmail.com","hello javatpoint","How r u?");  
   //     //change from, password and to  
   // }  
    public static void sendEmailNotification(String to,String sub,String msg) {    
        //from,password,to,subject,message  
        Mailer.send("ashutosh.kp@gmail.com","xxxxx",to,sub,msg);  
        //change from, password and to  
    }  
}  
   
  
  