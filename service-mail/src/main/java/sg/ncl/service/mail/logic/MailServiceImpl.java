package sg.ncl.service.mail.logic;

import java.util.Properties;

/**
 * Created by dcszwang on 8/10/2016.
 */
public class MailServiceImpl {

    public void sendMail() throws MessagingException {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        props.put("mail.smtp.port", "465"); //SMTP Port
        //props.put("mail.smtp.starttls.enable", "true");

        //sender.setHost("smtp.gmail.com");
        //sender.setPort(587);
        sender.setUsername("testbed-ops@ncl.sg");
        //sender.setUsername("zhangchunwang@gmail.com");
        sender.setPassword("deterinavm");

        sender.setJavaMailProperties(props);

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo("dcszwang@nus.edu.sg");
        helper.setText("Testing email from NCL testbed.");
        helper.setSubject("Test Email");
        //helper.setFrom("testbed-ops@ncl.sg");


        sender.send(message);
    }
}
