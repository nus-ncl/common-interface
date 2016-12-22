package sg.ncl.service.analytic.domain;

import java.time.ZonedDateTime;

/**
 * @author: Tran Ly Vu
 */
public interface DatasetDownloads {

    /**
     * Returns a  {@link Long} that represents the unique identifier of this {@link DatasetDownloads}.
     *
     * @return a  {@link Long} that represents the unique identifier of this {@link DatasetDownloads}.
     */
    Long getId();

    /**
     *Returns a  {@link Long} that represents the data id of this {@link DatasetDownloads}.
     *
     * @return a  a  {@link Long} that represents the data id of this {@link DatasetDownloads}.
     */

    Long getDataId();

    /**
     * Returns a  {@link Long}  that represents the resource id of this {@link DatasetDownloads}.
     *
     * @return a  a  {@link Long}  that represents the resource id of this {@link DatasetDownloads}.
     */
    Long getResourceId();

    /**
     * Returns a  {@link ZonedDateTime }  that represents the date this {@link DatasetDownloads}.
     *
     * @return a  a  {@link ZonedDateTime }  that represents the date of this {@link DatasetDownloads}.
     */
    ZonedDateTime getDownloadDate();

    /**
     * Returns a  {@link String}  that represents hashed user id of this {@link DatasetDownloads}.
     *
     * @return a  a  {@link String}  that represents the hashed user id of this {@link DatasetDownloads}.
     */
    String getHashedUserId();

}
