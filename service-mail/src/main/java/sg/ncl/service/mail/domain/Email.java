package sg.ncl.service.mail.domain;

import javax.mail.internet.InternetAddress;
import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 8/23/2016.
 */
public interface Email {

    Long getId();

    InternetAddress getSender();

    InternetAddress[] getRecipients();

    InternetAddress[] getCcList();

    String getSubject();

    String getContent();

    boolean isHtml();

    int getRetryTimes();

    ZonedDateTime getLastRetryTime();

    String getErrorMessage();

    boolean isSent();

}
