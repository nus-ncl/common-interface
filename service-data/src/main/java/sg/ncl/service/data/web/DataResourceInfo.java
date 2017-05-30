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
    private boolean malicious;

    @JsonCreator
    public DataResourceInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("uri") final String uri,
            @JsonProperty("is_malicious") final boolean malicious
    ) {
        this.id = id;
        this.uri = uri;
        this.malicious = malicious;
    }

    public DataResourceInfo(final DataResource dataResource) {
        this(dataResource.getId(), dataResource.getUri(), dataResource.isMalicious());
    }

}
