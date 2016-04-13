package sg.ncl.testbed_interface;

import java.time.LocalDateTime;

/**
 * @author Christopher Zhong
 */
public class VersionInfo {

    private final int major;
    private final int minor;
    private final String build;
    private final LocalDateTime date;

    protected VersionInfo(final int major, final int minor, final String build, final LocalDateTime date) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.date = date;
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public String getBuild() {
        return this.build;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        VersionInfo that = (VersionInfo) o;

        if (this.getMajor() != that.getMajor()) return false;
        if (this.getMinor() != that.getMinor()) return false;
        if (!this.getBuild().equals(that.getBuild())) return false;
        return this.getDate().equals(that.getDate());
    }

    @Override
    public int hashCode() {
        int result = this.getMajor();
        result = 31 * result + this.getMinor();
        result = 31 * result + this.getBuild().hashCode();
        result = 31 * result + this.getDate().hashCode();
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
