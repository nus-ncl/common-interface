package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

public interface DataPublicDownload {

    Long getId();

    Long getDataId();

    Long getResourceId();

    ZonedDateTime getDownloadDate();

    Long getPublicUserId();

}
