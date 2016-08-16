package sg.ncl.service.mail.domain;

import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by dcszwang on 8/10/2016.
 */

public interface MailService {

    void send(String from, String to, String subject, String content);

    void send(SimpleMailMessage msg);
}
