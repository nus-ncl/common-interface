package sg.ncl.service.mail.domain;

/**
 * Created by dcszwang on 8/10/2016.
 */

@FunctionalInterface
public interface MailService {

    void send(String from, String to, String subject, String content);
}
