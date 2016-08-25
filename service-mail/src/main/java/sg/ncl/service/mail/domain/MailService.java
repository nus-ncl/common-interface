package sg.ncl.service.mail.domain;

import sg.ncl.service.mail.data.jpa.EmailEntity;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by dcszwang on 8/10/2016.
 */
public interface MailService {

    void send(InternetAddress from, InternetAddress[] to, InternetAddress[] ccList, String subject, String content, boolean isHtml);
    void send(EmailEntity entity, MimeMessage message);
    MimeMessage prepareMessage(Email email);

}
