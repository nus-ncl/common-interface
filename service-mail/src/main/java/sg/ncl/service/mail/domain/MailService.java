package sg.ncl.service.mail.domain;

/**
 * Created by dcszwang on 8/10/2016.
 */
public interface MailService {

    void send(String from, String recipients, String subject, String content, boolean html, String cc, String bcc);

    void send(String from, String[] recipients, String subject, String content, boolean html, String[] cc, String[] bcc);

    void send(Email email);

}
