package sg.ncl.service.mail.domain;

import javax.mail.internet.InternetAddress;

/**
 * Created by dcszwang on 8/10/2016.
 */

@FunctionalInterface
public interface MailService {

    void send(InternetAddress from, InternetAddress to, String subject, String content, boolean isHtml);
}
