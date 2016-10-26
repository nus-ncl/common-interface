package sg.ncl.service.data.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.data.domain.DataResource;

/**
 * Created by jng on 17/10/16.
 */
@Getter
public class DataResourceInfo implements DataResource {

    private Long id;
    private String uri;

    @JsonCreator
    public DataResourceInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("uri") final String uri
    ) {
        this.id = id;
        this.uri = uri;
    }

    public DataResourceInfo(final DataResource dataResource) {
        this(dataResource.getId(), dataResource.getUri());
    }

}
