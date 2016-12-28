package sg.ncl.service.analytics.domain;

import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 */
public interface DataDownload {

    /**
     * Returns a  {@link Long} that represents the unique identifier of this {@link DataDownload}.
     *
     * @return a  {@link Long} that represents the unique identifier of this {@link DataDownload}.
     */
    Long getId();

    /**
     *Returns a  {@link Long} that represents the data id of this {@link DataDownload}.
     *
     * @return a  a  {@link Long} that represents the data id of this {@link DataDownload}.
     */

    Long getDataId();

    /**
     * Returns a  {@link Long}  that represents the resource id of this {@link DataDownload}.
     *
     * @return a  a  {@link Long}  that represents the resource id of this {@link DataDownload}.
     */
    Long getResourceId();

    /**
     * Returns a  {@link ZonedDateTime }  that represents the date this {@link DataDownload}.
     *
     * @return a  a  {@link ZonedDateTime }  that represents the date of this {@link DataDownload}.
     */
    ZonedDateTime getDownloadDate();

    /**
     * Returns a  {@link String}  that represents hashed user id of this {@link DataDownload}.
     *
     * @return a  a  {@link String}  that represents the hashed user id of this {@link DataDownload}.
     */
    String getHashedUserId();

}
