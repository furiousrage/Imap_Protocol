package com.example.demo.service;

import com.example.demo.dto.EmailDTO;
import com.example.demo.dto.UserDTO;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class ReadingEmailImpl implements IReadingEmail {
    List<EmailDTO> allMessage = null;
            Properties properties = null;
    private Session session = null;
    private Store store = null;
    private Folder inbox = null;


    public List<EmailDTO> readMails(UserDTO userDTO) {

        properties = new Properties();
        properties.setProperty("mail.host", "imap.gmail.com");
        properties.setProperty("mail.port", "995");
        properties.setProperty("mail.transport.protocol", "imaps");
        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userDTO.getEmail(), userDTO.getPassword());
                    }
                });
        try {

            store = session.getStore("imaps");
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            SimpleDateFormat df1 = new SimpleDateFormat( "MM/dd/yy" );
            String dt=userDTO.getDate();
            Date dDate = df1.parse(dt);
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            SearchTerm specificDate = new ReceivedDateTerm(ComparisonTerm.EQ,dDate);
            SearchTerm searchTerm = new AndTerm(unseenFlagTerm, specificDate);
            Message[] messages = inbox.search(searchTerm);

            System.out.println("Number of mails = " + messages.length);
                      allMessage = new ArrayList<>();
            for (Message message : messages) {
                if (message.getSubject().equals("test") ){
                    Address[] from = message.getFrom();
                    System.out.println("-------------------------------");
                    System.out.println("Date : " + message.getSentDate());
                    System.out.println("From : " + from[0]);
                    System.out.println("Subject: " + message.getSubject());
                    System.out.println("Body :");
                    String body = getTextFromMessage(message);
                    System.out.println(body);
                    System.out.println("--------------------------------");
//                String body = getTextFromMessage(message);
                    EmailDTO emailDTO = new EmailDTO();
                    emailDTO.setFrom(from[0].toString());
                    emailDTO.setSubject(message.getSubject());
                    emailDTO.setBody(body);
                    emailDTO.setDate(message.getSentDate().toString());
                    allMessage.add(emailDTO);
                }
            }
            inbox.close(true);
            store.close();

        } catch (Exception e) {
           e.printStackTrace();
        }
        return allMessage;

    }

    private String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")){
            return message.getContent().toString();
        }else if (message.isMimeType("multipart/*")) {
            String result = "";
            MimeMultipart mimeMultipart = (MimeMultipart)message.getContent();
            int count = mimeMultipart.getCount();
            for (int i = 0; i < count; i ++){
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")){
                    result = result + "\n" + bodyPart.getContent();
                    break;  //without break same text appears twice in my tests
                } else if (bodyPart.isMimeType("text/html")){
                    String html = (String) bodyPart.getContent();
                    result = result + "\n" + Jsoup.parse(html).text();

                }
            }
            return result;
        }
        return "";
    }
}
