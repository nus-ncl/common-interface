package sg.ncl.service.analytics.data.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class DiskSpace {
    private String spaceSize;
    private String directory;
    private String nclId;
    private String alert;
    private String quota;

    public DiskSpace(String spaceSize, String directory) {
        this(spaceSize, directory, "");
    }

    public DiskSpace(String spaceSize, String directory, String nclId) {
        this.spaceSize = spaceSize;
        this.directory = directory;
        this.nclId = nclId;
    }

    @Override
    public String toString() {
        return "DiskUsage{spaceSize=" + spaceSize + ",directory=" + directory + ",nclId=" + nclId + "}";
    }
}
