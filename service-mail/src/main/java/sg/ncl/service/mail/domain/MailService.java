package sg.ncl.service.mail.domain;

/**
 * Created by dcszwang on 8/10/2016.
 */
public interface MailService {

    /**
     *
     * @param from
     * @param to
     * @param subject
     * @param content
     */
    void send (String from, String to, String subject, String content);
}
