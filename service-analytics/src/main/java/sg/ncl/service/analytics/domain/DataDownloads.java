package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 */
public interface DataDownloads {

    /**
     * Returns a  {@link Long} that represents the unique identifier of this {@link DataDownloads}.
     *
     * @return a  {@link Long} that represents the unique identifier of this {@link DataDownloads}.
     */
    Long getId();

    /**
     *Returns a  {@link Long} that represents the data id of this {@link DataDownloads}.
     *
     * @return a  a  {@link Long} that represents the data id of this {@link DataDownloads}.
     */

    Long getDataId();

    /**
     * Returns a  {@link Long}  that represents the resource id of this {@link DataDownloads}.
     *
     * @return a  a  {@link Long}  that represents the resource id of this {@link DataDownloads}.
     */
    Long getResourceId();

    /**
     * Returns a  {@link ZonedDateTime }  that represents the date this {@link DataDownloads}.
     *
     * @return a  a  {@link ZonedDateTime }  that represents the date of this {@link DataDownloads}.
     */
    ZonedDateTime getDownloadDate();

    /**
     * Returns a  {@link String}  that represents hashed user id of this {@link DataDownloads}.
     *
     * @return a  a  {@link String}  that represents the hashed user id of this {@link DataDownloads}.
     */
    String getHashedUserId();

}
