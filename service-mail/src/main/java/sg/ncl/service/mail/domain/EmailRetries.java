package sg.ncl.service.mail.domain;

import javax.mail.Message;
import java.time.ZonedDateTime;

/**
 * Created by dcszwang on 8/23/2016.
 */
public interface EmailRetries {

    Long getId();

    Message getMessage();

    int getRetriedTimes();

    ZonedDateTime getLastRetryTime();

    String getErrInfo();
}
