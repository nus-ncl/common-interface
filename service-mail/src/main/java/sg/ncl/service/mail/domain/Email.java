package sg.ncl.service.mail.domain;

import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 8/23/2016.
 */
public interface Email {

    Long getId();

    String getSender();

    String[] getRecipients();

    String[] getCc();

    String[] getBcc();

    String getSubject();

    String getContent();

    boolean isHtml();

    int getRetryTimes();

    ZonedDateTime getLastRetryTime();

    String getErrorMessage();

    boolean isSent();

}
