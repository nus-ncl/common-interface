package sg.ncl.service.analytic.domain;


import sg.ncl.service.analytic.data.jpa.DatasetEntity;

/**
 * @author: Tran Ly Vu
 */

public interface DatasetResources {

    /**
     * Returns a  {@link Long} that represents the unique identifier of this {@link DatasetResources}.
     *
     * @return a  {@link Long} that represents the unique identifier of this {@link DatasetResources}.
     */
    Long getId();

    /**
     * Returns a  {@link Dataset} of this {@link DatasetResources}.
     *
     * @return a  {@link Dataset} of this {@link DatasetResources}.
     */
    Dataset getDataSet();



    /**
     * Returns a  {@link String} of this {@link DatasetResources}.
     *
     * @return a  {@link String} of this {@link DatasetResources}.
     */
    String getUrl();


}
