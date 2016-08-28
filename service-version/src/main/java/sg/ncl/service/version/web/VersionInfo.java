package sg.ncl.service.version.web;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sg.ncl.service.version.domain.Version;

import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
@Getter
@EqualsAndHashCode
@ToString
public class VersionInfo implements Version {

    private final int major;
    private final int minor;
    private final String build;
    private final ZonedDateTime date;

    public VersionInfo(int major, int minor, String build, ZonedDateTime date) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.date = date;
    }

}
