package sg.ncl.service.version.domain;

import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
public interface Version {

    int getMajor();

    int getMinor();

    String getBuild();

    ZonedDateTime getDate();

}
