package sg.ncl.service.data.domain;

/**
 * Created by jng on 25/10/16.
 */
public interface DataRequest {

    Long getId();

    Long getRequestorId();

    Long getApproverId();

    Long getDataId();

    String remarks();

}
