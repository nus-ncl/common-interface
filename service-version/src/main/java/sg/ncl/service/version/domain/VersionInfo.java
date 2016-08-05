package sg.ncl.service.version.domain;

import java.time.ZonedDateTime;

/**
 * @author Christopher Zhong
 */
public class VersionInfo {

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

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getBuild() {
        return build;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VersionInfo that = (VersionInfo) o;

        if (getMajor() != that.getMajor()) {
            return false;
        }
        if (getMinor() != that.getMinor()) {
            return false;
        }
        if (!getBuild().equals(that.getBuild())) {
            return false;
        }
        return getDate().equals(that.getDate());
    }

    @Override
    public int hashCode() {
        int result = getMajor();
        result = 31 * result + getMinor();
        result = 31 * result + getBuild().hashCode();
        result = 31 * result + getDate().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "major=" + major +
                ", minor=" + minor +
                ", build='" + build + '\'' +
                ", date=" + date +
                '}';
    }

}
